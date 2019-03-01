package view.menu;

import java.io.IOException;

import javafx.stage.Stage;

/**
 * The view of the menu, it has to draw, show and hide the initial menu.
 */
public interface MenuView {
    /**
     * Draws the menu of the game.
     * @param stage The stage in which to draw the menu.
     * @throws IOException If IO error occurs.
     */
    void drawMenu(Stage stage) throws IOException;
    /**
     * Shows the previously drawed menu.
     */
    void showMenu();
    /**
     * Hides the menu.
     */
    void hideMenu();
}
