package model.world;

import java.io.Serializable;
import java.util.Collection;
import java.util.Queue;

import model.entities.MovementType;
import model.entities.UnmodifiableEntity;
import model.entities.EntityProperties;
import org.apache.commons.lang3.tuple.Pair;

/**
 * The World in which the game takes place. It creates, initializes, destroys and passes around all the entities which populate
 * the game world.
 */
public interface UpdatableWorld extends Serializable {
    /**
     * Gets the dimensions of the world.
     * @return a pair containing the dimensions of the game world in meters
     */
    Pair<Double, Double> getDimensions();

    /**
     * Initializes the world with the specified level, so it populates it with the entities which should be inside this level.
     * @param entities the {@link Collection} of {@link EntityProperties} which contains the properties of each of the entities
     * that should be put inside this {@link World}
     */
    void initLevel(Collection<EntityProperties> entities);

    /**
     * Updates the current state of the {@link World} by letting an interval of time pass.
     */
    void update();

    /**
     * It moves the player inside the world making her do one of the movements it's allowed to make, as specified in the 
     * {@link MovementType} enum.
     * @param movement the type of movement the player should do
     * @return true if the movement was authorized by the world and executed by the player, false otherwise
     */
    boolean movePlayer(MovementType movement);

    /**
     * Checks whether the game has ended and the {@link model.entities.Player} has lost or not.
     * @return true if the game has ended and the {@link model.entities.Player} has lost, false otherwise
     */
    boolean isGameOver();

    /**
     * Checks whether the game has ended and the {@link model.entities.Player} has won or not.
     * @return true if the game has ended and the {@link model.entities.Player} has won, false otherwise
     */
    boolean hasPlayerWon();

    /**
     * Produces all the entities which populate this world which are still alive since the last {@link #update()} method call.
     * @return a {@link Collection} of the alive {@link Entity}s
     */
    Collection<UnmodifiableEntity> getAliveEntities();

    /**
     * Produces all the entities which have died since the last {@link #update()} method call.
     * @return a {@link Collection} of the {@link Entity}s which have died across last {@link World} update
     */
    Collection<UnmodifiableEntity> getDeadEntities();

    /**
     * Returns a queue of all events that happened during the last {@link #update()}. 
     * @return a {@link Queue} which contains the {@link EventType}s that happened during the last update
     */
    Queue<CollisionEvent> getCurrentEvents();

    /**
     * Calculates the score currently totaled by the user.
     * @return the current game score
     */
    int getCurrentScore();

    /**
     * Gets the current number of lives which the {@link model.entities.Player} has.
     * @return the count of the {@link model.entities.Player}'s lives
     */
    int getPlayerLives();
}
