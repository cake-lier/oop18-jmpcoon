package it.unibo.jmpcoon.view.game;

/**
 * An enumeration representing the sprites for a {@link it.unibo.jmpcoon.model.entities.PowerUp}.
 */
public enum PowerUpImage {
    /**
     * Image for a {@link model.entities.PowerUpType#GOAL}.
     */
    GOAL("goal.png"),
    /**
     * Image for a {@link model.entities.PowerUpType#EXTRA_LIFE}.
     */
    EXTRA_LIFE("extra_life.png"),
    /**
     * Image for a {@link model.entities.PowerUpType#INVINCIBILITY}.
     */
    INVINCIBILITY("invincibility.png");

    private static final String SPRITES_DIR = "images/";

    private final String imageUrl;

    PowerUpImage(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Returns the URL of the image associated to the {@link it.unibo.jmpcoon.model.entities.PowerUpType}.
     * @return the URL of the image associated to the {@link it.unibo.jmpcoon.model.entities.PowerUpType}
     */
    public String getImageUrl() {
        return SPRITES_DIR + this.imageUrl;
    }
}
