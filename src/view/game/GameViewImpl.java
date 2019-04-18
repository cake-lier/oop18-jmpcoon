package view.game;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.entities.EntityType;
import model.world.CollisionEvent;
import view.View;
import view.menus.GameMenu;
import view.menus.GameMenuImpl;

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
import controller.game.InputType;

/**
 * The class implementation of the {@link GameView} interface.
 */
public final class GameViewImpl implements GameView {
    private static final String INIT_ERR = "You can't call this method before initializing the instance";
    private static final String BG_IMAGE = "images/bg_game.png";
    private static final String LAYOUT_PATH = "layouts/";
    private static final String SCORE_SRC = LAYOUT_PATH + "score.fxml";
    private static final String END_MSG_SRC = LAYOUT_PATH + "endMessage.fxml";
    private static final String WIN_COLOR = "#FFB100";
    private static final String LOSE_COLOR = "#BB29BB";
    private static final String SCORE_STR = "Score: ";
    private static final String LIVES_STR = " - Lives: ";
    private static final String WIN_MSG = "YOU WON";
    private static final String LOSE_MSG = "GAME OVER";
    private static final String FONT_SIZE = "-fx-font-size: ";
    private static final String PADDING = "-fx-padding: ";
    private static final String SIZE_UNIT = "em";
    private static final int SCORE_RATIO = 255;
    private static final int SCORE_PADDING_RATIO = 2500;
    private static final int END_MSG_RATIO = 40;
    private static final int END_BUTTONS_RATIO = 200;

