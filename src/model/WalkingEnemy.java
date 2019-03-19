package model;

/**
 * a walking enemy inside the {@link World} of the game.
 */
public class WalkingEnemy extends DynamicEntity {

    private static final double DISTANCE = 5;
    private static final double WALKING_SPEED = 4;
    private double initialPosition;
    private DynamicPhysicalBody body;

    /**
     * builds a new {@link WalkingEnemy}.
     * @param body the {@link PhysicalBody} of this {@link WalkingEnemy}
     */
    public WalkingEnemy(final DynamicPhysicalBody body) {
        super(body);
        this.body = body;
        this.initialPosition = this.body.getWorldPosition().x;
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
        double x = this.body.getWorldPosition().x;
        if (x <= this.initialPosition) {
            body.applyMovement(WALKING_SPEED, 0);
        }
        if (x >= this.initialPosition + DISTANCE) {
            body.applyMovement(-WALKING_SPEED, 0);
        }
    }

}
