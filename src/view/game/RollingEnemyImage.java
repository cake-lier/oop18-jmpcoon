package view.game;

/**
 * An enumeration representing the sprite sheets of the {@link RollingEnemyView}.
 */
public enum RollingEnemyImage {
    /**
     * Image for an idle {@link RollingEnemy}.
     */
    IDLE(getSpritesDir() + "rollingEnemy.png", 1),
    /**
     * Image for a {@link RollingEnemy} moving left.
     */
    MOVING_LEFT(getSpritesDir() + "rollingEnemy.png", 1),
    /**
     * Image for a {@link RollingEnemy} moving left.
     */
    MOVING_RIGHT(getSpritesDir() + "rollingEnemy.png", 1);

    private static String getSpritesDir() {
        return "images/";
    }

    private final String spriteSheetUrl;
    private final int framesNumber;

    RollingEnemyImage(final String spriteSheetUrl, final int frames) {
        this.spriteSheetUrl = spriteSheetUrl;
        this.framesNumber = frames;
    }

    /**
     * Returns the url of the sprite sheet associated to the {@link model.entities.EntityState}.
     * @return the url of the sprite sheet associated to the {@link model.entities.EntityState}
     */
    public String getImageUrl() {
        return this.spriteSheetUrl;
    }

    /**
     * Returns the number of frames contained in the sprite sheet associated.
     * @return the number of frames contained in the sprite sheet associated 
     */
    public int getFramesNumber() {
        return this.framesNumber;
    }
}
