package view;

import java.io.IOException;

import javafx.stage.Stage;

/**
 * Interface modeling the view of the game. It should be able to draw the entities of the
 * game and the menu of the game.
 */
public interface GameView {
    /**
     * Function drawing the menu of the game.
     * @param stage The current stage.
     * @throws IOException If IO error occurs.
     */
    void drawMenu(Stage stage) throws IOException;
}
