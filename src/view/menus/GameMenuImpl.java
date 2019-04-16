package view.menus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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
 * The class implementation of {@link GameMenu}.
 */
public final class GameMenuImpl implements GameMenu {
    private static final String SAVES_PATH = System.getProperty("user.home") + System.getProperty("file.separator") + "jmpcoon" 
                                             + System.getProperty("file.separator");
    private static final String LAYOUT_PATH = "layouts/";
    private static final String GAME_MENU_SRC = LAYOUT_PATH + "gameMenu.fxml";
    private static final String SAVE_GAME_MENU_SRC = LAYOUT_PATH + "saveGameMenu.fxml";
    private static final String TIME_FORMAT = "d MMMM yyyy HH:mm";
    private static final String NO_SAVE_MSG = "No save game in this slot";
    private static final String OVERWRITE_MSG = "Are you sure you want to overwrite this saved game?";
    private static final String FIRST_SAVE_FILE = "save1.sav";
    private static final String SECOND_SAVE_FILE = "save2.sav";
    private static final String THIRD_SAVE_FILE = "save3.sav";
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
     * @param root The {@link Pane} in which to draw this menu.
     * @param stageHeight The height of {@link Stage} which contains the root.
     * @param appController The controller of this application.
     * @param appView The view of this application.
     * @param gameController The controller of this game.
     * @param gameView The view of this game.
     */
    public GameMenuImpl(final Pane root, final double stageHeight, final AppController appController, final View appView,
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

    private void formatSaveSlotText(final Button saveButton, final File file) {
        saveButton.setText(LocalDateTime.ofEpochSecond(file.lastModified() / 1000, 0, 
                                                       ZoneOffset.of(ZoneOffset.systemDefault()
                                                                               .getRules()
                                                                               .getOffset(Instant.now())
                                                                               .getId()))
                                        .format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
    }

    /*
     * Initializes a generic save game button present in this menu.
     */
    private void initSaveButton(final Button save, final String fileName) {
        save.setStyle(FONT_SIZE + this.stageHeight / SAVE_BUTTONS_RATIO + SIZE_UNIT);
        final File file = Paths.get(SAVES_PATH + fileName).toFile();
        if (file.exists()) {
            this.formatSaveSlotText(save, file);
            save.getStyleClass().add(BTN_STYLE_CLASS);
            save.setOnMouseClicked(e -> {
                final Alert overwriteAlert = new Alert(AlertType.CONFIRMATION, OVERWRITE_MSG);
                overwriteAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                final Optional<ButtonType> choice = Optional.fromJavaUtil(overwriteAlert.showAndWait());
                if (choice.isPresent() && choice.get().equals(ButtonType.OK)) {
                    try {
                        this.gameController.saveGame(file);
                        this.formatSaveSlotText(save, file);
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } else {
            save.setText(NO_SAVE_MSG);
            final File saveFile = Paths.get(SAVES_PATH + fileName).toFile();
            save.setOnMouseClicked(e -> {
                try {
                    this.gameController.saveGame(saveFile);
                    this.formatSaveSlotText(save, saveFile);
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
                this.initSaveButton(this.firstSave, FIRST_SAVE_FILE);
                this.initSaveButton(this.secondSave, SECOND_SAVE_FILE);
                this.initSaveButton(this.thirdSave, THIRD_SAVE_FILE);
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
