package view;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Pane;

/**
 * Utility class for styling graphical elements.
 */
public final class ViewUtils {
    private static final String TIME_FORMAT = "d MMMM yyyy HH:mm";

    private ViewUtils() {
    }

    /**
     * Sets the text of the passed {@link Labeled} to the value of time in milliseconds from 01/01/1970 00:00 passed
     * formatted following the Italian time format for time.
     * @param labeled the view element to which change the text into the passed time
     * @param time the time to use for creating text to which insert into the passed {@link Labeled}
     */
    public static void setTextToTime(final Labeled labeled, final long time) {
        labeled.setText(LocalDateTime.ofEpochSecond(time / 1000, 0, 
                                                    ZoneOffset.of(ZoneOffset.systemDefault()
                                                                            .getRules()
                                                                            .getOffset(Instant.now())
                                                                            .getId()))
                                     .format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
    }

    /**
     * Given two nodes, hides the first and shows the second. Useful in toggling visibility of nodes.
     * @param firstNode the node to hide
     * @param secondNode the node to show
     */
    public static void hideFirstNodeShowSecondNode(final Node firstNode, final Node secondNode) {
        firstNode.setVisible(false);
        secondNode.setVisible(true);
    }

    /**
     * Performs the loading of the FXML file which is located at the path received setting as controller the object
     * passed and in the end put the elements loaded into the root element specified.
     * @param resourcePath the path of the file in which the elements to draw are located
     * @param controller the controller to be set when loading the resource
     * @param root the root element in which to add the loaded element
     */
    public static void drawFromURL(final String resourcePath, final Object controller, final Pane root) {
        final FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource(resourcePath));
        loader.setController(controller);
        try {
            final Pane page = loader.load();
            page.setVisible(false);
            root.getChildren().add(page);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
