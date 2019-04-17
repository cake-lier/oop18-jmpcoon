package view.game;

/**
 * An enumeration representing the sprites for a {@link PowerUp}.
 */
public enum StaticEntityImage {
    /**
     * Modular image for a {@link Ladder}.
     */
    LADDER(getSpritesDir() + "ladder.png"),
    /**
     * Modular image for a {@link Platform}.
     */
    PLATFORM(getSpritesDir() + "platform.png"),
    /**
     * Image for a {@link PowerUpType#INVINCIBILITY}.
     */
    ENEMY_GENERATOR(getSpritesDir() + "enemyGenerator.png");

    private static String getSpritesDir() {
        return "images/";
    }

    private final String imageUrl;

    StaticEntityImage(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Returns the url of the image associated to the {@link StaticEntity}.
     * @return the url of the image associated to the {@link StaticEntity}
     */
    public String getImageUrl() {
        return this.imageUrl;
    }
}
