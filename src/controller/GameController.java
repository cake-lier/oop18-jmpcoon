package controller;

import java.util.Collection;

import model.Entity;
import utils.Pair;

/**
 * a controller for the game playing.
 */
public interface GameController {

    /**
     * method to start the game for the first time, of after a pause.
     */
    void startGame();

    /**
     * method to pause the game currently playing.
     */
    void pauseGame();

    void saveGame();

    void stopGame();

    /**
     * gives to the {@link World} in which the game is playing the input received from the view.
     * @param input the input received
     */
    void processInput(InputType input);

    /**
     * @return the entities contained in the {@link World}
     */
    Collection<Entity> getEntities();

    /**
     * @return the dimensions (width and height) of the {@link World} in which the game is playing
     */
    Pair<Double, Double> getWorldDimensions();

}
