package view.menu;

/**
 * The initial menu, it has to draw, show and hide itself.
 */
public interface Menu {
    /**
     * Draws the menu of the game.
     */
    void drawMenu();
    /**
     * Shows the previously drawn menu.
     */
    void showMenu();
    /**
     * Hides the currently shown menu.
     */
    void hideMenu();
}
