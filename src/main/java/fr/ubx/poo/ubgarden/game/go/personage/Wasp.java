package fr.ubx.poo.ubgarden.game.go.personage;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.view.Sprite;
import fr.ubx.poo.ubgarden.game.view.SpriteWasp;
import javafx.scene.layout.Pane;


public class Wasp extends Insect {
    public Wasp(Game game, Position position) {
        super(game, position, game.configuration().waspMoveFrequency() * 1000,1); // Wasp a une énergie de 1 point
    }

    @Override
    public String getInsectType() {
        return "Wasp";
    }

    @Override
    public int getDamage(){
        return 20;
    }

    public Sprite createSprite(Pane layer) {
        return new SpriteWasp(layer, this);
    }
}