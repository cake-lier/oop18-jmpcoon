package controller.game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Queue;

import org.apache.commons.lang3.tuple.Pair;

import model.entities.UnmodifiableEntity;
import model.world.CollisionEvent;

/**
 * A controller for the game playing.
 */
public interface GameController {
    /**
     * Starts the game.
     */
    void startGame();

    /**
     * Pause the game so that it can be restarted later, or restart a game that is paused.
     */
    void togglePauseGame();

    /**
     * Saves the current game.
     * @param saveFileIndex the index of {@link SaveFile} referring to the chosen save file
     * @throws IOException if an I/O error occurs
     * @throws FileNotFoundException if the {@link URL} passed does not reference an existent file
     */
    void saveGame(int saveFileIndex) throws FileNotFoundException, IOException;

    /**
     * Loads a previously saved game.
     * @param saveFileIndex the index of {@link SaveFile} referring to the choosen save file
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the file referenced by the {@link URL} isn't compatible with this application
     */
    void loadGame(int saveFileIndex) throws IOException, IllegalArgumentException;

    /**
     * Stops definitely the current game.
     */
    void stopGame();

    /**
     * Returns the current score of the game.
     * @return the current score of the game
     */
    int getCurrentScore();

    /**
     * Returns the number of lives of the player.
     * @return the number of {@link model.entities.Player}'s lives
     */
    int getPlayerLives();

    /**
     * Gives to the {@link World} in which the game is playing the input received from the view.
     * @param givenInput the input received. Such input is propagated to the {@link World} until the view says differently using
     * the method {@link #stopInput(InputType)}
     * @return True if the command was successful and the {@link model.world.World} has accepted it, false otherwise
     */
    boolean processInput(InputType givenInput);

    /**
     * Stops the propagation of a previously given {@link InputType} to the {@link World}. 
     * @param stoppedInput the input whose propagation has to stop
     */
    void stopInput(InputType stoppedInput);

    /**
     * Returns the dimensions of the {@link model.world.World} in which the game is playing.
     * @return the dimensions (width and height) of the {@link model.world.World} in which the game is playing
     */
    Pair<Double, Double> getWorldDimensions();

    /**
     * Returns a {@link Collection} of the {@link model.entities.Entity} alive in the game.
     * @return the alive entities in the {@link World} game
     */
    Collection<UnmodifiableEntity> getAliveEntities();

    /**
     * Returns a {@link Collection} of the {@link model.entities.Entity} that recently died in the game.
     * @return the dead entities in the {@link World} game
     */
    Collection<UnmodifiableEntity> getDeadEntities();

    /**
     * Returns a {@link Queue} of {@link CollisionEvent} that recently happened in the game.
     * @return a {@link Queue} of the last {@link CollisionEvent} that happened.
     */
    Queue<CollisionEvent> getCurrentEvents();
}
