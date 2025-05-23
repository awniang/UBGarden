package fr.ubx.poo.ubgarden.game;


import fr.ubx.poo.ubgarden.game.go.bonus.Carrots;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;

import java.util.Collection;

public interface Map {
    int width();

    int height();

    Decor get(Position position);

    Collection<Decor> values();
    Collection<Carrots> valuesCarrots();
    Carrots getCarrot(Position position);

    boolean inside(Position nextPos);
}
