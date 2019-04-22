package view.menus;

import java.io.IOException;

import com.google.common.base.Optional;

import controller.app.AppController;
import controller.game.GameController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import view.Ratios;
import view.ViewUtils;
import view.app.AppView;
import view.game.GameView;
import javafx.scene.control.Alert.AlertType;

/**
 * Represents the menu which will be launched during the game.
 */
public final class GameMenu implements Menu {
    private static final String LAYOUT_PATH = "layouts/";
    private static final String LAYOUT_EXT = ".fxml";
    private static final String GAME_MENU_SRC = LAYOUT_PATH + "gameMenu" + LAYOUT_EXT;
    private static final String SAVE_GAME_MENU_SRC = LAYOUT_PATH + "saveGameMenu" + LAYOUT_EXT;
    private static final String NO_SAVE_MSG = "No save game in this slot";
    private static final String OVERWRITE_MSG = "Are you sure you want to overwrite this saved game?";
    private static final String BTN_STYLE_CLASS = "buttons";

    private final AppController appController;
    private final AppView appView;
    private final GameController gameController;
    private final GameView gameView;
    private final Pane root;
    private final double stageHeight;
    private boolean drawn;
    private boolean shown;
    @FXML
    private GridPane menu;
    @FXML
    private GridPane saveMenu;
    @FXML
    private Button backMenuButton;
    @FXML
    private Button quitButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button firstSave;
    @FXML
    private Button secondSave;
    @FXML
    private Button thirdSave;
    @FXML
    private Button backButton;

    /**
     * The default constructor. It accepts the pane element in which to add itself, the {@link AppController}, the {@link AppView}
     * and the {@link GameController} of this particular instance of the game so as to save a game, go back to the main menu and
     * exit the game.
     * @param root the {@link Pane} in which to draw this menu
     * @param stageHeight the height of {@link javafx.stage.Stage} which contains the root
     * @param appController the controller of this application
     * @param appView the view of this application
     * @param gameController the controller of this game
     * @param gameView the view of this game
     */
    public GameMenu(final Pane root, final double stageHeight, final AppController appController, final AppView appView,
                    final GameController gameController, final GameView gameView) {
        this.root = root;
        this.stageHeight = stageHeight;
        this.appController = appController;
        this.appView = appView;
        this.gameController = gameController;
        this.gameView = gameView;
        this.drawn = false;
        this.shown = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw() {
        if (!this.drawn) {
            ViewUtils.drawFromURL(GAME_MENU_SRC, this, this.root);
            Ratios.GAME_MENU_BUTTONS.styleNodeToRatio(this.stageHeight, this.backMenuButton);
            this.backMenuButton.setOnMouseClicked(e -> {
                this.gameView.clean();
                this.appView.displayMenu();
            });
            Ratios.GAME_MENU_BUTTONS.styleNodeToRatio(this.stageHeight, this.quitButton);
            this.quitButton.setOnMouseClicked(e -> this.appController.exitApp());
            Ratios.GAME_MENU_BUTTONS.styleNodeToRatio(this.stageHeight, this.saveButton);
            this.saveButton.setOnMouseClicked(e -> ViewUtils.hideFirstNodeShowSecondNode(this.menu, this.saveMenu));
            ViewUtils.drawFromURL(SAVE_GAME_MENU_SRC, this, this.root);
            Ratios.BACK_BUTTONS.styleNodeToRatio(this.stageHeight, this.backButton);
            this.backButton.setOnMouseClicked(e -> ViewUtils.hideFirstNodeShowSecondNode(this.saveMenu, this.menu));
            this.initSaveButton(this.firstSave, 0);
            this.initSaveButton(this.secondSave, 1);
            this.initSaveButton(this.thirdSave, 2);
            this.drawn = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        if (this.drawn && !this.shown) {
            this.menu.setVisible(true);
            this.shown = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide() {
        if (this.shown) {
            this.menu.setVisible(false);
            this.saveMenu.setVisible(false);
            this.shown = false;
        }
    }


    /*
     * Initializes a generic save game button present in this menu.
     */
    private void initSaveButton(final Button save, final int saveFileIndex) {
        Ratios.SAVE_BUTTONS.styleNodeToRatio(this.stageHeight, save);
        if (this.appController.getSaveFileAvailability().get(saveFileIndex).isPresent()) {
            ViewUtils.setTextToTime(save, this.appController.getSaveFileAvailability().get(saveFileIndex).get());
            this.styleToOverwritableButton(save, saveFileIndex);
        } else {
            save.setText(NO_SAVE_MSG);
            save.setOnMouseClicked(e -> {
                this.updateSaveButton(save, saveFileIndex);
                this.styleToOverwritableButton(save, saveFileIndex);
            });
        }
    }

    /*
     * Style a save button to be one of those which allow to save a file overriding one already existing.
     */
    private void styleToOverwritableButton(final Button save, final int saveFileIndex) {
        save.getStyleClass().add(BTN_STYLE_CLASS);
        save.setOnMouseClicked(e -> {
            final Alert overwriteAlert = new Alert(AlertType.CONFIRMATION, OVERWRITE_MSG);
            overwriteAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            final Optional<ButtonType> choice = Optional.fromJavaUtil(overwriteAlert.showAndWait());
            if (choice.isPresent() && choice.get().equals(ButtonType.OK)) {
               this.updateSaveButton(save, saveFileIndex);
            }
        });
    }

    /*
     * Updates the state of the passed save button by saving the game into the file at corresponding index and retrieving
     * the last modification time of the file just created.
     */
    private void updateSaveButton(final Button save, final int saveFileIndex) {
        try {
            this.gameController.saveGame(saveFileIndex);
            ViewUtils.setTextToTime(save, this.appController.getSaveFileAvailability().get(saveFileIndex).get());
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
