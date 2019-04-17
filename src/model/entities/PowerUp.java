package model.entities;

import model.physics.StaticPhysicalBody;

/**
 * A power-up.
 */
public final class PowerUp extends StaticEntity {
    private static final long serialVersionUID = -6954816914217174592L;

    private final PowerUpType powerUpType;

    /**
     * Creates a new {@link PowerUp} with the given {@link StaticPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link AbstractEntityBuilder} when creating a new instance of it and no one else.
     * @param body The {@link StaticPhysicalBody} that should be contained in this {@link PowerUp}.
     * @param powerUpType The {@link PowerUpType} of this {@link PowerUp}.
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
     * @return the {@link PowerUpType} of this {@link PowerUp}.
     */
    public PowerUpType getPowerUpType() {
        return this.powerUpType;
    }
}
