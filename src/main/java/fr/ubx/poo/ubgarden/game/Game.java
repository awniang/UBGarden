package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import fr.ubx.poo.ubgarden.game.go.personage.Hornet;
import fr.ubx.poo.ubgarden.game.go.personage.Insect;
import fr.ubx.poo.ubgarden.game.go.personage.Wasp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private final Configuration configuration;
    private final World world;
    private final Gardener gardener;
    private final List<Insect> insects;

    private boolean switchLevelRequested = false;
    private int switchLevel;

    public Game(World world, Configuration configuration, Position gardenerPosition) {
        this.configuration = configuration;
        this.world = world;
        this.gardener = new Gardener(this, gardenerPosition);
        this.insects = new ArrayList<>();
    }

    public Configuration configuration() {
        return configuration;
    }

    public Gardener getGardener() {
        return gardener;
    }

    public List<Insect> getInsects() {
        return insects;
    }

    public void addInsect(Insect insect) {
        insects.add(insect);
    }

    public void removeInsect(Insect insect) {
        insect.remove();
        insects.remove(insect);
    }

    public World world() {
        return world;
    }

    public boolean isSwitchLevelRequested() {
        return switchLevelRequested;
    }

    public int getSwitchLevel() {
        return switchLevel;
    }

    public void requestSwitchLevel(int level) {
        this.switchLevel = level;
        this.switchLevelRequested = true;

    }

    public void clearSwitchLevel() {
        switchLevelRequested = false;
    }
}