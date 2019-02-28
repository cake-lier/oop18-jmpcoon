package view;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Class implementation of the {@link GameView} interface.
 */
public class GameViewImpl implements GameView {
    /**
     * {@inheritDoc}
     */
    @Override
    public void drawMenu(final Stage stage) {
        final Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());

        final Button startGame = new Button("Hi!");
        final Group root = new Group();
        root.getChildren().add(startGame);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
