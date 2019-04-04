package view.menu;

import java.io.IOException;
import java.net.URL;

import controller.app.AppController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    @FXML
    private Button startButton;
    @FXML
    private Button savesButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button quitButton;

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

    private void drawFromURL(final URL drawableResource) {
        final FXMLLoader loader = new FXMLLoader(drawableResource);
        loader.setController(this);
        try {
            this.stage.setScene(new Scene(loader.load()));
            this.drawn = true;
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw() {
        this.drawFromURL(MENU_LAYOUT);
        this.startButton.setOnMouseClicked(e -> {
            this.music.stop();
            this.controller.startGame();
        });
        this.quitButton.setOnMouseClicked(e -> this.controller.exitApp());
        this.savesButton.setOnMouseClicked(e -> this.drawFromURL(LOADER_LAYOUT));
        this.settingsButton.setOnMouseClicked(e -> {
            this.drawFromURL(SETTINGS_LAYOUT);
            this.volumeControl.setValue(this.music.getVolume() * VOLUME_RATIO);
            this.volumeControl.valueProperty().addListener(c -> {
                this.music.setVolume(this.volumeControl.getValue() / VOLUME_RATIO);
            });
        });
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
