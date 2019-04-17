package view.game;

/**
 * An enumeration representing the sprites for a {@link PowerUp}.
 */
public enum PowerUpImage {
    /**
     * Image for a {@link PowerUpType#GOAL}.
     */
    GOAL("goal.png"),
    /**
     * Image for a {@link PowerUpType#EXTRA_LIFE}.
     */
    EXTRA_LIFE("extra_life.png"),
    /**
     * Image for a {@link PowerUpType#INVINCIBILITY}.
     */
    INVINCIBILITY("invincibility.png");

    private static final String SPRITES_DIR = "images/";

    private final String imageUrl;

    PowerUpImage(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Returns the url of the image associated to the {@link PowerUpType}.
     * @return the url of the image associated to the {@link PowerUpType}
     */
    public String getImageUrl() {
        return SPRITES_DIR + this.imageUrl;
    }
}
