package view.menus;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.google.common.base.Optional;

import controller.app.AppController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import view.ResizableView;

/**
 * The class implementation of the {@link AppMenu} interface.
 */
public final class AppMenuImpl implements AppMenu {
    private static final String LAYOUT_PATH = "layouts/";
    private static final String MENU_LAYOUT = LAYOUT_PATH + "menu.fxml";
    private static final String SETTINGS_LAYOUT = LAYOUT_PATH + "settings.fxml";
    private static final String LOADER_LAYOUT = LAYOUT_PATH + "savesLoader.fxml";
    private static final String TIME_FORMAT = "d MMMM yyyy HH:mm";
    private static final String DEL_MSG = "Are you sure you want to delete this game save?";
    private static final String DEL_ERR_MSG = " was not correctly deleted!";
    private static final String NO_SAVE_MSG = "No save game in this slot";
    private static final String FONT_SIZE = "-fx-font-size: ";
    private static final String SIZE_UNIT = "em";
    private static final int VOLUME_RATIO = 100;
    private static final int TITLE_RATIO = 105;
    private static final int MAIN_BUTTONS_RATIO = 200;
    private static final int BACK_BUTTONS_RATIO = 300;
    private static final int LOAD_BUTTONS_RATIO = 135;
    private static final int DELETE_BUTTONS_RATIO = 250;

    private final AppController controller;
    private final ResizableView view;
    private final Stage stage;
    private final MediaPlayer music;
    private final double stageHeight;
    private boolean drawn;
    private boolean showed;

    @FXML
    private GridPane frontPage;
    @FXML
    private GridPane savesPage;
    @FXML
    private GridPane settingsPage;
    @FXML
    private Text firstTitle;
    @FXML
    private Text secondTitle;
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
    private CheckBox muteCheck;
    @FXML
    private ChoiceBox<Integer> screenChoice;
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
     * @param view The view which manages the screen.
     * @param stage The {@link Stage} in which adding this menu and all its pages.
     * @param stageHeight The height of the {@link javafx.stage.Stage} which contains the scene.
     * @param music The {@link MediaPlayer} from which play music while the menu is showed.
     */
    public AppMenuImpl(final AppController controller, final ResizableView view, final Stage stage, final double stageHeight, final MediaPlayer music) {
        this.controller = controller;
        this.view = view;
        this.stage = stage;
        this.stageHeight = stageHeight;
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
            ex.printStackTrace();
        }
    }

    private void initLoadDeleteButton(final Button load, final Button delete, final int saveFileIndex) {
        load.setStyle(FONT_SIZE + this.stageHeight / LOAD_BUTTONS_RATIO + SIZE_UNIT);
        delete.setStyle(FONT_SIZE + this.stageHeight / DELETE_BUTTONS_RATIO + SIZE_UNIT);
        if (this.controller.getSaveFileAvailability().get(saveFileIndex).isPresent()) {
            final String created = LocalDateTime.ofEpochSecond(
                                                    this.controller.getSaveFileAvailability().get(saveFileIndex).get() / 1000, 0, 
                                                    ZoneOffset.of(ZoneOffset.systemDefault()
                                                                            .getRules()
                                                                            .getOffset(Instant.now())
                                                                            .getId()))
                                                .format(DateTimeFormatter.ofPattern(TIME_FORMAT));
            load.setText(created);
            load.setOnMouseClicked(e -> {
                this.music.stop();
                this.controller.startGame(Optional.of(saveFileIndex));
            });
            delete.setOnMouseClicked(e -> {
                final Alert deleteAlert = new Alert(AlertType.CONFIRMATION, DEL_MSG);
                deleteAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                final Optional<ButtonType> choice = Optional.fromJavaUtil(deleteAlert.showAndWait());
                if (choice.isPresent() && choice.get().equals(ButtonType.OK)) {
                    if (!this.controller.deleteSaveFile(saveFileIndex)) {
                        new Alert(AlertType.ERROR, "save file " + saveFileIndex + DEL_ERR_MSG).show();
                    }
                    load.setDisable(true);
                    load.setText(NO_SAVE_MSG);
                    delete.setDisable(true);
                }
            });
        } else {
            load.setDisable(true);
            delete.setDisable(true);
            load.setText(NO_SAVE_MSG);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw() {
        if (!this.drawn) {
            final StackPane root = new StackPane();
            this.stage.getScene().setRoot(root);
            this.drawFromURL(MENU_LAYOUT, root);
            this.frontPage.setVisible(false);
            this.firstTitle.setStyle(FONT_SIZE + this.stageHeight / TITLE_RATIO + SIZE_UNIT);
            this.secondTitle.setStyle(FONT_SIZE + this.stageHeight / TITLE_RATIO + SIZE_UNIT);
            this.startButton.setStyle(FONT_SIZE + this.stageHeight / MAIN_BUTTONS_RATIO + SIZE_UNIT);
            this.startButton.setOnMouseClicked(e -> {
                this.music.stop();
                this.hide();
                this.controller.startGame(Optional.absent());
            });
            this.quitButton.setStyle(FONT_SIZE + this.stageHeight / MAIN_BUTTONS_RATIO + SIZE_UNIT);
            this.quitButton.setOnMouseClicked(e -> this.controller.exitApp());
            this.drawFromURL(LOADER_LAYOUT, root);
            this.savesPage.setVisible(false);
            this.initLoadDeleteButton(this.firstSave, this.firstDelete, 0);
            this.initLoadDeleteButton(this.secondSave, this.secondDelete, 1);
            this.initLoadDeleteButton(this.thirdSave, this.thirdDelete, 2);
            this.backSavesButton.setStyle(FONT_SIZE + this.stageHeight / BACK_BUTTONS_RATIO + SIZE_UNIT);
            this.backSavesButton.setOnMouseClicked(e -> {
                this.savesPage.setVisible(false);
                this.frontPage.setVisible(true);
            });
            this.savesButton.setStyle(FONT_SIZE + this.stageHeight / MAIN_BUTTONS_RATIO + SIZE_UNIT);
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
            this.muteCheck.setAllowIndeterminate(false);
            this.muteCheck.selectedProperty().addListener(e -> {
                this.music.setMute(this.muteCheck.isSelected());
            });
            this.screenChoice.setItems(Screen.getScreens()
                                             .parallelStream()
                                             .map(screen -> Screen.getScreens().indexOf(screen))
                                             .collect(Collectors.toCollection(() -> FXCollections.observableArrayList())));
            this.screenChoice.setValue(Screen.getScreens()
                                             .indexOf(Screen.getScreensForRectangle(this.stage.getX(),
                                                                                        this.stage.getY(),
                                                                                            this.stage.getWidth(),
                                                                                                this.stage.getHeight())
                                                            .get(0)));
            this.screenChoice.valueProperty().addListener(e -> {
                this.stage.setScene(new Scene(new Pane()));
                this.view.setScreenSize(this.screenChoice.getValue());
                this.drawn = false;
                this.showed = false;
                this.draw();
                this.show();
            });
            this.backSettingsButton.setStyle(FONT_SIZE + this.stageHeight / BACK_BUTTONS_RATIO + SIZE_UNIT);
            this.backSettingsButton.setOnMouseClicked(e -> {
                this.settingsPage.setVisible(false);
                this.frontPage.setVisible(true);
            });
            this.settingsButton.setStyle(FONT_SIZE + this.stageHeight / MAIN_BUTTONS_RATIO + SIZE_UNIT);
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
