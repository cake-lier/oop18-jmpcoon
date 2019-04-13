package controller.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import view.View;

/**
 * Class implementation of the {@link AppController}.
 */
public final class AppControllerImpl implements AppController {
    private static final String FOLDER = "jmpcoon";
    private static final String LOG_FILE = "jmpcoon.log";

    private final View view;

    /**
     * Acquires a new {@link View} to call when it's needed to display something.
     * @param view The view element responsible for the application.
     */
    public AppControllerImpl(final View view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startApp() {
        final String pathString = System.getProperty("user.home") + System.getProperty("file.separator") + FOLDER;
        final Path dataPath = Paths.get(pathString);
        final Path logPath = Paths.get(pathString + System.getProperty("file.separator") + LOG_FILE);
        if (Files.notExists(dataPath)) {
            try {
                Files.createDirectory(dataPath);
                Files.createFile(logPath);
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
/*        try {
            System.setErr(new PrintStream(logPath.toFile()));
        } catch (final FileNotFoundException ex) {
            ex.printStackTrace();
        }*/
        this.view.displayMenu();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exitApp() {
        System.exit(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame(final Optional<File> saveFile) {
        this.view.displayGame(saveFile);
    }
}
