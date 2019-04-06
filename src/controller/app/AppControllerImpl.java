package controller.app;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import view.View;

/**
 * Class implementation of the {@link AppController}.
 */
public final class AppControllerImpl implements AppController {
    private static final String SAVE_FOLDER = "saves";
    private static final String CUR_FOLDER = ".";

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
        if (ClassLoader.getSystemResource(SAVE_FOLDER) == null) {
            try {
                Files.createDirectory(Paths.get(ClassLoader.getSystemResource(CUR_FOLDER).getFile() + SAVE_FOLDER));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
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
    public void startGame(final Optional<URL> saveFile) {
        this.view.displayGame(saveFile);
    }
}
