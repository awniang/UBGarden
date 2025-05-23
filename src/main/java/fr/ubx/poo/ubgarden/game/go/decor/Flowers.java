package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Character;

public class Flowers extends Decor {
    public Flowers(Position position) {
        super(position);
    }

    public boolean walkableBy(Character character) {
        return character.canWalkOn(this);
    }
}
