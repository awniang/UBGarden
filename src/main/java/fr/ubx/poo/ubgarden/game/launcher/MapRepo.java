package fr.ubx.poo.ubgarden.game.launcher;

import java.io.IOException;
import java.io.Writer;

public interface MapRepo {
    char EOL = 'x';
    MapLevel load(String string);
    void export(MapLevel map, Writer writer) throws IOException;
}
