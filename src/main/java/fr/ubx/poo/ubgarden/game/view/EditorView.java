package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.launcher.MapEntity;
import fr.ubx.poo.ubgarden.game.launcher.MapLevel;
import fr.ubx.poo.ubgarden.game.launcher.MapRepoCompressedString;
import fr.ubx.poo.ubgarden.game.launcher.MapRepoString;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditorView extends BorderPane {
    private final List<MapLevel> maps;
    private int currentLevelIndex;
    private final Stage stage;
    private final VBox editorPane = new VBox();
    private MapEntity selected = MapEntity.Grass;
    private final TextField widthField;
    private final TextField heightField;
    private final ComboBox<Integer> levelSelector;

    public EditorView(Stage stage) {
        this.stage = stage;
        this.maps = new ArrayList<>();
        this.maps.add(new MapLevel(10, 10));
        this.currentLevelIndex = 0;

        // Menu
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem newMap = new MenuItem("New Map");
        MenuItem addLevel = new MenuItem("Add Level");
        MenuItem saveCompressedMap = new MenuItem("Save Compressed Map");
        MenuItem saveStringMap = new MenuItem("Save String Map");
        MenuItem previewMap = new MenuItem("Preview Map");
        fileMenu.getItems().addAll(newMap, addLevel, saveCompressedMap, saveStringMap, previewMap);
        menuBar.getMenus().add(fileMenu);
        this.setTop(menuBar);

        // Panneau de controle (taille et niveau)
        HBox controlPane = new HBox(10);
        widthField = new TextField("10");
        heightField = new TextField("10");
        levelSelector = new ComboBox<>();
        levelSelector.getItems().add(1);
        levelSelector.setValue(1);
        Button applySizeButton = new Button("Apply Size");
        controlPane.getChildren().addAll(
                new Label("Width:"), widthField,
                new Label("Height:"), heightField,
                applySizeButton,
                new Label("Level:"), levelSelector
        );
        editorPane.getChildren().add(controlPane);

        // sélecteur des entities
        HBox entitySelector = new HBox();
        for (MapEntity entity : MapEntity.values()) {
            Image image = ImageResourceFactory.getInstance().get(imageResourceFor(entity));
            ImageView icon = new ImageView(image);
            icon.setFitWidth(ImageResource.size);
            icon.setFitHeight(ImageResource.size);
            Button btn = new Button();
            btn.setGraphic(icon);
            btn.setOnAction(e -> selected = entity);
            entitySelector.getChildren().add(btn);
        }
        editorPane.getChildren().add(entitySelector);

        Label gardenerReminderLabel = new Label("Place a Gardener in Level 1!");
        gardenerReminderLabel.setStyle("-fx-text-fill: red;");
        editorPane.getChildren().add(gardenerReminderLabel);

        // nouvelle carte
        newMap.setOnAction(e -> {
            try {
                int width = Integer.parseInt(widthField.getText().trim());
                int height = Integer.parseInt(heightField.getText().trim());
                if (width <= 0 || height <= 0) {
                    showErrorAlert("Invalid Size", "Width and height must be positive integers.");
                    return;
                }
                maps.clear();
                maps.add(new MapLevel(width, height));
                currentLevelIndex = 0;
                levelSelector.getItems().clear();
                levelSelector.getItems().add(1);
                levelSelector.setValue(1);
                updateGrid();
            } catch (NumberFormatException ex) {
                showErrorAlert("Invalid Input", "Please enter valid integers for width and height.");
            }
        });

        //ajouter un niveau
        addLevel.setOnAction(e -> {
            try {
                int width = Integer.parseInt(widthField.getText().trim());
                int height = Integer.parseInt(heightField.getText().trim());
                if (width <= 0 || height <= 0) {
                    showErrorAlert("Invalid Size", "Width and height must be positive integers.");
                    return;
                }
                maps.add(new MapLevel(width, height));
                currentLevelIndex = maps.size() - 1;
                levelSelector.getItems().add(maps.size());
                levelSelector.setValue(maps.size());
                updateGrid();
            } catch (NumberFormatException ex) {
                showErrorAlert("Invalid Input", "Please enter valid integers for width and height.");
            }
        });

        //change le niveau à éditer
        levelSelector.setOnAction(e -> {
            if (levelSelector.getValue() != null) {
                currentLevelIndex = levelSelector.getValue() - 1;
                updateGrid();
            }
        });

        // appliquer la taille
        applySizeButton.setOnAction(e -> {
            try {
                int width = Integer.parseInt(widthField.getText().trim());
                int height = Integer.parseInt(heightField.getText().trim());
                if (width <= 0 || height <= 0) {
                    showErrorAlert("Invalid Size", "Width and height must be positive integers.");
                    return;
                }
                maps.set(currentLevelIndex, new MapLevel(width, height));
                updateGrid();
            } catch (NumberFormatException ex) {
                showErrorAlert("Invalid Input", "Please enter valid integers for width and height.");
            }
        });

        // sauvegarder la carte
        saveCompressedMap.setOnAction(e -> saveMap(true));
        saveStringMap.setOnAction(e -> saveMap(false));

        // aperçu de la carte
        previewMap.setOnAction(e -> {
            Stage previewStage = new Stage();
            previewStage.initModality(Modality.APPLICATION_MODAL);
            previewStage.setTitle("Map Preview");

            VBox previewPane = new VBox(10);
            ComboBox<Integer> previewLevelSelector = new ComboBox<>();
            for (int i = 1; i <= maps.size(); i++) {
                previewLevelSelector.getItems().add(i);
            }
            previewLevelSelector.setValue(1);

            GridPane previewGrid = new GridPane();
            updatePreviewGrid(previewGrid, maps.get(0));

            previewLevelSelector.setOnAction(ev -> {
                if (previewLevelSelector.getValue() != null) {
                    updatePreviewGrid(previewGrid, maps.get(previewLevelSelector.getValue() - 1));
                }
            });

            Button saveCompressedButton = new Button("Save as Compressed");
            Button saveStringButton = new Button("Save as String");
            Button cancelButton = new Button("Cancel");

            saveCompressedButton.setOnAction(ev -> {
                previewStage.close();
                saveMap(true);
            });
            saveStringButton.setOnAction(ev -> {
                previewStage.close();
                saveMap(false);
            });
            cancelButton.setOnAction(ev -> previewStage.close());

            HBox buttonPane = new HBox(10, saveCompressedButton, saveStringButton, cancelButton);
            previewPane.getChildren().addAll(new Label("Select Level:"), previewLevelSelector, previewGrid, buttonPane);

            Scene previewScene = new Scene(previewPane);
            previewStage.setScene(previewScene);
            previewStage.showAndWait();
        });

        this.setCenter(editorPane);
        updateGrid();
    }

    private void saveMap(boolean compressed) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Map Files", "*.properties"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("compression = " + compressed + "\n");
                writer.write("levels = " + maps.size() + "\n");
                for (int i = 0; i < maps.size(); i++) {
                    writer.write("level" + (i + 1) + " = ");
                    if (compressed) {
                        new MapRepoCompressedString().export(maps.get(i), writer);
                    } else {
                        new MapRepoString().export(maps.get(i), writer);
                    }
                    writer.write("\n");
                }
                writer.write("gardenerEnergy = 200\n");
                writer.write("energyBoost = 50\n");
                writer.write("energyRecoverDuration = 1000\n");
                writer.write("diseaseDuration = 5000\n");
                writer.write("waspMoveFrequency = 1\n");
                writer.write("hornetMoveFrequency = 2\n");
            } catch (IOException ex) {
                showErrorAlert("Save Error", "Error while saving the map: " + ex.getMessage());
            }
        }
    }

    private void updatePreviewGrid(GridPane gridPane, MapLevel map) {
        gridPane.getChildren().clear();
        for (int y = 0; y < map.height(); y++) {
            for (int x = 0; x < map.width(); x++) {
                MapEntity entity = map.get(x, y);
                Image image = ImageResourceFactory.getInstance().get(imageResourceFor(entity));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(ImageResource.size);
                imageView.setFitHeight(ImageResource.size);
                gridPane.add(imageView, x, y);
            }
        }
    }

    public void updateGrid() {
        GridPane gridPane = new GridPane();
        MapLevel map = maps.get(currentLevelIndex);
        for (int y = 0; y < map.height(); y++) {
            for (int x = 0; x < map.width(); x++) {
                MapEntity entity = map.get(x, y);
                Image image = ImageResourceFactory.getInstance().get(imageResourceFor(entity));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(ImageResource.size);
                imageView.setFitHeight(ImageResource.size);
                Button cell = new Button();
                cell.setGraphic(imageView);
                cell.setPrefSize(ImageResource.size, ImageResource.size);
                final int fx = x, fy = y;
                cell.setOnAction(e -> {
                    map.set(fx, fy, selected);
                    Image newImage = ImageResourceFactory.getInstance().get(imageResourceFor(selected));
                    ImageView newView = new ImageView(newImage);
                    newView.setFitWidth(ImageResource.size);
                    newView.setFitHeight(ImageResource.size);
                    cell.setGraphic(newView);
                });
                gridPane.add(cell, x, y);
            }
        }

        if (editorPane.getChildren().size() > 3) {
            editorPane.getChildren().remove(3);
        }
        editorPane.getChildren().add(gridPane);
        stage.sizeToScene();
    }

    private ImageResource imageResourceFor(MapEntity entity) {
        return switch (entity) {
            case Grass -> ImageResource.GRASS;
            case Land -> ImageResource.LAND;
            case Carrots -> ImageResource.CARROTS;
            case Flowers -> ImageResource.FLOWERS;
            case PoisonedApple -> ImageResource.POISONED_APPLE;
            case Apple -> ImageResource.APPLE;
            case Tree -> ImageResource.TREE;
            case Insecticide -> ImageResource.INSECTICIDE;
            case Gardener -> ImageResource.GARDENER_DOWN;
            case Hedgehog -> ImageResource.HEDGEHOG;
            case Hornet -> ImageResource.HORNET_DOWN;
            case Wasp -> ImageResource.WASP_DOWN;
            case NestWasp -> ImageResource.NESTWASP;
            case NestHornet -> ImageResource.NESTHORNET;
            case DoorPrevOpened -> ImageResource.DOOR_OPENED_MINUS;
            case DoorNextOpened -> ImageResource.DOOR_OPENED_PLUS;
            case DoorNextClosed -> ImageResource.DOOR_CLOSED_PLUS;
        };
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}