package controller;

/**
 * Interface modeling the controller for the application: it should correctly start the
 * application, stop it and start the game when asked to do so. A start method is missing
 * because its job is delegated to the main method.
 */
public interface AppController {
    /**
     * Exits the application.
     */
    void exitApp();
    /**
     * Starts the game controller. This is done when the user specifies that she wants to
     * start a new game, so it has to be correctly started.
     */
    void startGameController();
}
