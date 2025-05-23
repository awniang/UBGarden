package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.Walkable;
import fr.ubx.poo.ubgarden.game.go.personage.Character;

public class Door extends Decor implements Walkable {

    private boolean isOpen = false;
    private final int targetLevel;

    public Door (Position position, int targetLevel){
        super(position);
        this.targetLevel = targetLevel;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getTargetLevel() {
        return targetLevel;
    }

    public void update(long now) {
        setModified(true);
    }

    @Override
    public boolean walkableBy(Character character) {
        return character.canWalkOn(this);
    }

    @Override
    public void handleLevelTransition(Game game) {
        if (isOpen()) {
            game.requestSwitchLevel(targetLevel);
        }
    }

    @Override
    public void openDoor() {
        this.isOpen = true;
    }
}
