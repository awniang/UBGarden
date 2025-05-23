package fr.ubx.poo.ubgarden.game.launcher;


import static fr.ubx.poo.ubgarden.game.launcher.MapEntity.*;

public class MapLevelDefaultStart extends MapLevel {


    private final static int width = 18;
    private final static int height = 8;
        private final MapEntity[][] level1 = {
                {Grass, Grass, Grass, Grass, Grass, Flowers, Grass, Insecticide, Grass, Grass, Grass, PoisonedApple, Grass, Grass, Grass, Grass, Grass, DoorNextClosed},
                {Grass, Gardener, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Tree, Grass, Flowers, Tree, Tree, Grass, Grass, Grass, Grass},
                {Grass, Grass, Grass, Grass, Apple, Grass, Flowers, Grass, Grass, Grass, Carrots, Carrots, Carrots, Tree, Grass, Grass, Grass, Hedgehog},
                {Grass, Grass, Grass, Grass, Insecticide, Grass, Flowers, Grass, Grass, Carrots, Carrots, Carrots, Grass, Tree, Land, Grass, Grass, Grass},
                {Grass, Tree, Grass, Tree, Grass, Land, Grass, Grass, NestHornet, Grass, Flowers, Grass, Grass, Tree, Grass, Grass, Apple, Grass},
                {Grass, Tree, Tree, Tree, PoisonedApple, Grass, Grass, Grass, Grass, Grass, Grass, NestWasp, Grass, Grass, Grass, Grass, Grass, Grass},
                {Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Carrots, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass},
                {Grass, Tree, Grass, Tree, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass}
        };

    public MapLevelDefaultStart() {
        super(width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                set(i, j, level1[j][i]);
    }


}
