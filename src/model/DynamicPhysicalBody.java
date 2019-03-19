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
    private static final double MAXVELOCITY = 5;

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
        //TODO
        return null;
    }

    /**
     * Applies given movement to current {@link Body}.
     * @param x The horizontal component of the movement
     * @param y The vertical component of the movement
     */
    public void applyMovement(final double x, final double y) {
        this.body.applyImpulse(new Vector2(x, y));
        if (this.body.getLinearVelocity().x > MAXVELOCITY) {
            this.body.setLinearVelocity(new Vector2(MAXVELOCITY, this.body.getLinearVelocity().y));
        }
    }

    /**
     * 
     * @return actual position of the body
     */
    public Vector2 getWorldPosition() {
        return body.getWorldCenter();
    }

}
