package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.go.personage.Hornet;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;


public class SpriteHornet extends SpriteInsect {

    public SpriteHornet(Pane layer, Hornet hornet) {
        super(layer, hornet);
    }

    @Override
    public Image getImage(Direction direction) {
        return ImageResourceFactory.getInstance().getHornet(direction);
    }
}
