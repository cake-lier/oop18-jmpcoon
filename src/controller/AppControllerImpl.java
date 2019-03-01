package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import view.menu.MenuView;
import view.menu.MenuViewImpl;

/**
 * Class implementation of the interface {@link AppController}.
 */
public final class AppControllerImpl extends Application implements AppController {
    private final String title = "Jumping Raccoon Adventures";
    private final int heightRatio = 9;
    private final int widthRatio = 16;

    private final GameController gameController;
    /**
     * This constructor is private so as to not letting anyone create a second controller
     * for this application, because it will lead to unintended errors. It create a new
     * {@link GameController} to call when the user wants to begin a new game.
     */
    public AppControllerImpl() {
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
     * 
     * @param stage
     */
    private void setScreenSize(final Stage stage) {
        final Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        if (primaryScreenBounds.getWidth()
            < (primaryScreenBounds.getHeight() / heightRatio) * widthRatio) {
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight((primaryScreenBounds.getWidth() / widthRatio) * heightRatio);
        } else if (primaryScreenBounds.getHeight()
                   < (primaryScreenBounds.getWidth() / widthRatio) * heightRatio) {
            stage.setHeight(primaryScreenBounds.getHeight());
            stage.setWidth((primaryScreenBounds.getHeight() / heightRatio) * widthRatio);
        }
    }
    /**
     * The method used by JavaFX to start the application.
     */
    @Override
    public void start(final Stage stage) throws Exception {
        stage.setTitle(title);
        this.setScreenSize(stage);
        final MenuView menuView = new MenuViewImpl();
        menuView.drawMenu(stage);
        menuView.showMenu();
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