    private final AppController appController;
    private final View appView;
    private final Stage stage;
    private final Pane entities;
    private final MediaPlayer music;
    private final double volume;
    private final EventHandler<KeyEvent> commandHandler;
    private EventHandler<WindowEvent> closeHandler;
    private GameController gameController;
    private MemoizedEntityConverter entityConverter;
    private GameMenu gameMenu;
    private StackPane root;
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
    private Button restartButton;

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
        this.volume = this.music.getVolume();
        this.entities = new Pane();
        this.mutableInitialization();
        this.commandHandler = key -> this.processInput(key);
        this.isGameEnded = false;
        this.isMenuVisible = false;
        this.isInitialized = false;
    }

    /**
     * Assigns a value to the mutable fields of this instance. It can be called multiple times, so as to reinitialize these
     * fields every time a new game starts.
     */
    private void mutableInitialization() {
        this.root = new StackPane();
        this.gameController = new GameControllerImpl(this);
        this.entityConverter = new MemoizedEntityConverterImpl(this.gameController.getWorldDimensions(),
                                                           new ImmutablePair<>(this.stage.getScene().getWidth(),
                                                                                   this.stage.getScene().getHeight()));
        this.gameMenu = new GameMenuImpl(this.root, this.stage.getHeight(), this.appController, this.appView, this.gameController, this);
        this.closeHandler = e -> this.gameController.stopGame();
    }

    /*
     * Checks if this instance has already been initialized and if not, throws an exception.
     */
    private void checkInitialization() {
        if (!this.isInitialized) {
            throw new IllegalStateException(INIT_ERR);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void initialize(final Optional<Integer> saveFileIndex) {
        this.setupStage();
        this.gameMenu.draw();
        Arrays.asList(Sounds.values()).parallelStream()
                                      .map(value -> value.getSound())
                                      .forEach(sound -> {
                                          sound.setVolume(this.music.isMute() ? 0 : this.music.getVolume());
                                      });
        if (saveFileIndex.isPresent()) {
            try {
                this.gameController.loadGame(saveFileIndex.get());
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
        this.drawAliveEntities();
        this.music.play();
        this.isInitialized = true;
        this.gameController.startGame();
    }

    /*
     * Sets up the stage and the scene in it by also setting up root's children ordered by layer (from bottom to top: platforms,
     * ladders, entities, score).
     */
    private void setupStage() {
        final Pane platforms = new Pane();
        final Pane ladders = new Pane();
        platforms.getChildren().addAll(this.getNodes(EntityType.PLATFORM));
        ladders.getChildren().addAll(this.getNodes(EntityType.LADDER));
        this.root.getChildren().addAll(platforms, ladders, this.entities);
        try {
            final FXMLLoader scoreLoader = new FXMLLoader(ClassLoader.getSystemResource(SCORE_SRC));
            scoreLoader.setController(this);
            final FlowPane scorePane = scoreLoader.load();
            scorePane.setStyle(PADDING + this.stage.getHeight() / SCORE_PADDING_RATIO + SIZE_UNIT);
            this.root.getChildren().add(scorePane);
            this.score.setStyle(FONT_SIZE + this.stage.getHeight() / SCORE_RATIO + SIZE_UNIT);
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
        this.stage.getScene().addEventHandler(KeyEvent.KEY_RELEASED, this.commandHandler);
    }

    /**
     * Draws only the alive entities of the specified types.
     */
    private void drawAliveEntities() {
        final List<Node> nodes = new ArrayList<>();
        Arrays.asList(EntityType.POWERUP, EntityType.WALKING_ENEMY, EntityType.PLAYER, EntityType.ROLLING_ENEMY)
              .forEach(type -> nodes.addAll(this.getNodes(type)));
        this.entities.getChildren().setAll(nodes);
    }

    /*
     * Returns a collection of ImageViews, which are Nodes, of the specified EntityType.
     */
    private Collection<Node> getNodes(final EntityType type) {
        return this.gameController.getAliveEntities()
                                  .stream()
                                  .map(this.entityConverter::getDrawableEntity)
                                  .filter(entity -> entity.getEntityType() == type)
                                  .map(DrawableEntity::getImageView)
                                  .collect(Collectors.toList());
    }

    /**
     *{@inheritDoc}
     */
    public void update() {
        this.checkInitialization();
        Platform.runLater(() -> {
            this.entityConverter.removeUnusedEntities(this.gameController.getDeadEntities());
            this.drawAliveEntities();
            this.gameController.getCurrentEvents().forEach(e -> this.notifyEvent(e));
            this.score.setText(SCORE_STR + this.gameController.getCurrentScore() + LIVES_STR + this.gameController.getPlayerLives());
        });
    }

    private void processInput(final KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            this.manageInput(event.getCode(), true);
        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            this.manageInput(event.getCode(), false);
        }
    }

    /*
     * Finds the key's correspondent in InputKey which has a method that converts it into InputType, which is passed to the
     * gameController.
     * forward must be true if the input has to be propagated to the GameController, false if it has to be removed from the
     * GameController (this distinction isn't valid for the escape key)
     */
    private void manageInput(final KeyCode key, final boolean forward) {
        Stream.of(InputKey.values())
              .filter(input -> input.name().equals(key.name()))
              .findAny()
              .ifPresent(input -> {
                  if (input == InputKey.ESCAPE && forward) {
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
                          }
                      }
                  } else {
                      if (input.convert().isPresent()) {
                          final InputType type = input.convert().get();
                          if (forward && this.gameController.processInput(type) 
                              && type == InputType.UP && !this.music.isMute()) {
                              Sounds.JUMP.getSound().play(this.volume);
                          } else if (!forward) {
                              this.gameController.stopInput(type);
                          }
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
            if (!this.music.isMute()) {
                Sounds.PLAYER_DEATH.getSound().play(this.volume);
            }
            this.showMessage(LOSE_MSG);
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showPlayerWin() {
        this.checkInitialization();
        Platform.runLater(() -> {
            if (!this.music.isMute()) {
                Sounds.END_GAME.getSound().setVolume(this.volume);
                Sounds.END_GAME.getSound().play();
            }
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
            this.message.setStyle(FONT_SIZE + this.stage.getHeight() / END_MSG_RATIO + SIZE_UNIT);
            if (msg.equals(WIN_MSG)) {
                this.message.setFill(Color.web(WIN_COLOR));
            } else if (msg.equals(LOSE_MSG)) {
                this.message.setFill(Color.web(LOSE_COLOR));
            }
            this.finalBackMenuButton.setStyle(FONT_SIZE + this.stage.getHeight() / END_BUTTONS_RATIO + SIZE_UNIT);
            this.finalBackMenuButton.setOnMouseClicked(e -> {
                this.clean();
                this.appView.displayMenu();
            });
            this.restartButton.setStyle(FONT_SIZE + this.stage.getHeight() / END_BUTTONS_RATIO + SIZE_UNIT);
            this.restartButton.setOnMouseClicked(e -> {
                this.mutableInitialization();
                this.initialize(Optional.absent());
                this.isGameEnded = false;
                this.gameController.startGame();
            });
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void clean() {
        this.checkInitialization();
        this.stage.getScene().removeEventHandler(KeyEvent.KEY_PRESSED, this.commandHandler);
        this.stage.removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, this.closeHandler);
    }

    private void notifyEvent(final CollisionEvent type) {
        this.checkInitialization();
        switch (type) {
            case ROLLING_ENEMY_KILLED:
                if (!this.music.isMute()) {
                    Sounds.ROLLING_DESTROY.getSound().play(this.volume);
                }
                break;
            case WALKING_ENEMY_KILLED:
                if (!this.music.isMute()) {
                    Sounds.WALKING_DESTROY.getSound().play(this.volume);
                }
                break;
            case INVINCIBILITY_HIT:
                if (!this.music.isMute()) {
                    Sounds.INVINCIBIITY.getSound().play(this.volume);
                }
                break;
            case POWER_UP_HIT:
                if (!this.music.isMute()) {
                    Sounds.POWER_UP_GOT.getSound().play(this.volume);
                }
                break;
            default:
        }
    }
}
