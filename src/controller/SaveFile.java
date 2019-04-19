package controller;

/**
 * An enumeration representing the possible save files for the game.
 */
public enum SaveFile {
    /**
     * The first available save file.
     */
    SAVE_FILE_1(1),
    /**
     * The second available save file.
     */
    SAVE_FILE_2(2),
    /**
     * The third available save file.
     */
    SAVE_FILE_3(3);

    private static final String FOLDER = "jmpcoon";
    private static final String FILE_NAME = "save";
    private static final String EXTENSION = ".sav";

    private final int index;

    /*
     * Creates an enumeration value from the index of the save file the value is associated to.
     */
    SaveFile(final int index) {
        this.index = index;
    }

    /**
     * Gets the path to the save file the value of this enumeration is associated to.
     * @return a string representing the path of the saved file the value of this enumeration is associated to.
     */
    public String getSavePath() {
        return getSaveFilesPrefix() + this.index + EXTENSION;
    }

    /*
     * Produces the prefix in the path common to all saves files.
     */
    private String getSaveFilesPrefix() {
        return System.getProperty("user.home") + System.getProperty("file.separator")  + FOLDER
               + System.getProperty("file.separator") + FILE_NAME;
    }
}
