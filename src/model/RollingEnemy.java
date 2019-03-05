package model.entities.d;

import model.enums.EntityShape;
import model.enums.EntityType;

/**
 * TODO: add a non-linear velocity movement
 * a walking enemy inside the {@link World} of the game.
 */
public class RollingEnemy extends DynamicEntity {

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
