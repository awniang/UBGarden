package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.personage.Insect;
import fr.ubx.poo.ubgarden.game.go.personage.Wasp;

public class NestWasp extends Nest {

    public NestWasp(Game game, Position position) {
        super(game, position, 5000); // 5 secondes
    }

    public Insect createInsect() {
        return new Wasp(getGame(), getPosition());
    }
    @Override
    public String getInsectType() {
        return "Wasp";
    }

    public int insecticideCount(){
        return 1;
    }
}