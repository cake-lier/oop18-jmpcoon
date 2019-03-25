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
     * @param entities The level entities.
     */
    void initLevel(Collection<EntityProperties> entities);

    /**
     * a method to move the player inside the {@link World}.
     * @param movement the type of movement the player should execute
     */
    void movePlayer(MovementType movement);

    /**
     * @return whether the game has ended or not
     */
    boolean isGameOver();

    void update();

    /**
     * @return the entities populating the {@link World}
     */
    Collection<Entity> getEntities();

    boolean hasPlayerWon();

}
