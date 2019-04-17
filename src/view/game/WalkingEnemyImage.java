package view.game;

/**
 * An enumeration representing the sprite sheets of the {@link WalkingEnemyView}.
 */
public enum WalkingEnemyImage {
    /**
     * Image for an idle {@link WalkingEnemy}.
     */
    IDLE(getSpritesDir() + "walkingEnemy_idle.png", 1),
    /**
     * Image for a {@link WalkingEnemy} moving left.
     */
    MOVING_LEFT(getSpritesDir() + "walkingEnemy_walking.png", 3),
    /**
     * Image for a {@link WalkingEnemy} moving left.
     */
    MOVING_RIGHT(getSpritesDir() + "walkingEnemy_walking.png", 3);

    private static String getSpritesDir() {
        return "images/";
    }

    private final String spriteSheetUrl;
    private final int framesNumber;

    WalkingEnemyImage(final String spriteSheetUrl, final int frames) {
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
