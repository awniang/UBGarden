/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.engine;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Level;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.bonus.Insecticide;
import fr.ubx.poo.ubgarden.game.go.decor.*;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import fr.ubx.poo.ubgarden.game.go.personage.Insect;
import fr.ubx.poo.ubgarden.game.view.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.*;

public final class GameEngine {

    private AnimationTimer gameLoop;
    private final Game game;
    private final Gardener gardener;
    private final List<Sprite> characterSprites = new LinkedList<>();
    private final List<Sprite> sprites = new LinkedList<>();
    private final Set<Sprite> cleanUpSprites = new HashSet<>();
    private final Scene scene;
    private StatusBar statusBar;
    private final Pane rootPane = new Pane();
    private final Group root = new Group();
    private final Pane layer = new Pane();
    private Input input;

    private final Random random = new Random();

    public GameEngine(Game game, Scene scene) {
        this.game = game;
        this.scene = scene;
        this.gardener = game.getGardener();
        initialize();
        buildAndSetGameLoop();
    }

    public Pane getRoot() {
        return rootPane;
    }

    private void initialize() {
        int height = game.world().getGrid().height();
        int width = game.world().getGrid().width();
        int sceneWidth = width * ImageResource.size;
        int sceneHeight = height * ImageResource.size;
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
        input = new Input(scene);

        root.getChildren().clear();
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight);

        rootPane.getChildren().clear();
        rootPane.setPrefSize(sceneWidth, sceneHeight + StatusBar.height);
        rootPane.getChildren().add(root);

        // si aucune carrote au chargement la porte su niveau suivant est ouverte
        Level grid = (Level) game.world().getGrid();
        if (grid.valuesCarrots().isEmpty()) {
           grid.openDoors();
        }


        for (var decor : game.world().getGrid().values()) {
            sprites.add(SpriteFactory.create(layer, decor));
            decor.setModified(true);
            var bonus = decor.getBonus();
            if (bonus != null) {
                sprites.add(SpriteFactory.create(layer, bonus));
                bonus.setModified(true);
            }
        }

        Sprite gardenerSprite = gardener.createSprite(layer);
        characterSprites.add(gardenerSprite);
        sprites.add(gardenerSprite);
        gardener.setModified(true);

