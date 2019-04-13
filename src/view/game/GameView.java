package view.game;

import java.io.File;
import java.util.Optional;

/**
 * Visual component of the game.
 */
public interface GameView {

    /**
     * Updates the game scene.
     */
    void update();

    /**
     * Initializes the game scene and starts the game by calling the {@link controller.game.GameController} appropriately. If a
     * file is specified, it means the game should started by loading data from the specified file, otherwise a new game should
     * start.
     * @param saveFile The file with the saved game from which starting the game, if present.
     */
    void init(Optional<File> saveFile);

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
    void cleanView();
}
