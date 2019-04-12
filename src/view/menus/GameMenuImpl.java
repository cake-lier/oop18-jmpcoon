package view.menus;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
    private static final String SAVES_PATH = "saves/";
    private static final String LAYOUT_PATH = "layouts/";
    private static final String GAME_MENU_SRC = LAYOUT_PATH + "gameMenu.fxml";
    private static final String SAVE_GAME_MENU_SRC = LAYOUT_PATH + "saveGameMenu.fxml";
    private static final String TIME_FORMAT = "d MMMM yyyy HH:mm";
    private static final String NO_SAVE_MSG = "No save game in this slot";
    private static final String DEL_ERR_MSG = " was not correctly deleted!";
    private static final String OVERWRITE_MSG = "Are you sure you want to overwrite this saved game?";
    private static final String FIRST_SAVE_FILE = "save1.sav";
    private static final String SECOND_SAVE_FILE = "save2.sav";
    private static final String THIRD_SAVE_FILE = "save3.sav";
    private static final int MILLI_TO_SEC = 1000;

    private final AppController appController;
    private final View appView;
    private final GameController gameController;
    private final GameView gameView;
    private final Pane root;
    private GridPane menu;
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
     * @param appController The controller of this application.
     * @param appView The view of this application.
     * @param gameController The controller of this game.
     * @param gameView The view of this game.
     */
    public GameMenuImpl(final Pane root, final AppController appController, final View appView, final GameController gameController,
                        final GameView gameView) {
        this.root = root;
        this.appController = appController;
        this.appView = appView;
        this.gameController = gameController;
        this.gameView = gameView;
    }

    private void formatSaveSlotText(final Button saveButton, final File file) {
        saveButton.setText(LocalDateTime.ofEpochSecond(file.lastModified() / MILLI_TO_SEC, 0, 
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
        final URL fileURL = ClassLoader.getSystemResource(SAVES_PATH + fileName);
        if (fileURL != null) {
            final File file = new File(fileURL.getFile());
            this.formatSaveSlotText(save, file);
            save.getStyleClass().add("buttons");
            save.setOnMouseClicked(e -> {
                final Alert overwriteAlert = new Alert(AlertType.CONFIRMATION, OVERWRITE_MSG);
                overwriteAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                final Optional<ButtonType> choice = overwriteAlert.showAndWait();
                choice.ifPresent(b -> {
                    if (b.equals(ButtonType.OK)) {
                        if (file.delete()) {
                            try {
                                this.gameController.saveGame(fileURL);
                                this.formatSaveSlotText(save, new File(fileURL.getFile()));
                            } catch (IOException ex) {
                                new Alert(AlertType.ERROR, ex.getLocalizedMessage()).show();
                            }
                        } else {
                            new Alert(AlertType.ERROR, file.getName() + DEL_ERR_MSG).show();
                        }
                    }
                });
            });
        } else {
            save.setText(NO_SAVE_MSG);
            try {
                final URL saveURL = new URL(ClassLoader.getSystemResource(SAVES_PATH).toExternalForm() + fileName);
                save.setOnMouseClicked(e -> {
                    try {
                        this.gameController.saveGame(saveURL);
                        this.formatSaveSlotText(save, new File(saveURL.getFile()));
                        save.getStyleClass().add("buttons");
                    } catch (IOException ex) {
                        new Alert(AlertType.ERROR, ex.getLocalizedMessage()).show();
                    }
                });
            } catch (final MalformedURLException ex) {
                new Alert(AlertType.ERROR, ex.getLocalizedMessage()).show();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw() {
        final FXMLLoader menuLoader = new FXMLLoader(ClassLoader.getSystemResource(GAME_MENU_SRC));
        menuLoader.setController(this);
        try {
            this.menu = menuLoader.load();
            this.menu.setVisible(false);
            this.root.getChildren().add(this.menu);
            this.backMenuButton.setOnMouseClicked(e -> {
                this.gameView.cleanView();
                this.appView.displayMenu();
            });
            this.quitButton.setOnMouseClicked(e -> {
                this.appController.exitApp();
            });
            this.saveButton.setOnMouseClicked(e -> {
                this.menu.setVisible(false);
                this.saveMenu.setVisible(true);
            });
        } catch (final IOException ex) {
            new Alert(AlertType.ERROR, ex.getLocalizedMessage()).show();
        }
        final FXMLLoader saveLoader = new FXMLLoader(ClassLoader.getSystemResource(SAVE_GAME_MENU_SRC));
        saveLoader.setController(this);
        try {
            this.saveMenu = saveLoader.load();
            this.saveMenu.setVisible(false);
            this.root.getChildren().add(this.saveMenu);
            this.backButton.setOnMouseClicked(ev -> {
                this.saveMenu.setVisible(false);
                this.menu.setVisible(true);
            });
            this.initSaveButton(this.firstSave, FIRST_SAVE_FILE);
            this.initSaveButton(this.secondSave, SECOND_SAVE_FILE);
            this.initSaveButton(this.thirdSave, THIRD_SAVE_FILE);
        } catch (final IOException ex) {
            new Alert(AlertType.ERROR, ex.getLocalizedMessage()).show();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        this.menu.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide() {
        this.menu.setVisible(false);
        this.saveMenu.setVisible(false);
    }
}
