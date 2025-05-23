package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.go.personage.Wasp;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteWasp extends SpriteInsect{
    public SpriteWasp(Pane layer, Wasp wasp) {
        super(layer, wasp);
    }

    @Override
    public Image getImage(Direction direction) {
        return ImageResourceFactory.getInstance().getWasp(direction);
    }
}




