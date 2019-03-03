package controller.game;

import java.util.Collection;

import controller.InputType;
import view.game.DrawableEntity;

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
     * @return the entities of the {@link World}, in such a way that they can be drawn
     */
    Collection<DrawableEntity> getDrawableEntities();

}
