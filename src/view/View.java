package view;

import java.net.URL;
import java.util.Optional;

/**
 * It represents the view of the game, the class which will be called by the controller for requesting to display the visual part
 * of the application. It can show the initial menu or the game itself.
 */
public interface View {
    /**
     * Displays the initial menu.
     */
    void displayMenu();

    /**
     * Displays the game. The game to be displayed depends on if it is a new game or a game saved and loaded. This is decided by
     * the {@link Optional} {@link URL} passed. If not present, it will be displayed a new game, otherwise it will be loaded the
     * game from the associated file and then displayed.
     * @param saveFile The file from which starting the game, if present.
     */
    void displayGame(Optional<URL> saveFile);
}
