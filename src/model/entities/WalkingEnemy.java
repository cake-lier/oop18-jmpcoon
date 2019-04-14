package model.entities;

import model.physics.DynamicPhysicalBody;

/**
 * a walking enemy inside the {@link model.world.World} of the game.
 */
public final class WalkingEnemy extends DynamicEntity {

    private static final long serialVersionUID = 5020187009003425168L;
    private static final double WALKING_SPEED = 0.4;
    private static final double DELTA = 150;

    private double count = 0;
    private boolean direction = false;
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
        if (this.count++ % DELTA == 0) {
            this.direction = !this.direction;
        }
        this.body.setFixedVelocity(this.getMovement(), this.getDirection() * WALKING_SPEED, 0);
    }

    private MovementType getMovement() {
        return this.direction ? MovementType.MOVE_RIGHT : MovementType.MOVE_LEFT;
    }

    private int getDirection() {
        return this.direction ? 1 : -1;
    }
}

