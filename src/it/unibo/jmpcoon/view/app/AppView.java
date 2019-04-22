package it.unibo.jmpcoon.view.app;

import com.google.common.base.Optional;

/**
 * It represents the view of this application, the component to which refer to for requesting to display the visual part
 * of the application. It can show the initial menu or the game itself.
 */
public interface AppView {
    /**
     * Displays the initial menu.
     */
    void displayMenu();

    /**
     * Displays the game. The game to be displayed depends on if it is a new game or a game previously saved and now loaded.
     * This is decided by the {@link Optional} passed. If not present, it will be displayed a new game, otherwise it will be
     * loaded the game from the file with the associated index (the association is known by the
     * {@link it.unibo.jmpcoon.controller.app.AppController}) and then displayed.
     * @param saveFileIndex the index of the file from which the game will be loaded, if present
     */
    void displayGame(Optional<Integer> saveFileIndex);
}
