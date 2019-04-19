package view.menus;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.google.common.base.Optional;

import controller.app.AppController;
import controller.game.GameController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import view.View;
import view.game.GameView;
import javafx.scene.control.Alert.AlertType;

/**
 * Represents the menu which will be launched during the game. It needs to draw all its elements, show and hide itself.
 */
public final class GameMenu implements Menu {
    private static final String LAYOUT_PATH = "layouts/";
    private static final String GAME_MENU_SRC = LAYOUT_PATH + "gameMenu.fxml";
    private static final String SAVE_GAME_MENU_SRC = LAYOUT_PATH + "saveGameMenu.fxml";
    private static final String TIME_FORMAT = "d MMMM yyyy HH:mm";
    private static final String NO_SAVE_MSG = "No save game in this slot";
    private static final String OVERWRITE_MSG = "Are you sure you want to overwrite this saved game?";
    private static final String BTN_STYLE_CLASS = "buttons";
    private static final String FONT_SIZE = "-fx-font-size: ";
    private static final String SIZE_UNIT = "em";
    private static final int MENU_BUTTONS_RATIO = 150;
    private static final int BACK_BUTTON_RATIO = 300;
    private static final int SAVE_BUTTONS_RATIO = 135;

    private final AppController appController;
    private final View appView;
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
     * The default constructor. It accepts the pane element in which to add itself, the {@link AppController}, the {@link View}
     * and the {@link GameController} of this particular instance of the game so as to save a game, go back to the main menu and
     * exit the game.
     * @param root the {@link Pane} in which to draw this menu
     * @param stageHeight the height of {@link Stage} which contains the root
     * @param appController the controller of this application
     * @param appView the view of this application
     * @param gameController the controller of this game
     * @param gameView the view of this game
     */
    public GameMenu(final Pane root, final double stageHeight, final AppController appController, final View appView,
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

    private void formatSaveSlotText(final Button saveButton, final Long lastModified) {
        saveButton.setText(LocalDateTime.ofEpochSecond(lastModified / 1000, 0, 
                                                       ZoneOffset.of(ZoneOffset.systemDefault()
                                                                               .getRules()
                                                                               .getOffset(Instant.now())
                                                                               .getId()))
                                        .format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
    }

    /*
     * Initializes a generic save game button present in this menu.
     */
    private void initSaveButton(final Button save, final int saveFileIndex) {
        save.setStyle(FONT_SIZE + this.stageHeight / SAVE_BUTTONS_RATIO + SIZE_UNIT);
        if (this.appController.getSaveFileAvailability().get(saveFileIndex).isPresent()) {
            this.formatSaveSlotText(save, this.appController.getSaveFileAvailability().get(saveFileIndex).get());
            save.getStyleClass().add(BTN_STYLE_CLASS);
            save.setOnMouseClicked(e -> {
                final Alert overwriteAlert = new Alert(AlertType.CONFIRMATION, OVERWRITE_MSG);
                overwriteAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                final Optional<ButtonType> choice = Optional.fromJavaUtil(overwriteAlert.showAndWait());
                if (choice.isPresent() && choice.get().equals(ButtonType.OK)) {
                    try {
                        this.gameController.saveGame(saveFileIndex);
                        this.formatSaveSlotText(save, this.appController.getSaveFileAvailability().get(saveFileIndex).get());
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } else {
            save.setText(NO_SAVE_MSG);
            save.setOnMouseClicked(e -> {
                try {
                    this.gameController.saveGame(saveFileIndex);
                    this.formatSaveSlotText(save, this.appController.getSaveFileAvailability().get(saveFileIndex).get());
                    save.getStyleClass().add(BTN_STYLE_CLASS);
                } catch (final IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw() {
        if (!this.drawn) {
            final FXMLLoader menuLoader = new FXMLLoader(ClassLoader.getSystemResource(GAME_MENU_SRC));
            menuLoader.setController(this);
            try {
                menuLoader.load();
                this.menu.setVisible(false);
                this.root.getChildren().add(this.menu);
                this.backMenuButton.setStyle(FONT_SIZE + this.stageHeight / MENU_BUTTONS_RATIO + SIZE_UNIT);
                this.backMenuButton.setOnMouseClicked(e -> {
                    this.gameView.clean();
                    this.appView.displayMenu();
                });
                this.quitButton.setStyle(FONT_SIZE + this.stageHeight / MENU_BUTTONS_RATIO + SIZE_UNIT);
                this.quitButton.setOnMouseClicked(e -> {
                    this.appController.exitApp();
                });
                this.saveButton.setStyle(FONT_SIZE + this.stageHeight / MENU_BUTTONS_RATIO + SIZE_UNIT);
                this.saveButton.setOnMouseClicked(e -> {
                    this.menu.setVisible(false);
                    this.saveMenu.setVisible(true);
                });
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
            final FXMLLoader saveLoader = new FXMLLoader(ClassLoader.getSystemResource(SAVE_GAME_MENU_SRC));
            saveLoader.setController(this);
            try {
                saveLoader.load();
                this.saveMenu.setVisible(false);
                this.root.getChildren().add(this.saveMenu);
                this.backButton.setStyle(FONT_SIZE + this.stageHeight / BACK_BUTTON_RATIO + SIZE_UNIT);
                this.backButton.setOnMouseClicked(ev -> {
                    this.saveMenu.setVisible(false);
                    this.menu.setVisible(true);
                });
                this.initSaveButton(this.firstSave, 0);
                this.initSaveButton(this.secondSave, 1);
                this.initSaveButton(this.thirdSave, 2);
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
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
}
