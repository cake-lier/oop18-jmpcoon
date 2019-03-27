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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Optional;
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
    private static final String BG_SOURCE = "images/bg_game.png";

    private final GameController gameController;
    private final AppController appController;
    private final Stage stage;
    private final Scene scene;

    /**
     * Binds this game view to the instance of the {@link AppController},
     * acquires the {@link Stage} in which to draw the game,
     * creates an instance of the {@link GameController}.
     * @param appController The application controller.
     * @param stage The stage in which to draw the game scene.
     */
    public GameViewImpl(final AppController appController, final Stage stage) {
        this.appController = appController;
        this.gameController = new GameControllerImpl(this, this.appController);
        this.stage = stage;
        final Pane root = new Pane();
        this.addBackgroundImage(root);
        this.scene = new Scene(root, this.stage.getScene().getWidth(), this.stage.getScene().getHeight());
        this.scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> getInput(key.getCode()));
        this.stage.setScene(this.scene);
    }

    /*
     * Sets the background of the passed Pane with a chosen image. It's positioned at the center of the scene,
     * stretched to cover the whole scene.
     */
    private void addBackgroundImage(final Pane root) {
        final Image bgImage = new Image(BG_SOURCE);
        final BackgroundSize bgSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true); 
        final BackgroundImage bgImagePositioned = new BackgroundImage(bgImage, BackgroundRepeat.ROUND, BackgroundRepeat.ROUND, BackgroundPosition.CENTER, bgSize);
        root.setBackground(new Background(bgImagePositioned));
    }

    /**
     *{@inheritDoc}
     */
    public void update() {
        Platform.runLater(() -> {
            final EntityConverter entityConverter = new EntityConverter(this.gameController.getWorldDimensions(),
                    new ImmutablePair<>(this.scene.getWidth(), this.scene.getHeight()));
            final Pane root = new Pane();
            this.addBackgroundImage(root);
            this.gameController.getEntities()
            .stream()
            .map(entity -> entityConverter.getDrawableEntity(entity))
            .forEach(e -> root.getChildren().add(e.getImageView()));
            this.scene.setRoot(root);
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showGameOver() {
        Platform.runLater(() -> {
            final Text text = new Text(450, this.stage.getHeight() / 2, "Game Over");
            text.setFont(new Font(FONTSIZE));
            final Scene gameOver = new Scene(new Group(text), this.stage.getWidth(), this.stage.getHeight(), Color.DARKRED);
            this.stage.setScene(gameOver);
        });
    }

    /**
     * {@inheritDoc}
     */
    public void showPlayerWin() {
        Platform.runLater(() -> {
            final Text text = new Text(450, this.stage.getHeight() / 2, "You won!"); 
            text.setFont(new Font(FONTSIZE));
            final Scene win = new Scene(new Group(text), this.stage.getHeight(), this.stage.getWidth(), Color.DARKRED);
            this.stage.setScene(win);
        });
    }

    /*
     * Finds the key's correspondent in InputKey which has a method that converts it into InputType,
     * which is passed to the gameController
     */
    private void getInput(final KeyCode key) {
        final Optional<InputKey> inputKey = Stream.of(InputKey.values()).filter(input -> input.name().equals(key.name())).findAny();
        if (inputKey.isPresent()) {
            gameController.processInput(inputKey.get().convert());
        }
    }
}
