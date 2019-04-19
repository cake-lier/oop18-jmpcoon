package view;

import com.google.common.base.Optional;

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
     * Displays the game. The game to be displayed depends on if it is a new game or a game previously saved and now loaded.
     * This is decided by the {@link Optional} passed. If not present, it will be displayed a new game, otherwise it will be
     * loaded the game from the file with the associated index (the association is known by the {@link AppController}) and 
     * then displayed.
     * @param saveFileIndex the index of the file from which the game will be loaded, if present
     */
    void displayGame(Optional<Integer> saveFileIndex);
}
