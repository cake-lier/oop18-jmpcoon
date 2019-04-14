package model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import model.entities.State;
import model.serializable.SerializableBody;
import model.entities.MovementType;

/**
 * a class representing a {@link PhysicalBody} that can move (players and
 * enemies).
 */
public class DynamicPhysicalBody extends AbstractPhysicalBody {
    private static final long serialVersionUID = -3108712528358435170L;
    private static final double MAXVELOCITY_X = 1;
    private static final double MAXVELOCITY_Y = 0.5;
    private static final double CLIMB_DAMPING = 2;

    private double maxVelocityX = MAXVELOCITY_X;
    private double maxVelocityY = MAXVELOCITY_Y;

    private final SerializableBody body;
    private State currentState;

    /**
     * builds a new {@link DynamicPhysicalBody}.
     * @param body the {@link SerializableBody} encapsulated by this {@link DynamicPhysicalBody}
     */
    public DynamicPhysicalBody(final SerializableBody body) {
        super(body);
        this.body = body;
        this.currentState = State.IDLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State getState() {
        if (this.body.getLinearVelocity().equals(new Vector2(0, 0))) {
            return State.IDLE;
        }
        return this.currentState;
    }

    /**
     * Sets entity's {@link State} to idle.
     */
    public void setIdle() {
        this.currentState = State.IDLE;
        this.body.setGravityScale(1);
        this.body.setLinearDamping(Body.DEFAULT_LINEAR_DAMPING);
        this.body.setLinearVelocity(new Vector2(0, 0));
    }

    /**
     * Applies given movement to this {@link Body},
     * sets corresponding {@link State}, sets body's gravity to 0 if climbing.
     * @param movement The kind of movement
     * @param x The horizontal component of the movement
     * @param y The vertical component of the movement
     */
    public void applyMovement(final MovementType movement, final double x, final double y) {
        if ((this.currentState != State.CLIMBING_UP && this.currentState != State.CLIMBING_DOWN)
            && (movement == MovementType.CLIMB_UP || movement == MovementType.CLIMB_DOWN)) {
            this.body.setGravityScale(0);
            this.body.setLinearDamping(CLIMB_DAMPING);
            this.body.setLinearVelocity(0, 0);
        }
        this.currentState = movement.convert();
        this.body.applyImpulse(new Vector2(x, y));
        if (Math.abs(this.body.getLinearVelocity().x) > maxVelocityX) {
            this.body.setLinearVelocity(new Vector2(Math.signum(this.body.getLinearVelocity().x) * maxVelocityX, this.body.getLinearVelocity().y));
        }
        if (Math.abs(this.body.getLinearVelocity().y) > maxVelocityY
            && (movement == MovementType.CLIMB_DOWN || movement == MovementType.CLIMB_UP)) {
            this.body.setLinearVelocity(new Vector2(this.body.getLinearVelocity().x, Math.signum(this.body.getLinearVelocity().y) * maxVelocityY));
        }
    }

    /**
     * Modifies the maximum velocity of this {@link DynamicPhysicalBody} to a custom one.
     * @param multiplierX The multiplier for the horizontal maximum velocity
     * @param multiplierY The multiplier for the vertical maximum velocity
     */
    public void setMaxVelocity(final double multiplierX, final double multiplierY) {
        this.maxVelocityX = MAXVELOCITY_X * multiplierX;
        this.maxVelocityY = MAXVELOCITY_Y * multiplierY;
    }

}
