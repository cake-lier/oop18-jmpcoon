package view.game;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.entities.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
    private static final int BIGFONTSIZE = 100;
    private static final int SCOREFONTSIZE = 24;
    private static final double OPACITY = 0.75;
    private static final String MUSIC_PATH = ClassLoader.getSystemResource("sounds/pixelland.mp3").toString();

    private final GameController gameController;
    private final EntityConverterImpl entityConverter;
    private final AppController appController;
    private final Stage stage;
    private final StackPane root;
    private final Pane entities = new Pane();
    private final Text score = new Text();
    private final BorderPane menu = new BorderPane();

    private boolean isMenuVisible = false;

    private final BackgroundImage bgImage;
    private final AudioClip music;

    /**
     * Binds this game view to the instance of the {@link AppController},
     * acquires the {@link Stage} in which to draw the game,
     * creates an instance of the {@link GameController}.
     * @param appController The application controller.
     * @param stage The stage in which to draw the game scene.
     * @param music the music.
     */
    public GameViewImpl(final AppController appController, final Stage stage, final MediaPlayer music) {
        this.appController = Objects.requireNonNull(appController);
        this.gameController = new GameControllerImpl(this);

        final BackgroundSize bgSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true); 
        this.bgImage = new BackgroundImage(new Image("images/bg_game.png"), BackgroundRepeat.ROUND, BackgroundRepeat.ROUND,
                                           BackgroundPosition.CENTER, bgSize);

        this.music = new AudioClip(MUSIC_PATH);
        this.music.setCycleCount(AudioClip.INDEFINITE);

        this.stage = Objects.requireNonNull(stage);
        this.root = new StackPane();
        final Scene scene = new Scene(this.root, this.stage.getScene().getWidth(), this.stage.getScene().getHeight());
        this.addBackgroundImage(this.root);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> getInput(key.getCode()));

        this.entityConverter = new EntityConverterImpl(this.gameController.getWorldDimensions(), 
                                new ImmutablePair<>(scene.getWidth(), scene.getHeight()));

        this.stage.setScene(scene);
        this.stage.sizeToScene();
        this.stage.setOnCloseRequest(e -> this.gameController.stopGame());
        this.music.play();
        this.gameController.startGame();
    }

    /*
     * Sets the background of the passed Pane with a chosen image. It's positioned at the center of the scene,
     * stretched to cover the whole scene.
     */
    private void addBackgroundImage(final Pane root) {
        root.setBackground(new Background(this.bgImage));
    }

    /**
     *{@inheritDoc}
     */
    public void update() {
        Platform.runLater(() -> {
            this.entityConverter.removeUnusedEntities(this.gameController.getDeadEntities());
            this.drawAliveEntities();
            this.score.setText("Player score: " + this.gameController.getCurrentScore());
        });
    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        this.setupRoot();
        this.setupMenu();
        this.drawAliveEntities();
    }

    /*
     * sets up the root's children ordered by layer (platforms, ladders, entities, score)
     */
    private void setupRoot() {
        final Pane platforms = new Pane(), ladders = new Pane(); 
        final BorderPane hud = new BorderPane();

        this.score.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, SCOREFONTSIZE));
        this.score.setStroke(Color.RED);
        hud.setTop(this.score);

        this.root.getChildren().addAll(platforms, ladders, entities, hud);
        platforms.getChildren().addAll(this.getNodes(EntityType.PLATFORM));
        ladders.getChildren().addAll(this.getNodes(EntityType.LADDER));
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
                      this.gameController.togglePauseGame();
                      if (isMenuVisible) {
                          this.menu.setVisible(false);
                          this.isMenuVisible = false;
                      } else {
                          this.menu.setVisible(true);
                          this.isMenuVisible = true;
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
            this.showMessage("GAME OVER");
            this.music.stop();
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showPlayerWin() {
        Platform.runLater(() -> {
            this.showMessage("PLAYER WIN");
            this.music.stop();
        });
    }

    private void setupMenu() {
        this.menu.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, OPACITY), CornerRadii.EMPTY, Insets.EMPTY)));

        final Text text = new Text("THIS WILL BE A MENU");
        text.setFont(Font.font("Gill Sans Ultra Bold Condensed", BIGFONTSIZE));
        text.setFill(Color.RED);
        text.setStrokeWidth(2);
        text.setStroke(Color.WHITE);
        this.menu.setCenter(text);
        this.menu.setVisible(false);
        this.root.getChildren().add(menu);
    }

    private void showMessage(final String msg) {
        final BorderPane pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, OPACITY), CornerRadii.EMPTY, Insets.EMPTY)));

        final Text text = new Text(msg);
        text.setFont(Font.font("Gill Sans Ultra Bold Condensed", BIGFONTSIZE));
        text.setFill(Color.RED);
        text.setStrokeWidth(2);
        text.setStroke(Color.WHITE);
        pane.setCenter(text);
        this.root.getChildren().add(pane);
    }
}
