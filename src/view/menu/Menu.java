package view.menu;

import java.io.IOException;

import javafx.stage.Stage;

/**
 * The initial menu, it has to draw, show and hide itself.
 */
public interface Menu {
    /**
     * Draws the menu of the game.
     * @param stage The stage in which to draw the menu.
     * @throws IOException If IO error occurs.
     */
    void drawMenu(Stage stage) throws IOException;
    /**
     * Shows the previously drawn menu.
     */
    void showMenu();
    /**
     * Hides the currently shown menu.
     */
    void hideMenu();
}
