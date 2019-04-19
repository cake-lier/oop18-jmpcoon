package view;

import javafx.scene.Node;

/**
 * An enumeration for collecting all values of node sizes throughout all nodes which compose the view of this application.
 */
public enum Sizes {
    /**
     * The height of a title expressed as the reciprocal of a ratio with the height of the window in which is displayed.
     */
    TITLE_RATIO(105),
    /**
     * The height of a button in the main menu expressed as the reciprocal of a ratio with the height of the window in
     * which is displayed.
     */
    MAIN_BUTTONS_RATIO(200),
    /**
     * The height of a button which goes back to a previous page expressed as the reciprocal of a ratio with the height
     * of the window in which is displayed.
     */
    BACK_BUTTONS_RATIO(300),
    /**
     * The height of a button for loading a save expressed as the reciprocal of a ratio with the height of the window in
     * which is displayed.
     */
    LOAD_BUTTONS_RATIO(135),
    /**
     * The height of a button for deleting a save expressed as the reciprocal of a ratio with the height of the window
     * in which is displayed.
     */
    DELETE_BUTTONS_RATIO(250),
    /**
     * The height of a label expressed as the reciprocal of a ratio with the height of the window in which is displayed.
     */
    LABELS_RATIO(175),
    /**
     * The height of a button for muting expressed as the reciprocal of a ratio with the height of the window in which is
     * displayed.
     */
    MUTE_RATIO(200),
    /**
     * The height of a button in the game menu expressed as the reciprocal of a ratio with the height of the window in
     * which is displayed.
     */
    GAME_MENU_BUTTONS_RATIO(150),
    /**
     * The height of a button for saving the game in the game menu expressed as the reciprocal of a ratio with the height
     * of the window in which is displayed.
     */
    SAVE_BUTTONS_RATIO(135);

    private static final String FONT_SIZE = "-fx-font-size: ";
    private static final String SIZE_UNIT = "em";

    private final double windowRatio;

    /*
     * Creates the enumeration value with 
     */
    Sizes(final double windowRatio) {
        this.windowRatio = windowRatio;
    }

    /**
     * Styles the passed node by fixing its font size as a ratio of the passed height of the window, so as to make this
     * view totally scalable with respect to the screen dimensions.
     * @param windowHeight the height of the current window
     * @param node the node to style
     */
    public void styleNodeToRatio(final double windowHeight, final Node node) {
        node.setStyle(FONT_SIZE + windowHeight / this.windowRatio + SIZE_UNIT);
    }
}
