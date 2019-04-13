package view.menus;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.MediaPlayer;

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
    private static final String TIME_FORMAT = "d MMMM yyyy HH:mm";
    private static final String DEL_MSG = "Are you sure you want to delete this game save?";
    private static final String DEL_ERR_MSG = " was not correctly deleted!";
    private static final String NO_SAVE_MSG = "No save game in this slot";
    private static final int VOLUME_RATIO = 100;

    private final AppController controller;
    private final Scene scene;
    private final MediaPlayer music;
    private boolean drawn;
    private boolean showed;

    @FXML
    private GridPane frontPage;
    @FXML
    private GridPane savesPage;
    @FXML
    private GridPane settingsPage;
    @FXML
    private Button startButton;
    @FXML
    private Button savesButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button quitButton;
    @FXML
    private Slider volumeControl;
    @FXML
    private Button backSettingsButton;
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
    @FXML
    private Button backSavesButton;

    /**
     * Binds this menu to the instance who has to be the controller of the menu, which is the controller of the application.
     * Furthermore, it acquires the {@link Scene} in which to draw the menu.
     * @param controller The controller of this application.
     * @param scene The {@link Scene} in which adding this menu and all its pages.
     * @param music The {@link MediaPlayer} from which play music while the menu is showed.
     */
    public MenuImpl(final AppController controller, final Scene scene, final MediaPlayer music) {
        this.controller = controller;
        this.scene = scene;
        this.music = music;
        this.drawn = false;
        this.showed = false;
    }

    private void drawFromURL(final String drawableResource, final StackPane root) {
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource(drawableResource));
        loader.setController(this);
        try {
            final Pane page = loader.load();
            page.setVisible(false);
            root.getChildren().add(page);
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
                this.music.stop();
                this.controller.startGame(Optional.of(fileURL));
            });
            delete.setOnMouseClicked(e -> {
                final Optional<ButtonType> choice = new Alert(AlertType.CONFIRMATION, DEL_MSG).showAndWait();
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
        if (!this.drawn) {
            final StackPane root = new StackPane();
            this.scene.setRoot(root);
            this.drawFromURL(MENU_LAYOUT, root);
            this.frontPage.setVisible(false);
            this.startButton.setOnMouseClicked(e -> {
                this.music.stop();
                this.hide();
                this.controller.startGame(Optional.empty());
            });
            this.quitButton.setOnMouseClicked(e -> this.controller.exitApp());
            this.drawFromURL(LOADER_LAYOUT, root);
            this.savesPage.setVisible(false);
            this.initSaveDeleteButton(this.firstSave, this.firstDelete, FIRST_SAVE_FILE);
            this.initSaveDeleteButton(this.secondSave, this.secondDelete, SECOND_SAVE_FILE);
            this.initSaveDeleteButton(this.thirdSave, this.thirdDelete, THIRD_SAVE_FILE);
            this.backSavesButton.setOnMouseClicked(e -> {
                this.savesPage.setVisible(false);
                this.frontPage.setVisible(true);
            });
            this.savesButton.setOnMouseClicked(e -> {
                this.frontPage.setVisible(false);
                this.savesPage.setVisible(true);
            });
            this.drawFromURL(SETTINGS_LAYOUT, root);
            this.settingsPage.setVisible(false);
            this.volumeControl.setValue(this.music.getVolume() * VOLUME_RATIO);
            this.volumeControl.valueProperty().addListener(c -> {
                this.music.setVolume(this.volumeControl.getValue() / VOLUME_RATIO);
            });
            this.backSettingsButton.setOnMouseClicked(e -> {
                this.settingsPage.setVisible(false);
                this.frontPage.setVisible(true);
            });
            this.settingsButton.setOnMouseClicked(e -> {
                this.frontPage.setVisible(false);
                this.settingsPage.setVisible(true);
            });
            this.drawn = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        if (this.drawn && !this.showed) {
            this.frontPage.setVisible(true);
            this.showed = true;
            this.music.play();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide() {
        if (this.showed) {
            this.frontPage.setVisible(false);
            this.settingsPage.setVisible(false);
            this.savesPage.setVisible(false);
            this.showed = false;
        }
    }
}
