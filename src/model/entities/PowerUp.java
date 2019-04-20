package model.entities;

import model.physics.StaticPhysicalBody;

/**
 * A class representing a PowerUp.
 */
public final class PowerUp extends StaticEntity {
    private static final long serialVersionUID = -6954816914217174592L;

    private final PowerUpType powerUpType;

    /**
     * Creates a new {@link PowerUp} with the given {@link StaticPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link AbstractEntityBuilder} when creating a new instance of it and no one else.
     * @param body the {@link StaticPhysicalBody} that should be contained in this {@link PowerUp}
     * @param powerUpType the {@link PowerUpType} of this {@link PowerUp}
     */
    PowerUp(final StaticPhysicalBody body, final PowerUpType powerUpType) {
        super(body);
        this.powerUpType = powerUpType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return EntityType.POWERUP;
    }

    /**
     * Returns the {@link PowerUpType} of this PowerUp.
     * @return the {@link PowerUpType} of this {@link PowerUp}
     */
    public PowerUpType getPowerUpType() {
        return this.powerUpType;
    }
}
