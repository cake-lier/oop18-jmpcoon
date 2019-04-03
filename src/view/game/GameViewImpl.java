package view.game;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    private static final BackgroundImage BG_IMAGE = new BackgroundImage(new Image("images/bg_game.png"), BackgroundRepeat.ROUND, 
                                                                        BackgroundRepeat.ROUND, BackgroundPosition.CENTER,
                                                                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, 
                                                                                           true, true, false, true));
    private static final String FONT_URL = ClassLoader.getSystemResource("fonts/darkforest.ttf").toExternalForm();
    private static final int SCORE_SIZE_RATIO = 20;
    private static final int MESSAGE_SIZE_RATIO = 4;
    private static final double OVERLAY_OPACITY = 0.75;

    private final GameController gameController;
    private final EntityConverterImpl entityConverter;
    private final AppController appController;
    private final Stage stage;
    private final StackPane root;
    private final Pane entities;
    private final Text score;
    private final BorderPane menu;
    private boolean isMenuVisible;
    private final MediaPlayer music;

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
        this.music = Objects.requireNonNull(music);
        this.stage = Objects.requireNonNull(stage);
        this.root = new StackPane();
        this.entities = new Pane();
        this.score = new Text();
        this.menu = new BorderPane();
        this.isMenuVisible = false;
        this.entityConverter = new EntityConverterImpl(this.gameController.getWorldDimensions(),
                                                       new ImmutablePair<>(stage.getScene().getWidth(),
                                                                           stage.getScene().getHeight()));
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
            this.score.setText("Score: " + this.gameController.getCurrentScore());
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
        final double shadowSize = scoreSize / 10;
        this.score.setEffect(new DropShadow(0, shadowSize, shadowSize, Color.web("#2D2926")));
        final FlowPane scorePane = new FlowPane(this.score);
        final double paddingSize = scoreSize / 4;
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
                      this.gameController.togglePauseGame();
                      if (this.isMenuVisible) {
                          this.menu.setVisible(false);
                          this.isMenuVisible = false;
                          this.music.play();
                      } else {
                          this.menu.setVisible(true);
                          this.isMenuVisible = true;
                          this.music.pause();
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
        this.menu.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, OVERLAY_OPACITY), CornerRadii.EMPTY, Insets.EMPTY)));
        this.menu.getStylesheets().add("layouts/buttons.css");
        final VBox buttons = new VBox();
        buttons.setPadding(new Insets(0, 0, 100, 0));
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);
        buttons.setFillWidth(true);
        final Button backButton = new Button("Back to menu");
        backButton.getStyleClass().add("buttons");
        backButton.setStyle("-fx-font-size: 5em");
        buttons.getChildren().add(backButton);
        final Button exitButton = new Button("Quit game");
        exitButton.getStyleClass().add("buttons");
        exitButton.setStyle("-fx-font-size: 5em");
        buttons.getChildren().add(exitButton);
        this.menu.setCenter(buttons);
        this.menu.setVisible(false);
        this.root.getChildren().add(menu);
        backButton.setOnMouseClicked(e -> {
            this.appController.startApp();
        });
        exitButton.setOnMouseClicked(e -> {
            this.appController.exitApp();
        });
    }

    private void showMessage(final String msg) {
        final BorderPane pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, OVERLAY_OPACITY), CornerRadii.EMPTY, Insets.EMPTY)));
        final Text text = new Text(msg);
        text.setFont(Font.loadFont(FONT_URL, this.stage.getScene().getHeight() / MESSAGE_SIZE_RATIO));
        text.setFill(Color.RED);
        pane.setCenter(text);
        final HBox buttons = new HBox();
        buttons.setPadding(new Insets(0, 0, 100, 0));
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);
        buttons.getStylesheets().add("layouts/buttons.css");
        final Button backButton = new Button("Back to menu");
        backButton.getStyleClass().add("buttons");
        backButton.setStyle("-fx-font-size: 5em");
        HBox.setHgrow(backButton, Priority.ALWAYS);
        buttons.getChildren().add(backButton);
        final Button exitButton = new Button("Quit game");
        exitButton.getStyleClass().add("buttons");
        exitButton.setStyle("-fx-font-size: 5em");
        HBox.setHgrow(exitButton, Priority.ALWAYS);
        buttons.getChildren().add(exitButton);
        pane.setBottom(buttons);
        this.root.getChildren().add(pane);
        backButton.setOnMouseClicked(e -> {
            this.appController.startApp();
        });
        exitButton.setOnMouseClicked(e -> {
            this.appController.exitApp();
        });
    }
}
