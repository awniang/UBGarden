/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go.decor.ground;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Grass extends Ground {
    public Grass(Position position) {
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
    public boolean isFree() {
        return getBonus() == null;
    } // est ce qu'on peut placer un bonus dessus

    @Override
    public int energyConsumptionWalk() {
        return 1;
    }

}
