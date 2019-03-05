package model;

/**
 * TODO: add a linear velocity movement
 * a walking enemy inside the {@link World} of the game.
 */
public class WalkingEnemy extends DynamicEntity{

    /**
     * builds a new {@link WalkingEnemy}.
     * @param body the {@link PhysicalBody} of this {@link WalkingEnemy}
     */
    public WalkingEnemy(final DynamicPhysicalBody body) {
        super(body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityShape getShape() {
        return EntityShape.RECTANGLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return EntityType.WALKING_ENEMY;
    }

}
