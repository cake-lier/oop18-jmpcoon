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
     * Starts the game. It can receive an integer index that represents the {@link SaveFile} or not. In the
     * latter case, it will start a new game.
     * @param saveFileIndex the index of the {@link SaveFile} from which to load the game, if present
     */
    void startGame(Optional<Integer> saveFileIndex);

    /**
     * Returns the availability of the save files.
     * @return a list of Optional. The nth element of the list is present if the nth possible save file exist, and it's the time
     * at which the file was last modified, it's not present if the nth possible file doesn't exist yet
     */
    List<Optional<Long>> getSaveFileAvailability();

    /**
     * Deletes a save file. 
     * @param saveFileIndex the index of the save file to delete
     * @return if the deletion was successful or not
     */
    boolean deleteSaveFile(int saveFileIndex);
}
