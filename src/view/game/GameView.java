package view.game;

/**
 * Visual component of the game.
 */
public interface GameView {

    /**
     * Uses all current entities stored in {@link GameController} to update the game scene.
     */
    void update();

    /**
     * Shows game over screen.
     */
    void showGameOver();

    /**
     * Shows win screen.
     */
    void showPlayerWin();

}
