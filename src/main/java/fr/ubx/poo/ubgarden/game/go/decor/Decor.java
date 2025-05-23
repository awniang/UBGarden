package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.GameEngine;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Pickupable;
import fr.ubx.poo.ubgarden.game.go.Walkable;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.personage.Character;


public abstract class Decor extends GameObject implements Walkable, Pickupable {

    private Bonus bonus;

    public void setNest(boolean nest) {
        isNest = nest;
    }

    private boolean isNest = false;

    public Decor(Position position) {
        super(position);
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    @Override
    public boolean walkableBy(Character character) {
        return character.canWalkOn(this);
    }

    @Override
    public void update(long now) {
        super.update(now);
        if (bonus != null) bonus.update(now);
    }

    public boolean isFree() {
        return false;
    }

    public void updateNest(GameEngine engine) {
        if (isNest) {
            engine.updateNestInsects((Nest)this);
        }
    }
    public void handleLevelTransition(Game game) {}
    public void openDoor() {}
}