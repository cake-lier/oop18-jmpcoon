package controller.game;

import java.util.Collection;

import model.Entity;
import utils.Pair;


/**
 * a controller for the game playing.
 */
public interface GameController {

    void startGame();

    void pauseGame();

    void saveGame();

    void stopGame();

    /**
     * gives to the {@link World} in which the game is playing the input received from the view.
     * @param input the input received
     */
    void processInput(InputType input);

    /**
     * @return the entities of the {@link World}, in such a way that they can be drawn
     */
    Collection<Entity> getEntities();

    /**
     * 
     * @return the dimensions (width and height) of the {@link World} in which the game is playing
     */
    Pair<Double, Double> getWorldDimensions();
}
