package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import view.GameView;
import view.GameViewImpl;

/**
 * Class implementation of the interface {@link AppController}.
 */
public final class AppControllerImpl extends Application implements AppController {
    private final String title = "Jumping Raccoon Adventures";

    private GameView gameView;
    private GameController gameController;

    /**
     * Main method which starts the application.
     * @param args Unused.
     */
    public static void main(final String... args) {
        launch();
    }
    /**
     * The method used by JavaFX to start the application.
     */
    @Override
    public void start(final Stage stage) throws Exception {
        this.gameController = new GameControllerImpl();
        this.gameView = new GameViewImpl();
        stage.setTitle(title);
        this.gameView.drawMenu(stage);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void exitApp() {
        Platform.exit();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void startGameController() {
        this.gameController.startGame();
    }
}
