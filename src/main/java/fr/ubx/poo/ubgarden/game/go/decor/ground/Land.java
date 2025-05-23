package fr.ubx.poo.ubgarden.game.go.decor.ground;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Land extends Ground {
    public Land(Position position){
        super(position);
    }

    @Override
    public void pickUpBy(Gardener gardener) {
        Bonus bonus = getBonus();
        if (bonus != null) {
            bonus.pickUpBy(gardener);
        }
    }

    @Override
    public int energyConsumptionWalk() {
        return 2;
    }
}
