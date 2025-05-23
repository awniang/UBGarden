package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.decor.Flowers;
import fr.ubx.poo.ubgarden.game.go.decor.Tree;


public abstract class Insect extends Character {

    private final Timer moveTimer;

    public Insect(Game game, Position position, int moveFrequency, int energy) {
        super(game, position, energy);
        this.moveTimer = new Timer(moveFrequency);
        this.moveTimer.start();
    }

    public void update(long now) {
        moveTimer.update(now);
        if (!moveTimer.isRunning()) {
            setDirection(Direction.random());
            if (canMove(getDirection())) {
                move(getDirection());
            }
            moveTimer.start();
        }
    }

    public void hurt(int damage) {
        super.hurt(damage);
        if (getEnergy() <= 0) {
            game.removeInsect(this);
        }
    }

    @Override
    public boolean canWalkOn(Tree tree) {
        return true;
    }

    @Override
    public boolean canWalkOn(Flowers flowers) {
        return true;
    }

    public abstract String getInsectType();
    public abstract int getDamage();
}
