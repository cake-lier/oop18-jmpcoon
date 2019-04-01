package view;

import controller.app.AppController;
import controller.app.AppControllerImpl;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import view.game.GameView;
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

    private final AppController controller;
    private final Stage stage;

    /**
     * Acquires the {@link Stage} in which to draw all the visual elements of this
     * application and it initializes it appropriately. Constructs the controller of this
     * application with this view, so as to let it call this view when the user instructs
     * it to perform some operation, then starts the application via the
     * {@link AppController}.
     * @param stage The {@link Stage} in which to draw all visual elements.
     */
    public ViewImpl(final Stage stage) {
        this.controller = new AppControllerImpl(this);
        this.stage = stage;
        this.stage.setTitle(TITLE);
        this.setScreenSize(this.stage);
        this.controller.startApp();
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
        stage.centerOnScreen();
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
        final Menu menu = new MenuImpl(this.controller, this.stage);
        menu.drawMenu();
        menu.showMenu();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayGame() {
        final GameView gameView = new GameViewImpl(this.controller, this.stage);
        gameView.init();
    }
}
