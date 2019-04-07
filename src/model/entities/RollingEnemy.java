package model.entities;

import model.physics.DynamicPhysicalBody;

/**
 * TODO: add a non-linear velocity movement
 * a walking enemy inside the {@link World} of the game.
 */
public final class RollingEnemy extends DynamicEntity {

    private static final long serialVersionUID = -6624661835399417683L;

    /**
     * Creates a new {@link RollingEnemy} with the given {@link DynamicPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link EntityBuilder} when creating a new instance of it and no one else.
     * @param body The {@link DynamicPhysicalBody} that should be contained in this {@link RollingEnemy}.
     */
    RollingEnemy(final DynamicPhysicalBody body) {
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
