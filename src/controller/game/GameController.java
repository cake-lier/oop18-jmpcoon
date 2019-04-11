package controller.game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import model.entities.Entity;
import model.entities.Player;

import org.apache.commons.lang3.tuple.Pair;


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
     * @param saveFileUrl the {@link URL} of the file where the information about the game to be loaded will be saved
     * @throws IOException 
     * @throws FileNotFoundException if the {@link URL} passed does not reference an existent file
     */
    void saveGame(URL saveFileUrl) throws FileNotFoundException, IOException;

    /**
     * Load a previously saved game.
     * @param saveFileUrl the {@link URL} of the file where the information about the game to be loaded is saved
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the file referenced by the {@link URL} isn't compatible with this application
     */
    void loadGame(URL saveFileUrl) throws IOException;

    /**
     * Stops definitely the current game.
     */
    void stopGame();

    /**
     * @return the current score of the game
     */
    int getCurrentScore();

    /**
     * @return the number of {@link Player}'s lives.
     */
    int getPlayerLives();

    /**
     * gives to the {@link World} in which the game is playing the input received from the view.
     * @param input the input received
     */
    void processInput(InputType input);

    /**
     * 
     * @return the dimensions (width and height) of the {@link World} in which the game is playing
     */
    Pair<Double, Double> getWorldDimensions();

    /**
     * 
     * @return alive entities.
     */
    Collection<Entity> getAliveEntities();

    /**
     * 
     * @return dead entities.
     */
    Collection<Entity> getDeadEntities();

}
