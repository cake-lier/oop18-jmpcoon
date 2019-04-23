package it.unibo.jmpcoon.view.game;

import it.unibo.jmpcoon.model.entities.EntityType;

/**
 * An enumeration representing the sprites for a {@link it.unibo.jmpcoon.model.entities.PowerUp}.
 */
public enum StaticEntityImage implements ImageUrlGetter {
    /**
     * Modular image for a {@link model.entities.Ladder}.
     */
    LADDER(EntityType.LADDER, "ladder.png"),
    /**
     * Modular image for a {@link model.entities.Platform}.
     */
    PLATFORM(EntityType.PLATFORM, "platform.png"),
    /**
     * Image for a {@link model.entities.EnemyGenerator}.
     */
    ENEMY_GENERATOR(EntityType.ENEMY_GENERATOR, "enemyGenerator.png");

    private static final String SPRITES_DIR = "images/";

    private final EntityType entityType;
    private final String imageUrl;

    StaticEntityImage(final EntityType type, final String imageUrl) {
        this.entityType = type;
        this.imageUrl = imageUrl;
    }

    /**
     * Returns the {@link EntityType} associated to the sprite returned by {@link #getImageUrl()}.
     * @return the {@link EntityType} associated to the sprite
     */
    public EntityType getAssociatedEntityType() {
        return this.entityType;
    }

    /**
     * Returns the URL of the image associated to the {@link it.unibo.jmpcoon.model.entities.StaticEntity}.
     * @return the URL of the image associated to the {@link it.unibo.jmpcoon.model.entities.StaticEntity}
     */
    public String getImageUrl() {
        return SPRITES_DIR + this.imageUrl;
    }
}
