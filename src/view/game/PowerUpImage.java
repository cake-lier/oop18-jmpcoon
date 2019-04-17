package view.game;

/**
 * An enumeration representing the sprites for a {@link PowerUp}.
 */
public enum PowerUpImage {
    /**
     * Image for a {@link PowerUpType#GOAL}.
     */
    GOAL(getSpritesDir() + "goal.png"),
    /**
     * Image for a {@link PowerUpType#EXTRA_LIFE}.
     */
    EXTRA_LIFE(getSpritesDir() + "extra_life.png"),
    /**
     * Image for a {@link PowerUpType#INVINCIBILITY}.
     */
    INVINCIBILITY(getSpritesDir() + "invincibility.png");

    private static String getSpritesDir() {
        return "images/";
    }

    private final String imageUrl;

    PowerUpImage(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Returns the url of the image associated to the {@link PowerUpType}.
     * @return the url of the image associated to the {@link PowerUpType}
     */
    public String getImageUrl() {
        return this.imageUrl;
    }
}
