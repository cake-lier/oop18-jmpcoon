package controller.app;

/**
 * Interface modeling the controller for the application: it should correctly start the
 * application and the manage the application once started, so it should stop it and start
 * the game when asked to do so.
 */
public interface AppController {
    /**
     * Starts the application.
     */
    void startApp();
    /**
     * Exits the application.
     */
    void exitApp();
    /**
     * Starts the game.
     */
    void startGame();
}
