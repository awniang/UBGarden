package fr.ubx.poo.ubgarden.game.launcher;

import fr.ubx.poo.ubgarden.game.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GameLauncher {

    private GameLauncher() {
    }

    public static GameLauncher getInstance() {
        return LoadSingleton.INSTANCE;
    }

    private int integerProperty(Properties properties, String name, int defaultValue) {
        return Integer.parseInt(properties.getProperty(name, Integer.toString(defaultValue)));
    }

    private boolean booleanProperty(Properties properties, String name, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(name, Boolean.toString(defaultValue)));
    }

    private Configuration getConfiguration(Properties properties) {
        int waspMoveFrequency = integerProperty(properties, "waspMoveFrequency", 2);
        int hornetMoveFrequency = integerProperty(properties, "hornetMoveFrequency", 1);
        int gardenerEnergy = integerProperty(properties, "gardenerEnergy", 100);
        int energyBoost = integerProperty(properties, "energyBoost", 50);
        long energyRecoverDuration = integerProperty(properties, "energyRecoverDuration", 1_000);
        long diseaseDuration = integerProperty(properties, "diseaseDuration", 5_000);

        return new Configuration(gardenerEnergy, energyBoost, energyRecoverDuration, diseaseDuration, waspMoveFrequency, hornetMoveFrequency);
    }

    public Game load(File file) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            properties.load(in);
        }

        boolean isCompressed = booleanProperty(properties, "compression", false);
        MapRepo mapRepo = isCompressed ? new MapRepoCompressedString() : new MapRepoString();


        int levels = Integer.parseInt(properties.getProperty("levels", "1"));

        Configuration configuration = getConfiguration(properties);
        World world = new World(levels);

        Position gardenerPosition = null;
        Game game = null;

        // charge chaque niveau
        for (int i = 1; i <= levels; i++) {
            String levelKey = "level" + i;
            String levelString = properties.getProperty(levelKey);
            if (levelString == null) {
                throw new IOException("No " + levelKey + " property found in file");
            }

            MapLevel mapLevel = mapRepo.load(levelString);

            if (i == 1) {
                gardenerPosition = mapLevel.getGardenerPosition();
                if (gardenerPosition == null)
                    throw new RuntimeException("Gardener not found in level1"); // il doit obligatoirement y avoir un jardinier
                game = new Game(world, configuration, gardenerPosition);
            }

            Map level = new Level(game, i, mapLevel);
            world.put(i, level);
        }

        if (game == null)
            throw new RuntimeException("No levels found");

        return game;
    }

    public Game load() {
        Properties emptyConfig = new Properties();
        MapLevel mapLevel = new MapLevelDefaultStart();
        Position gardenerPosition = mapLevel.getGardenerPosition();

        if (gardenerPosition == null)
            throw new RuntimeException("Gardener not found");

        Configuration configuration = getConfiguration(emptyConfig);
        World world = new World(1);
        Game game = new Game(world, configuration, gardenerPosition);
        Map level = new Level(game, 1, mapLevel);
        world.put(1, level);
        return game;
    }

    private static class LoadSingleton {
        static final GameLauncher INSTANCE = new GameLauncher();
    }
}