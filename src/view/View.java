package view;

/**
 * It represents the view of the game, the class which will be called by the controller
 * for requesting to display the visual part of the application. It can show the initial
 * menu or the game itself.
 */
public interface View {
    /**
     * Displays the initial menu.
     */
    void displayMenu();
    /**
     * Displays the game.
     */
    void displayGame();
}
