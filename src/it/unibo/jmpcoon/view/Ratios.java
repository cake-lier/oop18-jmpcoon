package it.unibo.jmpcoon.view;

import javafx.scene.Node;

/**
 * An enumeration for collecting all values of node sizes throughout all nodes which compose the view of this application.
 */
public enum Ratios {
    /**
     * The height of a title expressed as the reciprocal of a ratio with the height of the window in which is displayed.
     */
    TITLE(105),
    /**
     * The height of a button in the main menu expressed as the reciprocal of a ratio with the height of the window in
     * which is displayed.
     */
    MAIN_BUTTONS(200),
    /**
     * The height of a button which goes back to a previous page expressed as the reciprocal of a ratio with the height
     * of the window in which is displayed.
     */
    BACK_BUTTONS(300),
    /**
     * The height of a button for loading a save expressed as the reciprocal of a ratio with the height of the window in
     * which is displayed.
     */
    LOAD_BUTTONS(135),
    /**
     * The height of a button for deleting a save expressed as the reciprocal of a ratio with the height of the window
     * in which is displayed.
     */
    DELETE_BUTTONS(250),
    /**
     * The height of a label expressed as the reciprocal of a ratio with the height of the window in which is displayed.
     */
    LABELS(175),
    /**
     * The height of a button for muting expressed as the reciprocal of a ratio with the height of the window in which is
     * displayed.
     */
    MUTE_TOGGLES(200),
    /**
     * The height of a button in the game menu expressed as the reciprocal of a ratio with the height of the window in
     * which is displayed.
     */
    GAME_MENU_BUTTONS(150),
    /**
     * The height of a button for saving the game in the game menu expressed as the reciprocal of a ratio with the height
     * of the window in which is displayed.
     */
    SAVE_BUTTONS(135),
    /**
     * The height of the score text at the upper left corner of the game screen expressed as the reciprocal of a ratio with
     * the height of the window in which is displayed.
     */
    SCORES(255),
    /**
     * The height of the ending message expressed as the reciprocal of a ratio with the height of the window in which is
     * displayed.
     */
    END_MESSAGES(40),
    /**
     * The height of a button at the ending screen expressed as the reciprocal of a ratio with the height of the window in
     * which is displayed.
     */
    END_BUTTONS(200);

    private static final String FONT_SIZE = "-fx-font-size: ";
    private static final String SIZE_UNIT = "em";

    private final double windowRatio;

    /*
     * Creates the enumeration value with the associated ratio to the window height.
     */
    Ratios(final double windowRatio) {
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
