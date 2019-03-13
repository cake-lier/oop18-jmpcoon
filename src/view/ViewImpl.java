package view;

import controller.app.AppController;
import controller.app.AppControllerImpl;
import controller.game.GameController;
import controller.game.GameControllerImpl;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import view.game.GameViewImpl;
import view.menu.Menu;
import view.menu.MenuImpl;

/**
 * The class implementation of {@link View}.
 */
public final class ViewImpl implements View {
    private static final String TITLE = "Jumping Raccoon Adventures";
    private static final int HEIGHT_RATIO = 9;
    private static final int WIDTH_RATIO = 16;

    private final AppController appController;
    private final GameController gameController;
    private final Stage stage;
    /**
     * Acquires the stage in which to draw all the visual elements of this application.
     * Constructs the controller of this application as a whole, as an
     * {@link AppController} and a {@link GameController}, so as to let the Menu and the
     * GameView to access it and make available its functionalities to the user.
     * @param stage The stage in which to draw all the visual elements.
     */
    public ViewImpl(final Stage stage) {
        this.appController = new AppControllerImpl(this.gameController);
        this.stage = stage;
        this.stage.setTitle(TITLE);
        this.setScreenSize(this.stage);
        this.displayMenu();
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
     * {@inheritDoc}
     */
    @Override
    public void displayMenu() {
        final Menu menu = new MenuImpl(this.appController, this.stage);
        menu.drawMenu();
        menu.showMenu();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void displayGame() {
        new GameViewImpl(this.appController, this.stage);
    }
}
