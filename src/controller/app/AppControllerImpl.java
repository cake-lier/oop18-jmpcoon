package controller.app;

import controller.game.GameController;
import controller.game.GameControllerImpl;

/**
 * Class implementation of the interface {@link AppController}.
 */
public final class AppControllerImpl implements AppController {
    private final GameController gameController;
    /**
     * Acquires a new {@link GameController} to call when the user wants to begin a new
     * game.
     * @param gameController The controller responsible for the game.
     */
    public AppControllerImpl(final GameController gameController) {
        this.gameController = new GameControllerImpl();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void exitApp() {
        System.exit(0);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void startGameController() {
        this.gameController.startGame();
    }
}
