package model.world;

import java.util.Collection;

import model.MovementType;
import model.entities.Entity;
import model.entities.EntityProperties;
import org.apache.commons.lang3.tuple.Pair;

/**
 * The World in which the game takes place. It creates, initializes, destroys and passes around all the entities which populate
 * the game world.
 */
public interface World {

    /**
     * Gets the dimensions of the world.
     * @return A pair, the dimensions of the game world in meters.
     */
    Pair<Double, Double> getDimensions();

    /**
     * Initializes the world with the specified level, so it populates it with the entities which should be inside this level.
     * @param entities The {@link Collection} of {@link EntityProperties} which contains the properties of each of the entities
     * that should be put inside this {@link World}.
     */
    void initLevel(Collection<EntityProperties> entities);

    /**
     * Updates the current state of the {@link World} by letting an interval of time pass.
     */
    void update();

    /**
     * It moves the player inside the world making her do one of the movements it's allowed to make, as specified in the 
     * {@link MovementType} enum.
     * @param movement The type of movement the player should do.
     */
    void movePlayer(MovementType movement);

    /**
     * Checks whether the game has ended and the {@link Player} has lost or not.
     * @return True if the game has ended and the {@link Player} has lost, false otherwise.
     */
    boolean isGameOver();

    /**
     * Checks whether the game has ended and the {@link Player} has won or not.
     * @return True if the game has ended and the {@link Player} has won, false otherwise.
     */
    boolean hasPlayerWon();

    /**
     * Produces all the entities which populate this world, divided by group depending of their type, as specified in the
     * {@link EntityType} enum.
     * @return An {@link Map} from the {@link Entity} class to the collection of {@link Entity}s of this type.
     */
    Collection<Entity> getEntities();
}
