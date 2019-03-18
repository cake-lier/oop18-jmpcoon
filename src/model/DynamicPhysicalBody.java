package model;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;

//TODO: how to compute movement
/**
 * a class representing a {@link PhysicalBody} that can move (players and
 * enemies).
 */
public class DynamicPhysicalBody extends AbstractPhysicalBody {

    private Vector2 movement = new Vector2(0, 0);
    private Body body;

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
        return (movement.y != 0) ? State.JUMPING
                : (movement.x > 0) ? State.MOVING_RIGHT
                        : (movement.x < 0) ? State.MOVING_LEFT : State.CLIMBING;
    }

    /**
     * @param movement
     *        the movement applied to the body as a vector
     */
    public void applyMovement(final Vector2 movement) {
        this.body.applyImpulse(movement);
        setMovement(movement);
    }

    /**
     * 
     * @return actual position of the body
     */
    public Vector2 getWorldPosition() {
        return body.getWorldCenter();
    }
    
    private void setMovement(final Vector2 movement) {
        this.movement.add(movement);
    }

}
