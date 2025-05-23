package fr.ubx.poo.ubgarden.game.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class MapRepoString implements MapRepo{

    public MapLevel load(String string) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("Map string cannot be null or empty");
        }

        String[] rows = string.split(String.valueOf(EOL));
        int height = rows.length;
        int width = rows[0].length();

        for (String row : rows) {
            if (row.length() != width) {
                throw new IllegalArgumentException("All rows must have the same length");
            }
        }

        MapLevel map = new MapLevel(width, height);

        for (int j = 0; j < height; j++) {
            String row = rows[j];
            for (int i = 0; i < width; i++) {
                char c = row.charAt(i);
                MapEntity entity = MapEntity.fromCode(c);
                map.set(i, j, entity);
            }
        }

        return map;
    }

    public String export(MapLevel map) {
        StringBuilder mapString = new StringBuilder();
        int height = map.height();
        int width = map.width();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                MapEntity entity = map.get(x, y);
                if (entity == null) {
                    throw new IllegalArgumentException("L'entité est nulle");
                }
                mapString.append(entity.getCode());
            }
            mapString.append(EOL);
        }

        return mapString.toString();
    }

    public void export(MapLevel map, Writer writer) throws IOException {
        writer.write(export(map));
    }


}
