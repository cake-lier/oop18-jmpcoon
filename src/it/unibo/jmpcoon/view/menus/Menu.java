package it.unibo.jmpcoon.view.menus;

/**
 * A general interface for a menu. It has to draw, show and hide itself.
 */
public interface Menu {
    /**
     * Draws the menu of the game. It should be invoked only one time, before showing the menu for the first time.
     */
    void draw();

    /**
     * Shows the previously drawn menu. It should be invoked, even multiple times, only after calling the {@link #draw()} method.
     * If the menu is currently shown, calling this method does nothing.
     */
    void show();

    /**
     * Hides the previously shown menu. It should be invoked, even multiple times, only after calling the {@link #draw()} method.
     * If the menu is currently hidden, this method does nothing.
     */
    void hide();
}
