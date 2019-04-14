package model.entities;

import model.physics.BodyShape;
import model.physics.DynamicPhysicalBody;

/**
 * a walking enemy inside the {@link model.world.World} of the game.
 */
public final class RollingEnemy extends DynamicEntity {

    private static final long serialVersionUID = -6624661835399417683L;
    private final DynamicPhysicalBody body;

    /**
     * Creates a new {@link RollingEnemy} with the given
     * {@link DynamicPhysicalBody}. This constructor is package protected because it
     * should be only invoked by the {@link EntityBuilder} when creating a new
     * instance of it and no one else.
     * 
     * @param body
     *            The {@link DynamicPhysicalBody} that should be contained in this
     *            {@link RollingEnemy}.
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

    //TODO: to be left or not?
    /**
     * appliese an impulse to the {@link RollingEnemy}.
     */
    public void applyImpulse() {
        this.body.setFixedVelocity(MovementType.MOVE_RIGHT, 2, 0);
    }

}
