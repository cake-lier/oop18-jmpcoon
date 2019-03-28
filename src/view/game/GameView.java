package view.game;

/**
 * Visual component of the game.
 */
public interface GameView {

    /**
     * Updates the game scene.
     */
    void update();

    /**
     * Initializes the game scene.
     */
    void init();

    /**
     * Shows game over screen.
     */
    void showGameOver();

    /**
     * Shows win screen.
     */
    void showPlayerWin();
}
