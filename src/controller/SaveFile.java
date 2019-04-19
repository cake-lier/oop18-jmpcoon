package controller;

/**
 * An enumeration representing the possible save files for the game.
 */
public enum SaveFile {
    /**
     * The first available save file.
     */
    SAVE_FILE_1("save1.sav"),
    /**
     * The second available save file.
     */
    SAVE_FILE_2("save2.sav"),
    /**
     * The third available save file.
     */
    SAVE_FILE_3("save3.sav");

    private final String savePath;

    SaveFile(final String savePath) {
        this.savePath = savePath;
    }

    /**
     * Returns the path of the save file.
     * @return the path of this saving file
     */
    public String getSavePath() {
        return System.getProperty("user.home") + System.getProperty("file.separator") 
               + "jmpcoon" + System.getProperty("file.separator") + this.savePath;
    }
}
