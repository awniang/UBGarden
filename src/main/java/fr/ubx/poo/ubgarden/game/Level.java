package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.bonus.Carrots;
import fr.ubx.poo.ubgarden.game.go.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.go.bonus.Insecticide;
import fr.ubx.poo.ubgarden.game.go.bonus.PoisonedApple;
import fr.ubx.poo.ubgarden.game.go.decor.*;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Grass;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Land;
import fr.ubx.poo.ubgarden.game.go.decor.Hedgehog;
import fr.ubx.poo.ubgarden.game.go.personage.Hornet;
import fr.ubx.poo.ubgarden.game.go.personage.Insect;
import fr.ubx.poo.ubgarden.game.go.personage.Wasp;
import fr.ubx.poo.ubgarden.game.launcher.MapEntity;
import fr.ubx.poo.ubgarden.game.launcher.MapLevel;

import java.util.Collection;
import java.util.HashMap;

public class Level implements Map {

    private final int level;
    private final int width;

    private final int height;

    private final java.util.Map<Position, Decor> decors = new HashMap<>();
    private final java.util.Map<Position, Carrots> carrots = new HashMap<>();

    public Level(Game game, int level, MapLevel entities) {
        this.level = level;
        this.width = entities.width();
        this.height = entities.height();

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                Position position = new Position(level, i, j);
                MapEntity mapEntity = entities.get(i, j);
                switch (mapEntity) {
                    case Grass:
                        decors.put(position, new Grass(position));
                        break;
                    case Tree:
                        decors.put(position, new Tree(position));
                        break;
                    case Flowers:
                        decors.put(position, new Flowers(position));
                        break;
                    case Land:
                        decors.put(position, new Land(position));
                        break;
                    case Carrots: {
                        Decor land = new Land(position);
                        Carrots carrot = new Carrots(position, land);
                        land.setBonus(carrot);
                        decors.put(position, land);
                        carrots.put(position,carrot);
                        break;
                    }
                    case DoorNextClosed,DoorNextOpened:
                        decors.put(position, new Door(position,level +1));
                        break;
                    case DoorPrevOpened:
                        Door prevDoor = new Door(position, level - 1);
                        prevDoor.openDoor();
                        decors.put(position, prevDoor);
                        break;
                    case Insecticide: {
                        Decor grass = new Grass(position);
                        grass.setBonus(new Insecticide(position, grass));
                        decors.put(position, grass);
                        break;
                    }
                    case PoisonedApple:{
                        Decor grass = new Grass(position);
                        grass.setBonus(new PoisonedApple(position, grass));
                        decors.put(position, grass);
                        break;
                    }
                    case Apple: {
                        Decor grass = new Grass(position);
                        grass.setBonus(new EnergyBoost(position, grass));
                        decors.put(position, grass);
                        break;
                    }
                    case NestHornet:
                        decors.put(position, new NestHornet(game,position));
                        break;
                    case NestWasp:
                        decors.put(position, new NestWasp(game,position));
                        break;
                    case Hedgehog:
                        decors.put(position, new Hedgehog(position));
                        break;
                    case Hornet: {
                        Decor grass = new Grass(position);
                        decors.put(position, grass);
                        Insect hornet = new Hornet(game, position);
                        game.getInsects().add(hornet);
                        break;
                    }
                    case Wasp: {
                        Decor grass = new Grass(position);
                        decors.put(position, grass);
                        Insect wasp = new Wasp(game, position);
                        game.getInsects().add(wasp);
                        break;
                    }
                    default:
                        throw new RuntimeException("EntityCode " + mapEntity.name() + " not processed");
                }
            }
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    public Decor get(Position position) {
        return decors.get(position);
    }
    public Carrots getCarrot(Position position) {
        return carrots.get(position);
    }


    public Collection<Decor> values() {
        return decors.values();
    }
    public Collection<Carrots> valuesCarrots() {
        return carrots.values();
    }



    @Override
    public boolean inside(Position position) {
        return position.x()<width && position.y()<height && position.x()>=0 && position.y()>=0;
    }

    public boolean allCarrotsCollected() {
        for (Carrots carrot : carrots.values()) {
            if (!carrot.isCollected()) {
                return false;
            }
        }
        return true;
    }

    public void openDoors() {
        for (Decor decor : decors.values()) {
            decor.openDoor();
        }
    }

}
