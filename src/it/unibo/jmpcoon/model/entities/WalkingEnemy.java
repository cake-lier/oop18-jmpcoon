package it.unibo.jmpcoon.model.entities;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import it.unibo.jmpcoon.model.physics.BodyShape;
import it.unibo.jmpcoon.model.physics.DynamicPhysicalBody;

/**
 * A walking enemy inside the {@link it.unibo.jmpcoon.model.world.World} of the game.
 */
public final class WalkingEnemy extends DynamicEntity {
    private static final long serialVersionUID = 5020187009003425168L;
    private static final double WALKING_SPEED = 0.4;

    private MovementType currentMovement;
    private final MutablePair<Double, Double> extremePosition;
    private final double walkingRange;
    private final DynamicPhysicalBody body;

    /**
     * Builds a new {@link WalkingEnemy}.
     * @param body the {@link it.unibo.jmpcoon.model.physics.PhysicalBody} of this {@link WalkingEnemy}
     * @param walkingRange the range this {@link WalkingEnemy} should walk across
     */
    public WalkingEnemy(final DynamicPhysicalBody body, final double walkingRange) {
        super(body);
        this.body = body;
        this.walkingRange = walkingRange;
        this.currentMovement = MovementType.MOVE_RIGHT;
        this.extremePosition = new MutablePair<>(this.body.getPosition().getLeft(), this.body.getPosition().getRight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BodyShape getShape() {
        return BodyShape.RECTANGLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return EntityType.WALKING_ENEMY;
    }

    /**
     * Computes the backward-and-forward movement.
     */
    public void computeMovement() {
        if (!this.checkDistanceFromExtreme()) {
            this.extremePosition.setLeft(this.body.getPosition().getLeft());
            this.extremePosition.setRight(this.body.getPosition().getRight());
            this.currentMovement = getOppositeMovement();
        }
        this.body.setFixedVelocity(this.currentMovement, this.getDelta() * WALKING_SPEED, 0);
    }

    private MovementType getOppositeMovement() {
        return this.currentMovement == MovementType.MOVE_RIGHT ? MovementType.MOVE_LEFT : MovementType.MOVE_RIGHT;
    }

    private int getDelta() {
        return this.currentMovement == MovementType.MOVE_RIGHT ? 1 : -1;
    }

    private boolean checkDistanceFromExtreme() {
        final Pair<Double, Double> actualPosition = this.body.getPosition();
        return Math.sqrt(Math.pow((actualPosition.getLeft() - this.extremePosition.getLeft()), 2)
                         + Math.pow((actualPosition.getRight() - this.extremePosition.getRight()), 2)) < this.walkingRange;
    }
}
