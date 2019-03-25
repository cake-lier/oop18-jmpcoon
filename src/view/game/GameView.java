package view.game;

<<<<<<< HEAD

public interface GameView {

    void update();

    void showGameOver();

    void showPlayerWin();    
=======
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
>>>>>>> feature-game-view

}
