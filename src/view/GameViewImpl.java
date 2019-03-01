package view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public void drawMenu(final Stage stage) throws IOException {
        final Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        final Parent root = FXMLLoader.load(this.getClass().getResource("layouts/menu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
