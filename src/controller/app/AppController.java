package controller.app;

/**
 * Interface modeling the controller for the application: it should correctly manage the
 * application once started, so it should stop it and start the game when asked to do so.
 */
public interface AppController {
    /**
     * Exits the application.
     */
    void exitApp();
    /**
     * Starts the game controller. This is done when the user specifies that she wants to
     * start a new game.
     */
    void startGameController();
}
