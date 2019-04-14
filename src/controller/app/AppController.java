package controller.app;

import java.io.File;
import java.util.Optional;

/**
 * Interface modeling the controller for the application: it should correctly start the application and the manage the application
 * once started, so it should stop it and start the game when asked to do so.
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
     * Starts the game. It can receive a {@link String} which represents a file from which to load the game to play or not. In the
     * latter case, it will start a new game.
     * @param saveFile The URL of the file from which to load the game, if present.
     */
    void startGame(Optional<File> saveFile);
}
