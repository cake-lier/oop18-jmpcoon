package it.unibo.jmpcoon.view.game;

import it.unibo.jmpcoon.model.entities.EntityState;

/**
 * An enumeration representing the sprite sheets of the {@link it.unibo.jmpcoon.model.entities.RollingEnemy}.
 */
public enum RollingEnemyImage implements SpriteSheetGetter {
    /**
     * Image for an idle {@link model.entities.RollingEnemy}.
     */
    IDLE(EntityState.IDLE, "rollingEnemy.png", 1),
    /**
     * Image for a {@link model.entities.RollingEnemy} moving left.
     */
    MOVING_LEFT(EntityState.MOVING_LEFT, "rollingEnemy.png", 1),
    /**
     * Image for a {@link model.entities.RollingEnemy} moving left.
     */
    MOVING_RIGHT(EntityState.MOVING_RIGHT, "rollingEnemy.png", 1);

    private static final String SPRITES_DIR = "images/";

    private final EntityState state;
    private final String spriteSheetUrl;
    private final int framesNumber;

    RollingEnemyImage(final EntityState state, final String spriteSheetUrl, final int frames) {
        this.state = state;
        this.spriteSheetUrl = spriteSheetUrl;
        this.framesNumber = frames;
    }

    /**
     * Returns the {@link EntityState} the sprite sheet is associated to.
     * @return the {@link EntityState} the sprite sheet returned by {@link #getImageUrl()} is associated to.
     */
    public EntityState getAssociatedEntityState() {
        return this.state;
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
