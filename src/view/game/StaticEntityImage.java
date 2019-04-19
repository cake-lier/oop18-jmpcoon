package view.game;

/**
 * An enumeration representing the sprites for a {@link PowerUp}.
 */
public enum StaticEntityImage {
    /**
     * Modular image for a {@link Ladder}.
     */
    LADDER("ladder.png"),
    /**
     * Modular image for a {@link Platform}.
     */
    PLATFORM("platform.png"),
    /**
     * Image for a {@link EnemyGenerator}.
     */
    ENEMY_GENERATOR("enemyGenerator.png");

    private static final String SPRITES_DIR = "images/";

    private final String imageUrl;

    StaticEntityImage(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Returns the url of the image associated to the {@link StaticEntity}.
     * @return the url of the image associated to the {@link StaticEntity}
     */
    public String getImageUrl() {
        return SPRITES_DIR + this.imageUrl;
    }
}
