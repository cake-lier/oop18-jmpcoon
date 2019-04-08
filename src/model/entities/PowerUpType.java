package model.entities;

/**
 * The type of a {@link PowerUp}.
 */
public enum PowerUpType {
    /**
     * The end of level zone.
     */
    GOAL,
    /**
     * The {@link Player} can't take damage.
     */
    INVINCIBILITY,
    /**
     * Add an extra life to player.
     */
    EXTRA_LIFE;
}
