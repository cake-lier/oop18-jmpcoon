package controller;

import java.util.Collection;

import model.Entity;

/**
 * a controller for the game playing.
 */
public interface GameController {

    // TODO: control if this methods are necessary, comment them
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
     * @return the entities contained in the {@link World}
     */
    Collection<Entity> getEntities();

}
