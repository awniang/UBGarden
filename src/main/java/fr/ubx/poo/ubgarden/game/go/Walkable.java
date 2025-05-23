package fr.ubx.poo.ubgarden.game.go;


import fr.ubx.poo.ubgarden.game.go.personage.Character;
public interface Walkable {

    /**
     * Checks whether the given {@link Character} can walk on this object.
     *
     * @param character the character attempting to walk
     * @return true if the character can walk on it, false otherwise
     */
    boolean walkableBy(Character character);

    /**
     * Returns the amount of energy consumed when walking over this object.
     *
     * @return the energy cost of walking, defaults to 0
     */
    default int energyConsumptionWalk() {
        return 0;
    }
}
