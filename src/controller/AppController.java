package controller;

/**
 * Interface modeling the controller for the application: it should correctly start the
 * application, stop it and start the game when asked to do so.
 */
public interface AppController {
    /**
     * Starts the application. It's invoked by the main method.
     */
    void startApp();
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