        for (Insect insect : game.getInsects()) {
            Sprite sprite = insect.createSprite(layer);
            characterSprites.add(sprite);
            sprites.add(sprite);
        }
        resizeScene(sceneWidth, sceneHeight);
        render();
    }

    void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                checkLevel();
                processInput();
                update(now);
                checkCollision();
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }

    private void checkLevel() {
        if (game.isSwitchLevelRequested()) {
            int targetLevel = game.getSwitchLevel();

            if (game.world().getGrid(targetLevel) == null) {
                game.clearSwitchLevel();
                return;
            }
            int previousLevel = game.world().currentLevel();
            game.world().setCurrentLevel(targetLevel);
            Position doorPosition = findDoorPosition(targetLevel,previousLevel);
            if (doorPosition != null) {
                gardener.setPosition(doorPosition);
            }else {
                System.out.println("no door found");
                gardener.setPosition(getRandomFreePosition());
            }

            initialize();
            gardener.setModified(true);
            render();
            game.clearSwitchLevel();
        }
    }

    //trouver la position de la porte pour le changement de niveau
    private Position findDoorPosition(int targetLevel, int previousLevel) {
        Level grid = (Level) game.world().getGrid(targetLevel);
        for (Decor decor : grid.values()) {
            try {
                Door door = (Door) decor;
                if (door.getTargetLevel() == previousLevel) {
                    return decor.getPosition();
                }
            } catch (ClassCastException e) {
                continue;
            }
        }
        return null;
    }

    private void checkCollision() {
        for (Insect insect : new ArrayList<>(game.getInsects())) {
            if (insect.getPosition().equals(gardener.getPosition())) {
                // si possible utiliser les insecticides sur les insectes
                if (gardener.useInsecticide()) {
                    insect.hurt(1);
                } else {
                    gardener.hurt(insect.getDamage());
                    insect.hurt(1); // dans tous les cas la piqure affecte l'insecte
                }
            }
            for (Decor decor : game.world().getGrid().values()) {
                Bonus bonus = decor.getBonus();
                if (bonus != null && insect.getPosition().equals(bonus.getPosition())) {
                    bonus.affectInsect(insect); // par défaut un bonus ne fait rien mais si c'est un insecticide il affecte l'insecte
                }
            }
        }

    }

    private void processInput() {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        } else if (input.isMoveDown()) {
            gardener.requestMove(Direction.DOWN);
        } else if (input.isMoveLeft()) {
            gardener.requestMove(Direction.LEFT);
        } else if (input.isMoveRight()) {
            gardener.requestMove(Direction.RIGHT);
        } else if (input.isMoveUp()) {
            gardener.requestMove(Direction.UP);
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text message = new Text(msg);
        message.setTextAlignment(TextAlignment.CENTER);
        message.setFont(new Font(60));
        message.setFill(color);

        StackPane pane = new StackPane(message);
        pane.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
        rootPane.getChildren().clear();
        rootPane.getChildren().add(pane);

        new AnimationTimer() {
            public void handle(long now) {
                processInput();
            }
        }.start();
    }

    public boolean isFree(Position position) {
        Decor decor = game.world().getGrid().get(position);
        return decor != null && decor.isFree() && !position.equals(gardener.getPosition());
    }

    // cherche une position libre pour placer un insecticide
    public Position getRandomFreePosition() {
        List<Position> freePositions = new ArrayList<>();
        for (int x = 0; x < game.world().getGrid().width(); x++) {
            for (int y = 0; y < game.world().getGrid().height(); y++) {
                Position pos = new Position(game.world().currentLevel(), x, y);
                if (isFree(pos)) {
                    freePositions.add(pos);
                }
            }
        }
        if (freePositions.isEmpty()) {
            System.out.println("Aucune position libre trouvée.");
            return null;
        }
        return freePositions.get(random.nextInt(freePositions.size()));
    }

    // met à jour tous les decors et caracteres
    private void update(long now) {
        game.world().getGrid().values().forEach(decor -> {
            decor.update(now);
            decor.updateNest(this);
        });

        gardener.update(now);

        for (Insect insect : new ArrayList<>(game.getInsects())) {
            insect.update(now);
        }

        if (gardener.getEnergy() == 0) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        if(gardener.isFoundHedgehog()){
            gameLoop.stop();
            showMessage("VICTOIRE!", Color.RED);
        }
    }

    public void updateNestInsects(Nest nest) {
        String insectType = nest.getInsectType();
        int insecticideCount = nest.insecticideCount();

        for (Insect insect : new ArrayList<>(game.getInsects())) {
            // Vérifie si l'insecte correspond au type du nid et n'a pas encore de sprite
            if (insectType.equals(insect.getInsectType()) && !characterSprites.stream().anyMatch(s -> s.getGameObject() == insect)) {
                Sprite sprite = insect.createSprite(layer);
                characterSprites.add(sprite);
                sprites.add(sprite);
                // Génère les insecticides associés
                for (int i = 0; i < insecticideCount; i++) {
                    Position pos = getRandomFreePosition();
                    if (pos != null) {
                        Decor decorAtPos = game.world().getGrid().get(pos);
                        Insecticide insecticide = new Insecticide(pos, decorAtPos);
                        decorAtPos.setBonus(insecticide);
                        insecticide.setModified(true);
                        sprites.add(SpriteFactory.create(layer, insecticide));
                    }
                }
            }
        }
    }



    public void cleanupSprites() {
        sprites.forEach(sprite -> {
            if (sprite.getGameObject().isDeleted()) {
                cleanUpSprites.add(sprite);
            }
        });
        cleanUpSprites.forEach(Sprite::remove);
        sprites.removeAll(cleanUpSprites);
        characterSprites.removeAll(cleanUpSprites);
        cleanUpSprites.clear();

        characterSprites.removeIf(sprite -> sprite.getGameObject().isInsect() && !game.getInsects().contains(sprite.getGameObject()));
        sprites.removeIf(sprite -> sprite.getGameObject().isInsect() && !game.getInsects().contains(sprite.getGameObject()));  }

    private void render() {
        //Afficher les decors d'abord pour que le jardinier puisse etre au dessus (notamment la porte)
        sprites.stream()
                .filter(sprite -> !characterSprites.contains(sprite))
                .forEach(Sprite::render);
        characterSprites.forEach(Sprite::render);
    }

    public void start() {
        gameLoop.start();
    }

    private void resizeScene(int width, int height) {
        rootPane.setPrefSize(width, height + StatusBar.height);
        layer.setPrefSize(width, height);
        Platform.runLater(() -> scene.getWindow().sizeToScene());
    }
}