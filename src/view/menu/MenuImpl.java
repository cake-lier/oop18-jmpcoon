package view.menu;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.io.File;

import controller.app.AppController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * The class implementation of the {@link Menu} interface.
 */
public final class MenuImpl implements Menu {
    private static final String LAYOUT_PATH = "layouts/";
    private static final String SAVES_PATH = "saves/";
    private static final String FIRST_SAVE_FILE = "save1.sav";
    private static final String SECOND_SAVE_FILE = "save2.sav";
    private static final String THIRD_SAVE_FILE = "save3.sav";
    private static final String MENU_LAYOUT = LAYOUT_PATH + "menu.fxml";
    private static final String SETTINGS_LAYOUT = LAYOUT_PATH + "settings.fxml";
    private static final String LOADER_LAYOUT = LAYOUT_PATH + "savesLoader.fxml";
    private static final String TIME_FORMAT = "d MMMM yyyy H:m";
    private static final String DEL_MSG = "Are you sure you want to delete this savegame?";
    private static final String DEL_ERR_MSG = " was not correctly deleted!";
    private static final String NO_SAVE_MSG = "No save game in this slot";
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
    @FXML
    private Button firstSave;
    @FXML
    private Button firstDelete;
    @FXML
    private Button secondSave;
    @FXML
    private Button secondDelete;
    @FXML
    private Button thirdSave;
    @FXML
    private Button thirdDelete;

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

    private void drawFromURL(final String drawableResource) {
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource(drawableResource));
        loader.setController(this);
        try {
            this.stage.setScene(new Scene(loader.load()));
            this.drawn = true;
        } catch (final IOException ex) {
            new Alert(AlertType.ERROR, ex.getLocalizedMessage()).show();
        }
    }

    private void initSaveDeleteButton(final Button save, final Button delete, final String fileName) {
        final URL fileURL = ClassLoader.getSystemResource(SAVES_PATH + fileName);
        if (fileURL != null) {
            final File file = new File(fileURL.getFile());
            final String created = LocalDateTime.ofEpochSecond(file.lastModified() / 1000, 0, 
                                                               ZoneOffset.of(ZoneOffset.systemDefault()
                                                                                       .getRules()
                                                                                       .getOffset(Instant.now())
                                                                                       .getId()))
                                                .format(DateTimeFormatter.ofPattern(TIME_FORMAT));
            save.setText(created);
            save.setOnMouseClicked(e -> {
                this.controller.startGame(Optional.of(fileURL));
            });
            delete.setOnMouseClicked(e -> {
                final Alert alert = new Alert(AlertType.CONFIRMATION, DEL_MSG);
                final Optional<ButtonType> choice = alert.showAndWait();
                choice.ifPresent(b -> {
                    if (b.equals(ButtonType.OK)) {
                        if (!file.delete()) {
                            new Alert(AlertType.ERROR, file.getName() + DEL_ERR_MSG).show();
                        }
                        save.setDisable(true);
                        save.setText(NO_SAVE_MSG);
                        delete.setDisable(true);
                    }
                }); 
            });
        } else {
            save.setDisable(true);
            delete.setDisable(true);
            save.setText(NO_SAVE_MSG);
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
            this.controller.startGame(Optional.empty());
        });
        this.quitButton.setOnMouseClicked(e -> this.controller.exitApp());
        this.savesButton.setOnMouseClicked(e -> {
            this.drawFromURL(LOADER_LAYOUT);
            this.initSaveDeleteButton(this.firstSave, this.firstDelete, FIRST_SAVE_FILE);
            this.initSaveDeleteButton(this.secondSave, this.secondDelete, SECOND_SAVE_FILE);
            this.initSaveDeleteButton(this.thirdSave, this.thirdDelete, THIRD_SAVE_FILE);
        });
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
