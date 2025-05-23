package fr.ubx.poo.ubgarden.game.view;


import fr.ubx.poo.ubgarden.game.go.personage.Insect;
import javafx.scene.layout.Pane;


public abstract class SpriteInsect extends SpriteCharacter {
    public SpriteInsect(Pane layer, Insect insect) {
        super(layer, insect);
    }
}