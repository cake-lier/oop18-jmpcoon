package model;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;

/**
 * a class representing a {@link PhysicalBody} that can move (players and
 * enemies).
 */
public class DynamicPhysicalBody extends AbstractPhysicalBody {
    private static final double MAXVELOCITY_X = 5, MAXVELOCITY_Y = 2;

    private Body body;
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State getState() {
        if (this.body.getLinearVelocity().equals(new Vector2(0, 0))) {
            return State.IDLE;
        } else {
        return currentState;
        }
    }

    /**
     * Applies given movement to this {@link Body}.
     * @param movement The kind of movement
     * @param x The horizontal component of the movement
     * @param y The vertical component of the movement
     */
    public void applyMovement(final MovementType movement, final double x, final double y) {
        this.currentState = movement.convert();
        this.body.applyImpulse(new Vector2(x, y));
        if (this.body.getLinearVelocity().x > MAXVELOCITY_X) {
            this.body.setLinearVelocity(new Vector2(MAXVELOCITY_X, this.body.getLinearVelocity().y));
        }
        if (this.body.getLinearVelocity().y > MAXVELOCITY_Y) {
            this.body.setLinearVelocity(new Vector2(this.body.getLinearVelocity().x, MAXVELOCITY_Y));
        }
    }
}
