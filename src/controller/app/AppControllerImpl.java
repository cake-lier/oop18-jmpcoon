package controller.app;

import controller.game.GameController;
import controller.game.GameControllerImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import view.menu.Menu;
import view.menu.MenuImpl;

/**
 * Class implementation of the interface {@link AppController}.
 */
public final class AppControllerImpl extends Application implements AppController {
    private static final String TITLE = "Jumping Raccoon Adventures";
    private static final int HEIGHT_RATIO = 9;
    private static final int WIDTH_RATIO = 16;

    private final GameController gameController;
    /**
     * Creates a new {@link GameController} to call when the user wants to begin a new game.
     */
    public AppControllerImpl() {
        super();
        this.gameController = new GameControllerImpl();
    }
    /**
     * Main method which starts the application by creating an instance of the controller
     * to manage it and then starting the game.
     * @param args Unused.
     */
    public static void main(final String... args) {
        new AppControllerImpl().startApp();
    }
    /**
     * Sets the stage size to the appropriate values, so as to make it always the
     * biggest possible and with an aspect ratio of 16:9. It is also unresizable, so
     * the ratio cannot be changed in any way.
     * @param stage The stage being used.
     */
    private void setScreenSize(final Stage stage) {
        final Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        final double unitaryHeight = primaryScreenBounds.getHeight() / HEIGHT_RATIO;
        final double unitaryWidth = primaryScreenBounds.getWidth() / WIDTH_RATIO;
        if (primaryScreenBounds.getWidth() < unitaryHeight * WIDTH_RATIO) {
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight(unitaryWidth * HEIGHT_RATIO);
        } else if (primaryScreenBounds.getHeight() < unitaryWidth * HEIGHT_RATIO) {
            stage.setHeight(primaryScreenBounds.getHeight());
            stage.setWidth(unitaryHeight * WIDTH_RATIO);
        }
        stage.setResizable(false);
    }
    /**
     * The method used by JavaFX to start the application. Sets title and size of the stage,
     * and then draws the menu on it.
     */
    @Override
    public void start(final Stage stage) throws Exception {
        stage.setTitle(TITLE);
        this.setScreenSize(stage);
        final Menu menu = new MenuImpl(this);
        menu.drawMenu(stage);
        menu.showMenu();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void startApp() {
        launch();
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
