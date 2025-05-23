package fr.ubx.poo.ubgarden.game.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class MapRepoCompressedString implements MapRepo {
    @Override
    public MapLevel load(String string) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("Map string cannot be null or empty");
        }

        String decompressedString = decompress(string);
        MapRepoString mapRepoString = new MapRepoString();
        return mapRepoString.load(decompressedString);
    }

    public String export(MapLevel map) {
        MapRepoString mapRepoString = new MapRepoString();
        String uncompressed = mapRepoString.export(map);
        return compress(uncompressed);
    }
    public void export(MapLevel map, Writer writer) throws IOException {
        writer.write(export(map));
    }

    private String compress(String uncompressed) {
        StringBuilder compressed = new StringBuilder();
        String[] rows = uncompressed.split(String.valueOf(EOL));

        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            int j = 0;
            while (j < row.length()) {
                char c = row.charAt(j);
                int count = 1;
                while (j + 1 < row.length() && row.charAt(j + 1) == c) {
                    count++;
                    j++;
                }
                compressed.append(c);
                if (count > 1) {
                    compressed.append(count);
                }
                j++;
            }
            if (i < rows.length - 1) {
                compressed.append(EOL);
            }
        }

        return compressed.toString();
    }

    private String decompress(String compressed) {
        if (compressed == null || compressed.isEmpty()) {
            throw new IllegalArgumentException("Compressed string cannot be null or empty");
        }

        StringBuilder decompressed = new StringBuilder();
        String[] rows = compressed.split(String.valueOf(EOL));

        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            if (row.isEmpty()) {
                throw new IllegalArgumentException("Empty row detected in compressed string");
            }

            StringBuilder decompressedRow = new StringBuilder();
            int j = 0;

            while (j < row.length()) {
                if (j >= row.length()) {
                    throw new IllegalArgumentException("Unexpected end of row at position " + j);
                }

                char c = row.charAt(j);
                j++;

                StringBuilder number = new StringBuilder();
                while (j < row.length() && Character.isDigit(row.charAt(j))) {
                    number.append(row.charAt(j));
                    j++;
                }

                if (number.length() > 0) {
                    try {
                        int count = Integer.parseInt(number.toString());
                        if (count < 0) {
                            throw new IllegalArgumentException("Negative count detected: " + count);
                        }
                        for (int k = 0; k < count; k++) {
                            decompressedRow.append(c);
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid number format in compressed string: '" + number + "' at row " + i, e);
                    }
                } else {
                    decompressedRow.append(c);
                }
            }

            decompressed.append(decompressedRow);
            if (i < rows.length - 1) {
                decompressed.append(EOL);
            }
        }

        return decompressed.toString();
    }
}