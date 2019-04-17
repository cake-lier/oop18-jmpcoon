package model.entities;

import model.physics.DynamicPhysicalBody;

/**
 * a rolling enemy inside the {@link model.world.World} of the game.
 */
public final class RollingEnemy extends DynamicEntity {

    private static final long serialVersionUID = -6624661835399417683L;
    private static final double ROLLING_ENEMY_SPEED = 1;

    private final DynamicPhysicalBody body;

    /**
     * Creates a new {@link RollingEnemy} with the given {@link DynamicPhysicalBody}. This constructor is package protected because it
     * should be only invoked by the {@link AbstractEntityBuilder} when creating a new instance of it and no one else.
     * 
     * @param body The {@link DynamicPhysicalBody} that should be contained in this {@link RollingEnemy}.
     */
    RollingEnemy(final DynamicPhysicalBody body) {
        super(body);
        this.body = body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return EntityType.ROLLING_ENEMY;
    }

    /**
     * applies a fixed velocity to the {@link RollingEnemy}.
     */
    public void applyImpulse() {
        this.body.setFixedVelocity(MovementType.MOVE_RIGHT, ROLLING_ENEMY_SPEED, 0);
    }
}
