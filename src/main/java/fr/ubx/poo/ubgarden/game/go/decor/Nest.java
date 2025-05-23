package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.GameEngine;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.personage.Insect;

public abstract class Nest extends Decor{
    private final Game game;
    private final Timer spawnTimer; // fréquence de génération d'insecte

    public Game getGame() {
        return game;
    }

    public Nest(Game game, Position position,int spawnFrequency) {
        super(position);
        this.game = game;
        this.spawnTimer = new Timer(spawnFrequency);
        this.spawnTimer.start();
        setNest(true);
    }

    public void update(long now) {
        spawnTimer.update(now);
        if (!spawnTimer.isRunning()) {
            Insect newInsect = createInsect();
            game.addInsect(newInsect);
            spawnTimer.start();
        }
    }

    public abstract String getInsectType();
    public abstract Insect createInsect();
    public abstract int insecticideCount();
}
