package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.PickupVisitor;
import fr.ubx.poo.ubgarden.game.go.WalkVisitor;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.view.Sprite;
import javafx.scene.layout.Pane;

public abstract class Character extends GameObject implements Movable, PickupVisitor, WalkVisitor {
    private Direction direction;
    private int energy;

    public Character(Game game, Position position, int energy) {
        super(game, position);
        this.direction = Direction.random();
        this.energy = energy;
    }

    public Direction getDirection() {
        return direction;
    }
    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void hurt(int damage) {
        this.energy -= damage;
        if (energy < 0) energy = 0;
    }

    @Override
    public final boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor nextDecor = game.world().getGrid().get(nextPos);
        return nextDecor!= null && nextDecor.walkableBy(this) && getEnergy() > 0 && game.world().getGrid().inside(nextPos);
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
        return nextPos;
    }

    public abstract void update(long now);
    public abstract Sprite createSprite(Pane layer);

}


