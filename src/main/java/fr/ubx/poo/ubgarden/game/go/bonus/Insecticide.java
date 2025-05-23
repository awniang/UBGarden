package fr.ubx.poo.ubgarden.game.go.bonus;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import fr.ubx.poo.ubgarden.game.go.personage.Insect;

public class Insecticide  extends Bonus{
    public Insecticide(Position position, Decor decor ) {
        super(position, decor);
        setInsecticide(true);
    }

    @Override
    public void pickUpBy(Gardener gardener) {
        gardener.pickUp(this);
    }

}
