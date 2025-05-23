package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.personage.Hornet;
import fr.ubx.poo.ubgarden.game.go.personage.Insect;
import fr.ubx.poo.ubgarden.game.go.personage.Wasp;

public class NestHornet extends Nest {

    public NestHornet(Game game, Position position) {
        super(game, position, 10000);
    } //10 secondes

    public Insect createInsect() {
        return new Hornet(getGame(), getPosition());
    }

    @Override
    public String getInsectType() {
        return "Hornet";
    }

    public int insecticideCount(){
        return 2;
    }
}
