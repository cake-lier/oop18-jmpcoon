package model.entities;

import model.physics.DynamicPhysicalBody;

/**
 * a walking enemy inside the {@link model.world.World} of the game.
 */
public final class WalkingEnemy extends DynamicEntity {

    private static final long serialVersionUID = 5020187009003425168L;
    private static final double WALKING_SPEED = 1;
    private static final double DELTA = 0.055;

    private double count = 0;
    private final DynamicPhysicalBody body;

    /**
     * builds a new {@link WalkingEnemy}.
     * 
     * @param body the {@link model.physics.PhysicalBody} of this {@link WalkingEnemy}
     */
    public WalkingEnemy(final DynamicPhysicalBody body) {
        super(body);
        this.body = body;
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

    /**
     * computes the backward-and-forward movement.
     */
    public void computeMovement() {
        applyImpulse(direction());
    }

    private void applyImpulse(final boolean direction) {
        if (direction) {
            body.applyMovement(MovementType.MOVE_RIGHT, WALKING_SPEED, 0);
        } else {
            body.applyMovement(MovementType.MOVE_LEFT, -WALKING_SPEED, 0);
        }
    }

    private boolean direction() {
        return Math.sin(counter()) >= 0;
    }

    private double counter() {
        this.count = count + DELTA;
        return this.count;
    }

}

