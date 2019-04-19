package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.ViewImpl;

/**
 * The main class which starts the whole application.
 */
public final class Main extends Application {

    /**
     * Main method which starts the application as requested by JavaFX.
     * @param args unused
     */
    public static void main(final String... args) {
        launch();
    }

    /**
     * The method used by JavaFX to start the application. It creates the View of this
     * application so as to manage its initial visualization.
     * @param stage the {@link Stage} to use for this application
     */
    @Override
    public void start(final Stage stage) throws Exception {
        new ViewImpl(stage);
    }
}
