package model.entities;

import model.world.CollisionEvent;

/**
 * The type which a {@link PowerUp} can have.
 */
public enum PowerUpType {
    /**
     * The end of level zone.
     */
    GOAL(CollisionEvent.GOAL_HIT),
    /**
     * Add an extra life to the {@link Player}.
     */
    EXTRA_LIFE(CollisionEvent.POWER_UP_HIT),
    /**
     * The {@link Player} moves faster and destroys every enemy it touches for a short while.
     */
    INVINCIBILITY(CollisionEvent.INVINCIBILITY_HIT);

    private final CollisionEvent associatedEvent;

    /**
     * Creates the type of the power up while binding to it the {@link CollisionEvent} which the {@link model.world.World}
     * should receive when the {@link Player} hits it.
     * @param associatedEvent the {@link CollisionEvent} associated with this power up type
     */
    PowerUpType(final CollisionEvent associatedEvent) {
        this.associatedEvent = associatedEvent;
    }

    /**
     * Gets the {@link CollisionEvent} which is generated when the {@link Player} hits a power up with this type.
     * @return the {@link CollisionEvent} associated with this power up type
     */
    public CollisionEvent getAssociatedEvent() {
        return this.associatedEvent;
    }
}
