/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go.bonus;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Pickupable;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.personage.Insect;

public abstract class Bonus extends GameObject implements Pickupable {

    private final Decor decor;
    private boolean isInsecticide =false;

    public void setInsecticide(boolean insecticide) {
        isInsecticide = insecticide;
    }

    public Bonus(Position position, Decor decor) {
        super(position);
        this.decor = decor;
    }

    @Override
    public void remove() {
        super.remove();
        decor.setBonus(null);
    }

    public void affectInsect(Insect insect){
        if (isInsecticide) {
            insect.hurt(1);
            remove();
        }
    }

}
