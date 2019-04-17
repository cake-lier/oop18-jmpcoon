package view.game;

/**
 * An enumeration representing the sprite sheets of the {@link PlayerView}.
 */
public enum PlayerImage {
    /**
     * Image for a {@link Player} climbing up.
     */
    CLIMBING_UP(getSpritesDir() + "raccoon_climb.png", 2),
    /**
     * Image for a {@link Player} climbing down.
     */
    CLIMBING_DOWN(getSpritesDir() + "raccoon_climb.png", 2),
    /**
     * Image for an idle {@link Player}.
     */
    IDLE(getSpritesDir() + "raccoon_idle.png", 1),
    /**
     * Image for a {@link Player} jumping.
     */
    JUMPING(getSpritesDir() + "raccoon_jump.png", 6),
    /**
     * Image for a {@link Player} moving left.
     */
    MOVING_LEFT(getSpritesDir() + "raccoon_walking.png", 2),
    /**
     * Image for a {@link Player} moving left.
     */
    MOVING_RIGHT(getSpritesDir() + "raccoon_walking.png", 2);

    private static String getSpritesDir() {
        return "images/";
    }

    private final String spriteSheetUrl;
    private final int framesNumber;

    PlayerImage(final String spriteSheetUrl, final int frames) {
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
