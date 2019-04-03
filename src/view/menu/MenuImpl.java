package view.menu;

import java.io.IOException;
import java.net.URL;

import controller.app.AppController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * The class implementation of the {@link Menu} interface.
 */
public final class MenuImpl implements Menu {
    private static final String LAYOUT_PATH = "layouts/";
    private static final URL MENU_LAYOUT = ClassLoader.getSystemResource(LAYOUT_PATH + "menu.fxml");
    private static final URL SETTINGS_LAYOUT = ClassLoader.getSystemResource(LAYOUT_PATH + "settings.fxml");
    private static final URL LOADER_LAYOUT = ClassLoader.getSystemResource(LAYOUT_PATH + "savesLoader.fxml");
    private static final int VOLUME_RATIO = 100;

    private final AppController controller;
    private final Stage stage;
    private final MediaPlayer music;
    private boolean drawn;
    private boolean showed;

    @FXML
    private Slider volumeControl;

    /**
     * Binds this menu to the instance who has to be the controller of the menu, which is the controller of the application.
     * Furthermore, it acquires the {@link Stage} in which to draw the menu.
     * @param controller The controller of this application.
     * @param stage The {@link Stage} in which to draw the menu.
     * @param music The {@link MediaPlayer} from which play music while the menu is showed.
     */
    public MenuImpl(final AppController controller, final Stage stage, final MediaPlayer music) {
        this.controller = controller;
        this.stage = stage;
        this.music = music;
        this.drawn = false;
        this.showed = false;
    }

    /*
     * Wrapper method to delegate to the application controller the job of starting the
     * game. This is made because every FXML file use only one controller and the
     * application controller couldn't be weigh down with other functionalities which are
     * view's relevance.
     */
    @FXML
    private void startGame() {
        this.music.stop();
        this.controller.startGame();
    }

    /*
     * Wrapper method to delegate to the application controller the job of exiting from
     * the app. This is made because every FXML file use only one controller and the
     * application controller couldn't be weigh down with functionalities which are view's
     * relevance.
     */
    @FXML
    private void exitApp() {
        this.controller.exitApp();
    }

    private void drawFromURL(final URL drawableResource) {
        final FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        loader.setLocation(drawableResource);
        try {
            this.stage.setScene(new Scene(loader.load()));
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        this.drawn = true;
    }

    /**
     * {@inheritDoc}
     * It loads the ".fxml" associated file and sets as JavaFX controller this specific
     * instance of menu, then draws it.
     */
    @Override
    @FXML
    public void draw() {
        this.drawFromURL(MENU_LAYOUT);
    }

    @FXML
    private void openSettings() {
        this.drawFromURL(SETTINGS_LAYOUT);
        this.volumeControl.setValue(this.music.getVolume() * VOLUME_RATIO);
        this.volumeControl.valueProperty().addListener(e -> {
            this.music.setVolume(this.volumeControl.getValue() / VOLUME_RATIO);
        });
    }

    @FXML
    private void openSavesLoader() {
        this.drawFromURL(LOADER_LAYOUT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        if (this.drawn && !this.showed) {
            this.stage.show();
            this.showed = true;
            this.music.play();
        }
    }
}
