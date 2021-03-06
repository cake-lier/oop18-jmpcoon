package it.unibo.jmpcoon.view.game;

import java.util.Collection;

import com.google.common.base.Optional;

import it.unibo.jmpcoon.controller.game.InputType;

/**
 * Visual component of the game.
 */
public interface GameView {
    /**
     * Updates the game scene.
     */
    void update();

    /**
     * Initializes the game scene and starts the game by calling the {@link it.unibo.jmpcoon.controller.game.GameController}
     * appropriately. If a file is specified, it means the game should be started by loading data from the specified file,
     * otherwise a new game should be started.
     * @param saveFileIndex the index of the file with the saved game from which starting the game, if present
     */
    void initialize(Optional<Integer> saveFileIndex);

    /**
     * Shows game over screen.
     */
    void showGameOver();

    /**
     * Shows win screen.
     */
    void showPlayerWin();

    /**
     * Performs cleaning of the view of this game, intended as a component, putting it into a state like before this object was
     * created. It should be used only after putting it in an "unusable" state, such as removing it, deleting it, etc.
     */
    void clean();

    /**
     * Returns a {@link Collection} of the inputs given by the user after the last update.
     * @return a collection of inputs
     */
    Collection<InputType> getInputs();
}
