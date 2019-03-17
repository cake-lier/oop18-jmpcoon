package model;

import java.util.Collection;

import model.entities.Entity;
import model.entities.EntityProperties;
import org.apache.commons.lang3.tuple.Pair;

/**
 * The World in which the game takes place. It creates, initializes, destroys and passes
 * around all the entities which populate the game world.
 */
public interface World {
    /**
     * Gets the dimensions of the world.
     * @return A pair, the dimensions of the game world in meters.
     */
    Pair<Double, Double> getDimensions();
    /**
     * Initializes the world with the specified level, so it populates it with the entities
     * which should be inside this level.
     * @param entities 
     */
    void initLevel(Collection<EntityProperties> entities);
    /**
     * It moves the player inside the world making her do one of the movements it's allowed
     * to make, as specified in the {@link MovementType} enum.
     * @param movement The type of movement the player should do.
     */
    void movePlayer(MovementType movement);
    /**
     * Checks whether the game has ended or not.
     * @return True if the game has ended.
     */
    boolean isGameOver();
    /**
     * Produces all the entities which populate this world, divided by group depending
     * of their type, as specified in the {@link EntityType} enum.
     * @return An {@link Map} from the {@link Entity} class to the collection of
     * {@link Entity}s of this type.
     */
    Collection<Entity> getEntities();
}
