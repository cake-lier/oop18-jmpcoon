package view.menus;

import com.google.common.base.Optional;

import controller.SaveFile;
import controller.app.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.Ratios;
import view.ViewUtils;

/**
 * Represents the menu which will be launched before the game.
 */
public final class AppMenu implements Menu {
    private static final String LAYOUT_PATH = "layouts/";
    private static final String LAYOUT_EXT = ".fxml";
    private static final String MENU_LAYOUT = LAYOUT_PATH + "menu" + LAYOUT_EXT;
    private static final String SETTINGS_LAYOUT = LAYOUT_PATH + "settings" + LAYOUT_EXT;
    private static final String LOADER_LAYOUT = LAYOUT_PATH + "savesLoader" + LAYOUT_EXT;
    private static final String DEL_MSG = "Are you sure you want to delete this game save?";
    private static final String DEL_ERR_MSG = " was not correctly deleted!";
    private static final String NO_SAVE_MSG = "No save game in this slot";
    private static final String TICK_FONT_SIZE_PREF = "-fx-tick-label-font: ";
    private static final String TICK_FONT_SIZE_SUFF = "em \"dark forest\"";
    private static final String MUTED_LABEL = "I";
    private static final String UNMUTED_LABEL = "O";
    private static final int VOLUME_RATIO = 100;
    private static final int TICK_RATIO = 655;

    private final AppController controller;
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
    private Label volumeLabel;
    @FXML
    private Slider volumeControl;
    @FXML
    private Label muteLabel;
    @FXML
    private ToggleButton muteCheck;
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
     * Furthermore, it acquires the {@link Stage} in which to draw the menu.
     * @param controller the controller of this application
     * @param stage the {@link Stage} in which adding this menu and all its pages
     * @param stageHeight the height of the {@link javafx.stage.Stage} which contains the scene
     * @param music the {@link MediaPlayer} from which play music while the menu is showed
     */
    public AppMenu(final AppController controller, final Stage stage, final double stageHeight, final MediaPlayer music) {
        this.controller = controller;
        this.stage = stage;
        this.stageHeight = stageHeight;
        this.music = music;
        this.drawn = false;
        this.showed = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw() {
        if (!this.drawn) {
            final StackPane root = new StackPane();
            this.stage.getScene().setRoot(root);
            ViewUtils.drawFromURL(MENU_LAYOUT, this, root);
            this.frontPage.setVisible(false);
            Ratios.TITLE.styleNodeToRatio(this.stageHeight, this.firstTitle);
            Ratios.TITLE.styleNodeToRatio(this.stageHeight, this.secondTitle);
            Ratios.MAIN_BUTTONS.styleNodeToRatio(this.stageHeight, this.startButton);
            this.startButton.setOnMouseClicked(e -> {
                this.music.stop();
                this.hide();
                this.controller.startGame(Optional.absent());
            });
            Ratios.MAIN_BUTTONS.styleNodeToRatio(this.stageHeight, this.quitButton);
            this.quitButton.setOnMouseClicked(e -> this.controller.exitApp());
            ViewUtils.drawFromURL(LOADER_LAYOUT, this, root);
            this.savesPage.setVisible(false);
            this.initLoadDeleteButton(this.firstSave, this.firstDelete, 0);
            this.initLoadDeleteButton(this.secondSave, this.secondDelete, 1);
            this.initLoadDeleteButton(this.thirdSave, this.thirdDelete, 2);
            Ratios.BACK_BUTTONS.styleNodeToRatio(this.stageHeight, this.backSavesButton);
            this.backSavesButton.setOnMouseClicked(e -> ViewUtils.hideFirstNodeShowSecondNode(this.savesPage, this.frontPage));
            Ratios.MAIN_BUTTONS.styleNodeToRatio(this.stageHeight, this.savesButton);
            this.savesButton.setOnMouseClicked(e -> ViewUtils.hideFirstNodeShowSecondNode(this.frontPage, this.savesPage));
            ViewUtils.drawFromURL(SETTINGS_LAYOUT, this, root);
            this.settingsPage.setVisible(false);
            Ratios.LABELS.styleNodeToRatio(this.stageHeight, this.volumeLabel);
            this.volumeControl.setStyle(TICK_FONT_SIZE_PREF + this.stageHeight / TICK_RATIO + TICK_FONT_SIZE_SUFF);
            this.volumeControl.setValue(this.music.getVolume() * VOLUME_RATIO);
            this.volumeControl.valueProperty()
                              .addListener(e -> this.music.setVolume(this.volumeControl.getValue() / VOLUME_RATIO));
            Ratios.LABELS.styleNodeToRatio(this.stageHeight, this.muteLabel);
            Ratios.MUTE_TOGGLES.styleNodeToRatio(this.stageHeight, this.muteCheck);
            this.muteCheck.setSelected(this.music.isMute());
            this.muteCheck.setText(this.music.isMute() ? MUTED_LABEL : UNMUTED_LABEL);
            this.muteCheck.selectedProperty().addListener(e -> {
                this.music.setMute(this.muteCheck.isSelected());
                this.muteCheck.setText(this.muteCheck.isSelected() ? MUTED_LABEL : UNMUTED_LABEL);
            });
            Ratios.BACK_BUTTONS.styleNodeToRatio(this.stageHeight, this.backSettingsButton);
            this.backSettingsButton.setOnMouseClicked(e -> ViewUtils.hideFirstNodeShowSecondNode(this.settingsPage, this.frontPage));
            Ratios.MAIN_BUTTONS.styleNodeToRatio(this.stageHeight, this.settingsButton);
            this.settingsButton.setOnMouseClicked(e -> ViewUtils.hideFirstNodeShowSecondNode(this.frontPage, this.settingsPage));
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


    private void initLoadDeleteButton(final Button load, final Button delete, final int saveFileIndex) {
        Ratios.LOAD_BUTTONS.styleNodeToRatio(this.stageHeight, load);
        Ratios.DELETE_BUTTONS.styleNodeToRatio(this.stageHeight, delete);
        final Optional<Long> optTime = this.controller.getSaveFileAvailability().get(saveFileIndex);
        if (optTime.isPresent()) {
            ViewUtils.setTextToTime(load, optTime.get());
            load.setOnMouseClicked(e -> {
                this.music.stop();
                this.controller.startGame(Optional.of(saveFileIndex));
            });
            delete.setOnMouseClicked(e -> {
                final Alert deleteAlert = this.createCorrectlySizedAlert(AlertType.CONFIRMATION, DEL_MSG);
                final Optional<ButtonType> choice = Optional.fromJavaUtil(deleteAlert.showAndWait());
                if (choice.isPresent() && choice.get().equals(ButtonType.OK)) {
                    if (!this.controller.deleteSaveFile(saveFileIndex)) {
                        final Alert errorDeleteAlert 
                            = this.createCorrectlySizedAlert(AlertType.ERROR, SaveFile.values()[saveFileIndex] + DEL_ERR_MSG);
                        errorDeleteAlert.show();
                    }
                    this.setEmptySaveState(load, delete);
                }
            });
        } else {
            this.setEmptySaveState(load, delete);
        }
    }

    /*
     * Creates a new alert but with the dialog pane sufficiently large to display all the text.
     */
    private Alert createCorrectlySizedAlert(final AlertType type, final String message) {
        final Alert alert = new Alert(type, message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        return alert;
    }

    /*
     * Sets a couple of load save/delete save buttons to a graphical state associated with an empty save slot.
     */
    private void setEmptySaveState(final Button load, final Button delete) {
        load.setDisable(true);
        load.setText(NO_SAVE_MSG);
        delete.setDisable(true);
    }
}
