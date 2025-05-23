package fr.ubx.poo.ubgarden.game.view;

import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import fr.ubx.poo.ubgarden.game.go.personage.Character;
import fr.ubx.poo.ubgarden.game.Direction;

public abstract class SpriteCharacter extends Sprite {
    public SpriteCharacter(Pane layer, Character character) {
        super(layer, null, character);
        updateImage();
    }

    @Override
    public void updateImage() {
        Character character = (Character) getGameObject();
        Image image = getImage(character.getDirection());
        setImage(image);
    }

    public abstract Image getImage(Direction direction);
}