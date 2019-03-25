package model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

import model.entities.State;

/**
 * a class representing a {@link PhysicalBody} that can't be moved.
 */
public class StaticPhysicalBody extends AbstractPhysicalBody {

    /**
     * builds a new {@link StaticPhysicalBody}.
     * @param body the {@link Body} encapsulated by this {@link StaticPhysicalBody}
     * @param world the {@link World} in which the given {@link Body} lives
     */
    public StaticPhysicalBody(final Body body, final World world) {
        super(body, world);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State getState() {
        return State.IDLE;
    }

}
