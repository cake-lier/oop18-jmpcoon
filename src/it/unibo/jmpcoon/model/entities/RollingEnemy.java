package it.unibo.jmpcoon.model.entities;

import it.unibo.jmpcoon.model.physics.DynamicPhysicalBody;

/**
 * A rolling enemy inside the {@link it.unibo.jmpcoon.model.world.World} of the game.
 */
public final class RollingEnemy extends DynamicEntity {
    private static final long serialVersionUID = -6624661835399417683L;
    private static final double ROLLING_ENEMY_SPEED = 3;

    private final DynamicPhysicalBody body;

    /**
     * Creates a new {@link RollingEnemy} with the given {@link DynamicPhysicalBody}. This constructor is package protected 
     * because it should be only invoked by the {@link AbstractEntityBuilder} when creating a new instance of it and no one else.
     * @param body the {@link DynamicPhysicalBody} that should be contained in this {@link RollingEnemy}
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
     * Applies a fixed velocity to this {@link RollingEnemy}.
     */
    public void applyImpulse() {
        this.body.setFixedVelocity(MovementType.MOVE_RIGHT, ROLLING_ENEMY_SPEED, 0);
    }
}
