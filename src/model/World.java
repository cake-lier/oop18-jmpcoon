package model;

import java.util.Collection;

import utils.Pair;

/**
 * interface representing the World in which the game takes place.
 */
public interface World {

    /**
     * @return the dimensions of the world
     */
    Pair<Double, Double> getDimensions();

    /**
     * initialize the desired level.
     */
    // TODO: what should be passed to this method? int, enum, level object?
    void initLevel();

    /**
     * a method to move the player inside the {@link World}.
     * @param movement the type of movement the player should execute
     */
    void movePlayer(MovementType movement);

    /**
     * @return whether the game has ended or not
     */
    boolean isGameOver();

    /**
     * @return the entities populating the {@link World}
     */
    Collection<Entity> getEntities();

}
