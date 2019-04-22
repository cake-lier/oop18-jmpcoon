package view.game;

/**
 * An enumeration representing the sprites for a {@link model.entities.PowerUp}.
 */
public enum StaticEntityImage {
    /**
     * Modular image for a {@link model.entities.Ladder}.
     */
    LADDER("ladder.png"),
    /**
     * Modular image for a {@link model.entities.Platform}.
     */
    PLATFORM("platform.png"),
    /**
     * Image for a {@link model.entities.EnemyGenerator}.
     */
    ENEMY_GENERATOR("enemyGenerator.png");

    private static final String SPRITES_DIR = "images/";

    private final String imageUrl;

    StaticEntityImage(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Returns the URL of the image associated to the {@link model.entities.StaticEntity}.
     * @return the URL of the image associated to the {@link model.entities.StaticEntity}
     */
    public String getImageUrl() {
        return SPRITES_DIR + this.imageUrl;
    }
}
