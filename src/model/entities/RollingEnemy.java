package model.entities;

import model.physics.DynamicPhysicalBody;

/**
 * TODO: add a non-linear velocity movement
 * a walking enemy inside the {@link World} of the game.
 */
public class RollingEnemy extends DynamicEntity {

    private static final long serialVersionUID = -6624661835399417683L;

    /**
     * builds a new {@link RollingEnemy}.
     * @param body the {@link PhysicalBody} of this {@link RollingEnemy}
     */
    public RollingEnemy(final DynamicPhysicalBody body) {
        super(body);
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityShape getShape() {
        return EntityShape.CIRCLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return EntityType.ROLLING_ENEMY;
    }

}
