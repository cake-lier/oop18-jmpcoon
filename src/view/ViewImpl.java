package view;

import java.net.URL;
import java.util.Optional;

import controller.app.AppController;
import controller.app.AppControllerImpl;
import javafx.geometry.Rectangle2D;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
    private static final String MENU_MUSIC = ClassLoader.getSystemResource("sounds/stillalive.mp3").toString();
    private static final String GAME_MUSIC = ClassLoader.getSystemResource("sounds/pixelland.mp3").toString();
    private static final int HEIGHT_RATIO = 9;
    private static final int WIDTH_RATIO = 16;
    private static final double INIT_VOLUME = 0.5;

    private final AppController controller;
    private final Stage stage;
    private MediaPlayer player;

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
        this.player = new MediaPlayer(new Media(MENU_MUSIC));
        this.player.setVolume(INIT_VOLUME);
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

    /*
     * Creates a new player for the audio track which source is passed and then sets the volume as the same one of the preceding
     * player, so as to maintain consistency between volume levels in tracks.
     */
    private void createNewTrack(final String source) {
        final MediaPlayer music = new MediaPlayer(new Media(source));
        music.setVolume(this.player.getVolume());
        music.setCycleCount(MediaPlayer.INDEFINITE);
        this.player = music;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayMenu() {
        this.createNewTrack(MENU_MUSIC);
        final Menu menu = new MenuImpl(this.controller, this.stage, this.player);
        this.setScreenSize(this.stage);
        menu.draw();
        menu.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayGame(final Optional<URL> saveFile) {
        this.createNewTrack(GAME_MUSIC);
        final GameView gameView = new GameViewImpl(this.controller, this, this.stage, this.player, saveFile);
        gameView.init();
    }
}
