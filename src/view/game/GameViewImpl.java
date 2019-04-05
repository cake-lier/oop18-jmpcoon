package view.game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.entities.EntityType;
import view.View;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.ImmutablePair;

import controller.app.AppController;
import controller.game.GameController;
import controller.game.GameControllerImpl;

/**
 * The class implementation of the {@link GameView} interface.
 */
public class GameViewImpl implements GameView {
    private static final BackgroundImage BG_IMAGE = new BackgroundImage(new Image("images/bg_game.png"), BackgroundRepeat.ROUND, 
                                                                        BackgroundRepeat.ROUND, BackgroundPosition.CENTER,
                                                                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, 
                                                                                           true, true, false, true));
    private static final String SAVES_PATH = "saves/";
    private static final String TIME_FORMAT = "d MMMM yyyy H:m";
    private static final String NO_SAVE_MSG = "No save game in this slot";
    private static final String DEL_ERR_MSG = " was not correctly deleted!";
    private static final String FIRST_SAVE_FILE = "save1.sav";
    private static final String SECOND_SAVE_FILE = "save2.sav";
    private static final String THIRD_SAVE_FILE = "save3.sav";
    private static final String FONT_URL = ClassLoader.getSystemResource("fonts/darkforest.ttf").toExternalForm();
    private static final String SHADOW_COLOR = "#2D2926";
    private static final String WIN_COLOR = "#FFB100";
    private static final String LOSE_COLOR = "#BB29BB";
    private static final String SCORE_STR = "Score: ";
    private static final String WIN_MSG = "YOU WON";
    private static final String LOSE_MSG = "GAME OVER";
    private static final int SCORE_SIZE_RATIO = 20;
    private static final int SHADOW_TO_SIZE_RATIO = 10;
    private static final int PADDING_TO_SIZE_RATIO = 4;

    private final GameController gameController;
    private final EntityConverterImpl entityConverter;
    private final AppController appController;
    private final View appView;
    private final Stage stage;
    private final StackPane root;
    private final Pane entities;
    private final Text score;
    private BorderPane menu;
    private BorderPane saveMenu;
    private boolean isMenuVisible;
    private boolean isGameEnded;
    private final MediaPlayer music;

    @FXML
    private Button backMenuButton;
    @FXML
    private Button quitButton;
    @FXML
    private Button saveButton;
    @FXML
    private Text message;
    @FXML
    private Button firstSave;
    @FXML
    private Button secondSave;
    @FXML
    private Button thirdSave;
    @FXML
    private Button backButton;

    /**
     * Binds this game view to the instance of the {@link AppController},
     * acquires the {@link Stage} in which to draw the game,
     * creates an instance of the {@link GameController}.
     * @param appController The application controller.
     * @param view The application view.
     * @param stage The stage in which to draw the game scene.
     * @param music The music to play in the background.
     * @param saveFile The file from which to load the game and display it, if present.
     */
    public GameViewImpl(final AppController appController, final View view, final Stage stage, final MediaPlayer music,
                        final Optional<URL> saveFile) {
        this.appController = Objects.requireNonNull(appController);
        this.gameController = new GameControllerImpl(this);
        this.appView = Objects.requireNonNull(view);
        this.music = Objects.requireNonNull(music);
        this.stage = Objects.requireNonNull(stage);
        this.root = new StackPane();
        this.entities = new Pane();
        this.score = new Text();
        this.isMenuVisible = false;
        this.isGameEnded = false;
        this.entityConverter = new EntityConverterImpl(this.gameController.getWorldDimensions(),
                                                       new ImmutablePair<>(stage.getScene().getWidth(),
                                                                           stage.getScene().getHeight()));
        if (saveFile.isPresent()) {
            try {
                this.gameController.loadGame(saveFile.get());
            } catch (final IOException ex) {
                new Alert(AlertType.ERROR, ex.getLocalizedMessage());
            }
        }
        this.gameController.startGame();
    }

    /*
     * Sets the background of the passed Pane with a chosen image. It's positioned at the center of the scene,
     * stretched to cover the whole scene.
     */
    private void addBackgroundImage(final Pane root) {
        root.setBackground(new Background(BG_IMAGE));
    }

    /**
     *{@inheritDoc}
     */
    public void update() {
        Platform.runLater(() -> {
            this.entityConverter.removeUnusedEntities(this.gameController.getDeadEntities());
            this.drawAliveEntities();
            this.score.setText(SCORE_STR + this.gameController.getCurrentScore());
        });
    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        this.setupStage();
        this.setupMenu();
        this.drawAliveEntities();
    }

    /*
     * sets up the stage and the scene in it by also setting up root's children ordered by layer (from bottom to top: platforms,
     * ladders, entities, score)
     */
    private void setupStage() {
        final Pane platforms = new Pane();
        final Pane ladders = new Pane();
        final BorderPane hud = new BorderPane();

        final double scoreSize = this.stage.getScene().getHeight() / SCORE_SIZE_RATIO;
        this.score.setFont(Font.loadFont(FONT_URL, scoreSize));
        this.score.setFill(Color.FLORALWHITE);
        final double shadowSize = scoreSize / SHADOW_TO_SIZE_RATIO;
        this.score.setEffect(new DropShadow(0, shadowSize, shadowSize, Color.web(SHADOW_COLOR)));
        final FlowPane scorePane = new FlowPane(this.score);
        final double paddingSize = scoreSize / PADDING_TO_SIZE_RATIO;
        scorePane.setPadding(new Insets(paddingSize, 0, 0, paddingSize));
        hud.setTop(scorePane);

        platforms.getChildren().addAll(this.getNodes(EntityType.PLATFORM));
        ladders.getChildren().addAll(this.getNodes(EntityType.LADDER));
        this.root.getChildren().addAll(platforms, ladders, this.entities, hud);
        this.addBackgroundImage(this.root);

        final Scene scene = new Scene(this.root, this.stage.getScene().getWidth(), this.stage.getScene().getHeight());
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> this.getInput(key.getCode()));
        this.stage.setScene(scene);
        this.stage.sizeToScene();
        this.stage.setOnCloseRequest(e -> this.gameController.stopGame());
        this.music.play();
    }

    private void drawAliveEntities() {
        List<Node> nodes = new ArrayList<>();
        Arrays.asList(EntityType.PLAYER, EntityType.ROLLING_ENEMY, EntityType.WALKING_ENEMY)
              .forEach(type -> nodes.addAll(this.getNodes(type)));
        this.entities.getChildren().setAll(nodes);
    }

    /*
     * returns a collection of ImageViews(Nodes) of the specified EntityType
     */
    private Collection<Node> getNodes(final EntityType type) {
        return this.gameController.getAliveEntities()
                                  .stream()
                                  .map(entity -> this.entityConverter.getDrawableEntity(entity))
                                  .filter(entity -> entity.getEntityType() == type)
                                  .map(entity -> entity.getImageView())
                                  .collect(Collectors.toList());
    }

    /*
     * Finds the key's correspondent in InputKey which has a method that converts it into InputType,
     * which is passed to the gameController
     */
    private void getInput(final KeyCode key) {
        Stream.of(InputKey.values())
              .filter(input -> input.name().equals(key.name()))
              .findAny()
              .ifPresent(input -> {
                  if (input == InputKey.ESCAPE) {
                      if (!this.isGameEnded) {
                          this.gameController.togglePauseGame();
                          if (this.isMenuVisible) {
                              this.menu.setVisible(false);
                              this.saveMenu.setVisible(false);
                              this.isMenuVisible = false;
                              this.music.play();
                          } else {
                              this.menu.setVisible(true);
                              this.isMenuVisible = true;
                              this.music.pause();
                          }
                      }
                  } else {
                      input.convert().ifPresent(moveInput -> this.gameController.processInput(moveInput));
                  }
              });
    }

    /**
     * {@inheritDoc}
     */
    public void showGameOver() {
        Platform.runLater(() -> {
            this.showMessage(LOSE_MSG);
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showPlayerWin() {
        Platform.runLater(() -> {
            this.showMessage(WIN_MSG);
        });
    }

    private void initSaveButton(final Button save, final String fileName) {
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
                final Optional<ButtonType> choice = new Alert(AlertType.CONFIRMATION, "Are you sure you want to overwrite this saved game?").showAndWait();
                choice.ifPresent(b -> {
                    if (b.equals(ButtonType.OK)) {
                        if (file.delete()) {
                            try {
                                this.gameController.saveGame(fileURL);
                                final File newFile = new File(fileURL.getFile());
                                final String newCreated = LocalDateTime.ofEpochSecond(newFile.lastModified() / 1000, 0, 
                                                                                   ZoneOffset.of(ZoneOffset.systemDefault()
                                                                                                           .getRules()
                                                                                                           .getOffset(Instant.now())
                                                                                                           .getId()))
                                                                       .format(DateTimeFormatter.ofPattern(TIME_FORMAT));
                                save.setText(newCreated);
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
                        final File newFile = new File(saveURL.getFile());
                        final String newCreated = LocalDateTime.ofEpochSecond(newFile.lastModified() / 1000, 0, 
                                                                           ZoneOffset.of(ZoneOffset.systemDefault()
                                                                                                   .getRules()
                                                                                                   .getOffset(Instant.now())
                                                                                                   .getId()))
                                                               .format(DateTimeFormatter.ofPattern(TIME_FORMAT));
                        save.setText(newCreated);
                    } catch (IOException ex) {
                        new Alert(AlertType.ERROR, ex.getLocalizedMessage()).show();
                    }
                });
            } catch (MalformedURLException ex) {
                new Alert(AlertType.ERROR, ex.getLocalizedMessage()).show();
            }
        }
    }

    private void setupMenu() {
        final FXMLLoader menuLoader = new FXMLLoader(ClassLoader.getSystemResource("layouts/gameMenu.fxml"));
        menuLoader.setController(this);
        try {
            this.menu = menuLoader.load();
            this.menu.setVisible(false);
            this.root.getChildren().add(this.menu);
            this.backMenuButton.setOnMouseClicked(e -> {
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
        final FXMLLoader saveLoader = new FXMLLoader(ClassLoader.getSystemResource("layouts/saveGameMenu.fxml"));
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

    private void showMessage(final String msg) {
        this.isGameEnded = true;
        this.music.stop();
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("layouts/endMessage.fxml"));
        loader.setController(this);
        try {
            this.root.getChildren().add(loader.load());
            this.message.setText(msg);
            if (msg.equals(WIN_MSG)) {
                this.message.setFill(Color.web(WIN_COLOR));
            } else if (msg.equals(LOSE_MSG)) {
                this.message.setFill(Color.web(LOSE_COLOR));
            }
            this.backMenuButton.setOnMouseClicked(e -> {
                this.appView.displayMenu();
            });
            this.quitButton.setOnMouseClicked(e -> {
                this.appController.exitApp();
            });
        } catch (final IOException ex) {
            new Alert(AlertType.ERROR, ex.getLocalizedMessage()).show();
        }
    }
}
