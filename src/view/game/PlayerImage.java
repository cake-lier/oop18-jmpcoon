package view.game;

/**
 * An enumeration representing the sprite sheets of the {@link model.entities.Player}.
 */
public enum PlayerImage {
    /**
     * Image for a {@link model.entities.Player} climbing up.
     */
    CLIMBING_UP("raccoon_climb.png", 2),
    /**
     * Image for a {@link model.entities.Player} climbing down.
     */
    CLIMBING_DOWN("raccoon_climb.png", 2),
    /**
     * Image for an idle {@link model.entities.Player}.
     */
    IDLE("raccoon_idle.png", 1),
    /**
     * Image for a {@link model.entities.Player} jumping.
     */
    JUMPING("raccoon_jump.png", 4),
    /**
     * Image for a {@link model.entities.Player} moving left.
     */
    MOVING_LEFT("raccoon_walking.png", 2),
    /**
     * Image for a {@link model.entities.Player} moving left.
     */
    MOVING_RIGHT("raccoon_walking.png", 2);

    private static final String SPRITES_DIR = "images/";

    private final String spriteSheetUrl;
    private final int framesNumber;

    PlayerImage(final String spriteSheetUrl, final int frames) {
        this.spriteSheetUrl = spriteSheetUrl;
        this.framesNumber = frames;
    }

    /**
     * Returns the URL of the sprite sheet associated to the {@link model.entities.EntityState}.
     * @return the URL of the sprite sheet associated to the {@link model.entities.EntityState}
     */
    public String getImageUrl() {
        return SPRITES_DIR + this.spriteSheetUrl;
    }

    /**
     * Returns the number of frames contained in the sprite sheet associated.
     * @return the number of frames contained in the sprite sheet associated 
     */
    public int getFramesNumber() {
        return this.framesNumber;
    }
}
