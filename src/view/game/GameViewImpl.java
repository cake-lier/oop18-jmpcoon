package view.game;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.entities.EntityType;
import model.world.EventType;
import view.View;
import view.menus.GameMenu;
import view.menus.GameMenuImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.google.common.base.Optional;

import controller.app.AppController;
import controller.game.GameController;
import controller.game.GameControllerImpl;

/**
 * The class implementation of the {@link GameView} interface.
 */
public final class GameViewImpl implements GameView {
    private static final AudioClip JUMP = new AudioClip(ClassLoader.getSystemResource("sounds/jump.wav").toExternalForm());
    private static final AudioClip ROLL_DEST = new AudioClip(ClassLoader.getSystemResource("sounds/rollDestroy.wav").toExternalForm());
    private static final AudioClip WALK_DEST = new AudioClip(ClassLoader.getSystemResource("sounds/walkDestroy.wav").toExternalForm());
    private static final String BG_IMAGE = "images/bg_game.png";
    private static final String LAYOUT_PATH = "layouts/";
    private static final String SCORE_SRC = LAYOUT_PATH + "score.fxml";
    private static final String END_MSG_SRC = LAYOUT_PATH + "endMessage.fxml";
    private static final String WIN_COLOR = "#FFB100";
    private static final String LOSE_COLOR = "#BB29BB";
    private static final String SCORE_STR = "Score: ";
    private static final String WIN_MSG = "YOU WON";
    private static final String LOSE_MSG = "GAME OVER";
    private static final String INIT_ERR = "You can't call this method before initializing the instance";

    private final AppController appController;
    private final View appView;
    private final Stage stage;
    private final GameController gameController;
    private final EntityConverter entityConverter;
    private final GameMenu gameMenu;
    private final StackPane root;
    private final Pane entities;
    private final MediaPlayer music;
    private final EventHandler<KeyEvent> commandHandler;
    private final EventHandler<WindowEvent> closeHandler;
    private Optional<AudioClip> currentSound;
    private boolean isMenuVisible;
    private boolean isGameEnded;
    private boolean isInitialized;

    @FXML
    private Text score;
    @FXML
    private Text message;
    @FXML
    private Button finalBackMenuButton;
    @FXML
    private Button finalQuitButton;

    /**
     * Binds this game view to the instance of the {@link AppController}, acquires the {@link Stage} in which to draw the game,
     * creates an instance of the {@link GameController}.
     * @param appController The application controller.
     * @param view The application view.
     * @param stage The stage in which to draw the game scene.
     * @param music The music to play in background.
     */
    public GameViewImpl(final AppController appController, final View view, final Stage stage, final MediaPlayer music) {
        this.appController = Objects.requireNonNull(appController);
        this.appView = Objects.requireNonNull(view);
        this.music = Objects.requireNonNull(music);
        this.stage = Objects.requireNonNull(stage);
        this.gameController = new GameControllerImpl(this);
        this.root = new StackPane();
        this.entities = new Pane();
        this.entityConverter = new EntityConverterImpl(this.gameController.getWorldDimensions(),
                                                       new ImmutablePair<>(this.stage.getScene().getWidth(),
                                                                           this.stage.getScene().getHeight()));
        this.gameMenu = new GameMenuImpl(this.root, this.appController, this.appView, this.gameController, this);
        this.closeHandler = e -> this.gameController.stopGame();
        this.commandHandler = key -> this.getInput(key.getCode());
        this.currentSound = Optional.absent();
        this.isGameEnded = false;
        this.isMenuVisible = false;
        this.isInitialized = false;
    }

    private void checkInitialization() {
        if (!this.isInitialized) {
            throw new IllegalStateException(INIT_ERR);
        }
    }

    /**
     *{@inheritDoc}
     */
    public void update() {
        this.checkInitialization();
        Platform.runLater(() -> {
            this.entityConverter.removeUnusedEntities(this.gameController.getDeadEntities());
            this.drawAliveEntities();
            this.score.setText(SCORE_STR + this.gameController.getCurrentScore()
                               + "  |  Lives: " + this.gameController.getPlayerLives());
        });
    }

