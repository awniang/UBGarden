/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteGardener extends SpriteCharacter {
    public SpriteGardener(Pane layer, Gardener gardener) {
        super(layer, gardener);
    }

    @Override
    public Image getImage(Direction direction) {
        return ImageResourceFactory.getInstance().getGardener(direction);
    }

    @Override
    public void render() {
        super.render();
        toFront();
    }
}
