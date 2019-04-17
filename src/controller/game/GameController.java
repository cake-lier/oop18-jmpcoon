package controller.game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Queue;

import org.apache.commons.lang3.tuple.Pair;

import model.entities.UnmodifiableEntity;
import model.world.CollisionType;

/**
 * a controller for the game playing.
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
     * @param saveFileIndex the index of {@link SaveFile} referring to the choosen save file.
     * @throws IOException if an I/O error occurs
     * @throws FileNotFoundException if the {@link URL} passed does not reference an existent file
     */
    void saveGame(int saveFileIndex) throws FileNotFoundException, IOException;

    /**
     * Load a previously saved game.
     * @param saveFileIndex the index of {@link SaveFile} referring to the choosen save file.
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the file referenced by the {@link URL} isn't compatible with this application
     */
    void loadGame(int saveFileIndex) throws IOException, IllegalArgumentException;

    /**
     * Stops definitely the current game.
     */
    void stopGame();

    /**
     * @return the current score of the game
     */
    int getCurrentScore();

    /**
     * @return the number of {@link model.entities.Player}'s lives.
     */
    int getPlayerLives();

    /**
     * gives to the {@link World} in which the game is playing the input received from the view.
     * @param input the input received
     * @return True if the command was successful and the {@link model.world.World} has accepted it, false otherwise.
     */
    boolean processInput(InputType input);

    /**
     * 
     * @return the dimensions (width and height) of the {@link model.world.World} in which the game is playing
     */
    Pair<Double, Double> getWorldDimensions();

    /**
     * 
     * @return alive entities.
     */
    Collection<UnmodifiableEntity> getAliveEntities();

    /**
     * 
     * @return dead entities.
     */
    Collection<UnmodifiableEntity> getDeadEntities();

    /**
     * 
     * @return A {@link Queue} of the last {@link EventType} that happened.
     */
    Queue<CollisionType> getCurrentEvents();
}
