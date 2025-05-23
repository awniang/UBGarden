package fr.ubx.poo.ubgarden.game.go.bonus;
import fr.ubx.poo.ubgarden.game.Position;

import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Carrots  extends Bonus{
    private boolean collected = false;

    public Carrots(Position position, Decor decor) {
        super(position, decor);
    }
    public void pickUpBy(Gardener gardener) {
        gardener.pickUp(this);
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

}
