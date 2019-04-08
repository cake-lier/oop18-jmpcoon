package view.menus;

/**
 * Represents the menu which will be launched during the game. It needs to draw all its elements, show and hide itself.
 *
 */
public interface GameMenu {
    /**
     * Draws the menu elements.
     */
    void draw();

    /**
     * Displays the menu.
     */
    void show();

    /**
     * Hides the menu.
     */
    void hide();
}
