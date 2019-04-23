package it.unibo.jmpcoon.view.game;

import it.unibo.jmpcoon.model.entities.PowerUpType;

/**
 * An enumeration representing the sprites for a {@link it.unibo.jmpcoon.model.entities.PowerUp}.
 */
public enum PowerUpImage implements ImageUrlGetter {
    /**
     * Image for a {@link model.entities.PowerUpType#GOAL}.
     */
    GOAL(PowerUpType.GOAL, "goal.png"),
    /**
     * Image for a {@link model.entities.PowerUpType#EXTRA_LIFE}.
     */
    EXTRA_LIFE(PowerUpType.EXTRA_LIFE, "extra_life.png"),
    /**
     * Image for a {@link model.entities.PowerUpType#INVINCIBILITY}.
     */
    INVINCIBILITY(PowerUpType.INVINCIBILITY, "invincibility.png");

    private static final String SPRITES_DIR = "images/";

    private final PowerUpType powerUpType;
    private final String imageUrl;

    PowerUpImage(final PowerUpType powerUpType, final String imageUrl) {
        this.powerUpType = powerUpType;
        this.imageUrl = imageUrl;
    }

    /**
     * Returns the {@link PowerUpType} associated to the sprite returned by {@link #getImageUrl()}.
     * @return the {@link PowerUpType} associated to this sprite
     */
    public PowerUpType getAssociatedPowerUpType() {
        return this.powerUpType;
    }

    /**
     * Returns the URL of the image associated to the {@link it.unibo.jmpcoon.model.entities.PowerUpType}.
     * @return the URL of the image associated to the {@link it.unibo.jmpcoon.model.entities.PowerUpType}
     */
    public String getImageUrl() {
        return SPRITES_DIR + this.imageUrl;
    }
}