    /**
     * {@inheritDoc}
     */
    public void init(final Optional<File> saveFile) {
        this.setupStage();
        this.gameMenu.draw();
        this.drawAliveEntities();
        if (saveFile.isPresent()) {
            try {
                this.gameController.loadGame(saveFile.get());
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
        this.music.play();
        this.isInitialized = true;
        this.gameController.startGame();
    }

    /*
     * sets up the stage and the scene in it by also setting up root's children ordered by layer (from bottom to top: platforms,
     * ladders, entities, score)
     */
    private void setupStage() {
        final Pane platforms = new Pane();
        final Pane ladders = new Pane();
        final Pane powerups = new Pane();
        platforms.getChildren().addAll(this.getNodes(EntityType.PLATFORM));
        ladders.getChildren().addAll(this.getNodes(EntityType.LADDER));
        powerups.getChildren().addAll(this.getNodes(EntityType.POWERUP));
        this.root.getChildren().addAll(platforms, ladders, powerups, this.entities);
        try {
            final FXMLLoader scoreLoader = new FXMLLoader(ClassLoader.getSystemResource(SCORE_SRC));
            scoreLoader.setController(this);
            this.root.getChildren().add(scoreLoader.load());
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        this.root.setBackground(new Background(new BackgroundImage(new Image(BG_IMAGE), BackgroundRepeat.ROUND, 
                                                                   BackgroundRepeat.ROUND, BackgroundPosition.CENTER,
                                                                   new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, 
                                                                                      true, true, false, true))));
        this.stage.getScene().setRoot(this.root);
        this.stage.setOnCloseRequest(this.closeHandler);
        this.stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, this.commandHandler);
    }

    private void drawAliveEntities() {
        final List<Node> nodes = new ArrayList<>();
        Arrays.asList(EntityType.ROLLING_ENEMY, EntityType.WALKING_ENEMY, EntityType.POWERUP, EntityType.PLAYER)
              .forEach(type -> nodes.addAll(this.getNodes(type)));
        this.entities.getChildren().setAll(nodes);
    }

    /*
     * returns a collection of ImageViews(Nodes) of the specified EntityType
     */
    private Collection<Node> getNodes(final EntityType type) {
        return this.gameController.getAliveEntities()
                                  .stream()
                                  .map(this.entityConverter::getDrawableEntity)
                                  .filter(entity -> entity.getEntityType() == type)
                                  .map(DrawableEntity::getImageView)
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
                              this.gameMenu.hide();
                              this.isMenuVisible = false;
                              this.music.play();
                          } else {
                              this.gameMenu.show();
                              this.isMenuVisible = true;
                              this.music.pause();
                              if (this.currentSound.isPresent()) {
                                  this.currentSound.get().stop();
                              }
                          }
                      }
                  } else {
                      if (input.convert().isPresent()) {
                          this.gameController.processInput(input.convert().get());
                      }
                  }
              });
    }

    /**
     * {@inheritDoc}
     */
    public void showGameOver() {
        this.checkInitialization();
        Platform.runLater(() -> {
            this.showMessage(LOSE_MSG);
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showPlayerWin() {
        this.checkInitialization();
        Platform.runLater(() -> {
            this.showMessage(WIN_MSG);
        });
    }

    private void showMessage(final String msg) {
        this.isGameEnded = true;
        this.music.stop();
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource(END_MSG_SRC));
        loader.setController(this);
        try {
            this.root.getChildren().add(loader.load());
            this.message.setText(msg);
            if (msg.equals(WIN_MSG)) {
                this.message.setFill(Color.web(WIN_COLOR));
            } else if (msg.equals(LOSE_MSG)) {
                this.message.setFill(Color.web(LOSE_COLOR));
            }
            this.finalBackMenuButton.setOnMouseClicked(e -> {
                this.cleanView();
                this.appView.displayMenu();
            });
            this.finalQuitButton.setOnMouseClicked(e -> {
                this.appController.exitApp();
            });
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void cleanView() {
        this.checkInitialization();
        this.stage.getScene().removeEventHandler(KeyEvent.KEY_PRESSED, this.commandHandler);
        this.stage.removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, this.closeHandler);
    }

    @Override
    public void notifyEvent(final EventType type) {
        switch (type) {
            case ROLLING_COLLISION:
                this.currentSound = Optional.of(ROLL_DEST);
                break;
            case WALKING_COLLISION:
                this.currentSound = Optional.of(WALK_DEST);
                break;
            case PLAYER_JUMP:
                this.currentSound = Optional.of(JUMP);
                break;
            default:
        }
        if (this.currentSound.isPresent()) {
            this.currentSound.get().setVolume(this.music.getVolume());
            this.currentSound.get().play();
        }
    }
}
