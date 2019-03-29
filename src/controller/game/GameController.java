package controller.game;

import java.util.Collection;

import model.entities.Entity;
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
     */
    void saveGame();

    /**
     * Stops definitely the current game.
     */
    void stopGame();

    /**
     * @return the current score of the game
     */
    int getCurrentScore();

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
