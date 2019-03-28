package model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;

import model.entities.State;
import model.entities.MovementType;

/**
 * a class representing a {@link PhysicalBody} that can move (players and
 * enemies).
 */
public class DynamicPhysicalBody extends AbstractPhysicalBody {
    private static final double MAXVELOCITY_X = 5, MAXVELOCITY_Y = 2;

    private final Body body;
    private State currentState;

    /**
     * builds a new {@link DynamicPhysicalBody}.
     * 
     * @param body
     *            the {@link Body} encapsulated by this {@link DynamicPhysicalBody}
     * @param world
     *            the {@link World} in which the given {@link Body} lives
     */
    public DynamicPhysicalBody(final Body body, final World world) {
        super(body, world);
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
        } else {
            return this.currentState;
        }
    }

    /**
     * Sets entity's {@link State} to idle.
     */
    public void setIdle() {
        this.currentState = State.IDLE;
        this.body.setGravityScale(1);
        //TODO: set velocity to 0 might have to be removed
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
        this.currentState = movement.convert();
        if (this.currentState.equals(State.CLIMBING_UP) || this.currentState.equals(State.CLIMBING_DOWN)) {
            this.body.setGravityScale(0);
        }
        this.body.applyImpulse(new Vector2(x, y));
        if (Math.abs(this.body.getLinearVelocity().x) > MAXVELOCITY_X) {
            this.body.setLinearVelocity(new Vector2(Math.signum(this.body.getLinearVelocity().x) * MAXVELOCITY_X, this.body.getLinearVelocity().y));
        }
        if (Math.abs(this.body.getLinearVelocity().y) > MAXVELOCITY_Y) {
            this.body.setLinearVelocity(new Vector2(this.body.getLinearVelocity().x, Math.signum(this.body.getLinearVelocity().y) * MAXVELOCITY_Y));
        }
    }
}
