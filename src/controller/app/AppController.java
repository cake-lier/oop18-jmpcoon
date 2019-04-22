package controller.app;

import java.util.List;

import com.google.common.base.Optional;

/**
 * Interface modeling the controller for the application: it should correctly start the application and manage it once started,
 * so it should stop it and start the game when asked to do so.
 */
public interface AppController {
    /**
     * Starts the application.
     */
    void startApp();

    /**
     * Exits the application.
     */
    void exitApp();

    /**
     * Starts the game. It can receive an integer index that represents the {@link controller.SaveFile} or not. In the
     * latter case, it will start a new game.
     * @param saveFileIndex the index of the {@link controller.SaveFile} from which to load the game, if present
     */
    void startGame(Optional<Integer> saveFileIndex);

    /**
     * Produces a list which represents the files which hold the saves. Every item in the list is present if the
     * file of corresponding index is present on disk. An item in the list is the time at which the file was last modified
     * in milliseconds from 01/01/1970 at 00:00.
     * @return a list of {@link Optional}s, representing the possible save files. If the file is on disk, the {@link Optional}
     * holds its time of last modification, otherwise it holds an {@link Optional#absent()}
     */
    List<Optional<Long>> getSaveFileAvailability();

    /**
     * Deletes a save file given its index.
     * @param saveFileIndex the index of the save file to delete
     * @return true if the deletion was successful, false otherwise
     */
    boolean deleteSaveFile(int saveFileIndex);
}
