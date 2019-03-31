package view.game;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.ImmutablePair;

import controller.app.AppController;
import controller.game.GameController;
import controller.game.GameControllerImpl;

/**
 * The class implementation of the {@link GameView} interface.
 */
public class GameViewImpl implements GameView {
    private static final int FONTSIZE = 150;

    private final GameController gameController;
    private final EntityConverterImpl entityConverter;
    private final AppController appController;
    private final Stage stage;
    private final Scene scene;
    private final BackgroundImage bgImage;

    /**
     * Binds this game view to the instance of the {@link AppController},
     * acquires the {@link Stage} in which to draw the game,
     * creates an instance of the {@link GameController}.
     * @param appController The application controller.
     * @param stage The stage in which to draw the game scene.
     */
    public GameViewImpl(final AppController appController, final Stage stage) {
        this.appController = Objects.requireNonNull(appController);
        this.gameController = new GameControllerImpl(this);
        this.stage = Objects.requireNonNull(stage);
        final BackgroundSize bgSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true); 
        this.bgImage = new BackgroundImage(new Image("images/bg_game.png"), BackgroundRepeat.ROUND, BackgroundRepeat.ROUND,
                                           BackgroundPosition.CENTER, bgSize);
        final Pane root = new Pane();
        this.addBackgroundImage(root);
        this.scene = new Scene(root, this.stage.getScene().getWidth(), this.stage.getScene().getHeight());
        this.scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> getInput(key.getCode()));
        this.entityConverter = new EntityConverterImpl(this.gameController.getWorldDimensions(), 
                                new ImmutablePair<>(this.scene.getWidth(), this.scene.getHeight()));
        this.stage.setScene(this.scene);
        this.stage.sizeToScene();
        this.stage.setOnCloseRequest(e -> this.gameController.stopGame());
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
        });
    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        this.drawAliveEntities();
    }

    private void drawAliveEntities() {
        final Pane root = new Pane();
        this.gameController.getAliveEntities()
                           .stream()
                           .map(entity -> this.entityConverter.getDrawableEntity(entity))
                           .forEach(e -> root.getChildren().add(e.getImageView()));
        this.addBackgroundImage(root);
        this.scene.setRoot(root);
    }

    /**
     * {@inheritDoc}
     */
    public void showGameOver() {
        Platform.runLater(() -> {
            final Text text = new Text(150, this.stage.getHeight() / 2, "Game Over");
            text.setFont(Font.font("Helvetica", FontPosture.ITALIC, FONTSIZE));
            text.setFill(Color.RED);
            final Scene gameOver = new Scene(new Group(text), this.stage.getScene().getWidth(), this.stage.getScene().getHeight(), Color.BLACK);
            this.stage.setScene(gameOver);
            this.stage.sizeToScene();
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showPlayerWin() {
        Platform.runLater(() -> {
            final Text text = new Text(150, this.stage.getHeight() / 2, "You win!"); 
            text.setFont(Font.font("Helvetica", FontPosture.ITALIC, FONTSIZE));
            text.setFill(Color.LIGHTBLUE);
            final Scene win = new Scene(new Group(text), this.stage.getScene().getWidth(), this.stage.getScene().getHeight(), Color.BLACK);
            this.stage.setScene(win);
            this.stage.sizeToScene();
        });
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
                      //TODO: In-game menu
                      this.appController.startApp();
                  } else {
                      input.convert().ifPresent(moveInput -> this.gameController.processInput(moveInput));
                  }
              });
    }
}
