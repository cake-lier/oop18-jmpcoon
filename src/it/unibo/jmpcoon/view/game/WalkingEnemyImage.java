package it.unibo.jmpcoon.view.game;

/**
 * An enumeration representing the sprite sheets of the {@link it.unibo.jmpcoon.model.entities.WalkingEnemy}.
 */
public enum WalkingEnemyImage {
    /**
     * Image for an idle {@link model.entities.WalkingEnemy}.
     */
    IDLE("walkingEnemy_idle.png", 1),
    /**
     * Image for a {@link model.entities.WalkingEnemy} moving left.
     */
    MOVING_LEFT("walkingEnemy_walking.png", 3),
    /**
     * Image for a {@link model.entities.WalkingEnemy} moving left.
     */
    MOVING_RIGHT("walkingEnemy_walking.png", 3);

    private static final String SPRITES_DIR = "images/";

    private final String spriteSheetUrl;
    private final int framesNumber;

    WalkingEnemyImage(final String spriteSheetUrl, final int frames) {
        this.spriteSheetUrl = spriteSheetUrl;
        this.framesNumber = frames;
    }

    /**
     * Returns the URL of the sprite sheet associated to the {@link it.unibo.jmpcoon.model.entities.EntityState}.
     * @return the URL of the sprite sheet associated to the {@link it.unibo.jmpcoon.model.entities.EntityState}
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
