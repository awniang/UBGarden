/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Level;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.bonus.*;
import fr.ubx.poo.ubgarden.game.go.decor.*;
import fr.ubx.poo.ubgarden.game.view.Sprite;
import fr.ubx.poo.ubgarden.game.view.SpriteGardener;
import javafx.scene.layout.Pane;
import java.util.LinkedList;

public class Gardener extends Character {
    private long lastActivityTime = 0;
    private boolean moveRequested = false;
    private int diseaseLevel = 1;
    private int insecticideCount = 0;
    private final LinkedList<Timer> timersTab = new LinkedList<>();
    private boolean foundHedgehog;

    public Gardener(Game game, Position position) {

        super(game, position,game.configuration().gardenerEnergy());
        setDirection(Direction.DOWN);
    }

    public boolean isFoundHedgehog() {
        return foundHedgehog;
    }

    public int getDiseaseLevel() {
        return diseaseLevel;
    }

    public int getInsecticideCount() {
        return insecticideCount;
    }

    public boolean useInsecticide() {
        if (insecticideCount >0) {
            insecticideCount--;
            return true;
        }
        return false;
    }

    @Override
    public void pickUp(EnergyBoost energyBoost) {
       setEnergy(Math.min(getEnergy() + game.configuration().energyBoost(), game.configuration().gardenerEnergy()));
        this.diseaseLevel = 1;
        energyBoost.remove();
        timersTab.clear();
    }

    public void pickUp(Insecticide bonus) {
        this.insecticideCount++;
        bonus.remove();
    }
    public void pickUp(Hedgehog hedgehog) {
        foundHedgehog = true;
        hedgehog.remove();
    }
    public void pickUp(PoisonedApple bonus) {
        diseaseLevel++;
        hurt(diseaseLevel);
        Timer timerPoisoned = new Timer(game.configuration().diseaseDuration());
        timerPoisoned.start();
        timersTab.add(timerPoisoned);

        bonus.remove();
    }
    public void pickUp(Carrots bonus) {
        bonus.setCollected(true);
        bonus.remove();
        Level level = (Level) game.world().getGrid();
        if (level.allCarrotsCollected()) {
            level.openDoors();
        }
    }


    public void requestMove(Direction direction) {
        if (direction != getDirection()) {
            setDirection(direction);
            setModified(true);
        }
        moveRequested = true;
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor next = game.world().getGrid().get(nextPos);
        next.handleLevelTransition(game);
        int energyCost = diseaseLevel * next.energyConsumptionWalk();
        setPosition(nextPos);
        hurt(energyCost);
        if (next != null)
            next.pickUpBy(this);
        return nextPos;
    }


    public void update(long now) {
        if (moveRequested) {
            setModified(true);
            if (canMove(getDirection())) {
                move(getDirection());
                lastActivityTime = now / 1_000_000; // Convertir en ms
            }
            moveRequested = false;
        }
        long nowMs = now / 1_000_000; // Convertir en ms
        // Ajuster la durée de régénération en fonction du diseaseLevel
        long adjustedRecoverDuration = game.configuration().energyRecoverDuration() * diseaseLevel;
        if (nowMs - lastActivityTime >= adjustedRecoverDuration) {
            if (getEnergy() < game.configuration().gardenerEnergy()) {
                setEnergy(getEnergy() + 1);
                lastActivityTime = nowMs;
            }
        }
        for (Timer timer : timersTab) {
            timer.update(now);
        }
        timersTab.removeIf(timer -> {
            if (!timer.isRunning()) {
                diseaseLevel = Math.max(1, diseaseLevel - 1); // ne descend pas en dessous de 1
                return true;
            }
            return false;
        });

    }

    public boolean canWalkOn(Door door) {
        return door.isOpen();
    }
    public Sprite createSprite(Pane layer) {
        return new SpriteGardener(layer, this);
    }

}
