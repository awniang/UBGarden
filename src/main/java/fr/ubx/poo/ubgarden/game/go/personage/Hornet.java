package fr.ubx.poo.ubgarden.game.go.personage;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.view.Sprite;
import fr.ubx.poo.ubgarden.game.view.SpriteHornet;
import javafx.scene.layout.Pane;

public class Hornet extends Insect {
    public Hornet(Game game, Position position) {
        super(game, position,game.configuration().hornetMoveFrequency() * 1000,2); // Hornet à une énergie de 2 points
    }

    @Override
    public String getInsectType() {
        return "Hornet";
    }

    @Override
    public int getDamage(){
        return 30;
    }

    public Sprite createSprite(Pane layer) {
        return new SpriteHornet(layer, this);
    }
}