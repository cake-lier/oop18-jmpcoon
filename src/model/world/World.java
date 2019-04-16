package model.world;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import model.entities.MovementType;
import model.entities.UnmodifiableEntity;
import model.entities.EntityProperties;
import org.apache.commons.lang3.tuple.Pair;

/**
 * The World in which the game takes place. It creates, initializes, destroys and passes around all the entities which populate
 * the game world.
 */
public interface World extends Serializable {

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
     * @return true if the movement was authorized by the world and executed by the player, false otherwise
     */
    boolean movePlayer(MovementType movement);

    /**
     * Checks whether the game has ended and the {@link model.entities.Player} has lost or not.
     * @return True if the game has ended and the {@link model.entities.Player} has lost, false otherwise.
     */
    boolean isGameOver();

    /**
     * Checks whether the game has ended and the {@link model.entities.Player} has won or not.
     * @return True if the game has ended and the {@link model.entities.Player} has won, false otherwise.
     */
    boolean hasPlayerWon();

    /**
     * Produces all the entities which populate this world which are still alive since the last {@link #update()} method call.
     * @return A {@link Collection} of the alive {@link Entity}s.
     */
    Collection<UnmodifiableEntity> getAliveEntities();

    /**
     * Produces all the entities which have died since the last {@link #update()} method call.
     * @return A {@link Collection} of the {@link Entity}s which have died across last {@link World} update.
     */
    Collection<UnmodifiableEntity> getDeadEntities();

    /**
     * 
     * @return A {@link Queue} of the last {@link EventType} that happened.
     */
    List<EventType> getRecentEvents();

    /**
     * Calculates the score currently totaled by the user.
     * @return The current game score.
     */
    int getCurrentScore();

    /**
     * Gets the current number of lives which the {@link model.entities.Player} has.
     * @return The count of the {@link model.entities.Player}'s lives.
     */
    int getPlayerLives();
}
