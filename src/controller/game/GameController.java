package controller.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.lang3.tuple.Pair;

import model.entities.UnmodifiableEntity;
import model.world.EventType;

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
     * @param saveFile the {@link File} where the information about the game to be loaded will be saved
     * @throws IOException if an I/O error occurs
     * @throws FileNotFoundException if the {@link URL} passed does not reference an existent file
     */
    void saveGame(File saveFile) throws FileNotFoundException, IOException;

    /**
     * Load a previously saved game.
     * @param saveFile the {@link File} where the information about the game to be loaded is saved
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the file referenced by the {@link URL} isn't compatible with this application
     */
    void loadGame(File saveFile) throws IOException;

    /**
     * Stops definitely the current game.
     */
    void stopGame();

    /**
     * @return the current score of the game
     */
    int getCurrentScore();

    /**
     * gives to the {@link model.world.World} in which the game is playing the input received from the view.
     * @param input the input received
     */
    void processInput(InputType input);

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
     * @param type The type of collision.
     */
    void notifyEvent(EventType type);
}
