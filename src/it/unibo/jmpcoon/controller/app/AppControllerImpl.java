package it.unibo.jmpcoon.controller.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Optional;

import it.unibo.jmpcoon.controller.SaveFile;
import it.unibo.jmpcoon.view.app.AppView;

/**
 * Class implementation of {@link AppController}.
 */
public class AppControllerImpl implements AppController {
    private static final String FOLDER = "jmpcoon";
    private static final String LOG_FILE = "jmpcoon.log";

    private final AppView view;

    /**
     * Builds a new {@link AppControllerImpl}.
     * @param view the {@link AppView} element responsible for the application
     */
    public AppControllerImpl(final AppView view) {
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
        try {
            System.setErr(new PrintStream(logPath.toFile()));
        } catch (final FileNotFoundException ex) {
            ex.printStackTrace();
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
    public void startGame(final Optional<Integer> saveFileIndex) {
        this.view.displayGame(saveFileIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Optional<Long>> getSaveFileAvailability() {
        final List<Optional<Long>> list = new LinkedList<>();
        Arrays.asList(SaveFile.values()).stream()
                                        .map(SaveFile::getSavePath)
                                        .map(File::new)
                                        .forEach(f -> list.add(f.exists() ? Optional.of(f.lastModified()) : Optional.absent()));
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteSaveFile(final int saveFileIndex) {
        return saveFileIndex >= 0
               && saveFileIndex < SaveFile.values().length
               && new File(SaveFile.values()[saveFileIndex].getSavePath()).delete();
    }
}
