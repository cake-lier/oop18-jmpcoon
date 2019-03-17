package model;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

/**
 * a class representing a {@link PhysicalBody} that can move (players and enemies).
 */
public class DynamicPhysicalBody extends AbstractPhysicalBody {

    /**
     * builds a new {@link DynamicPhysicalBody}.
     * @param body the {@link Body} encapsulated by this {@link DynamicPhysicalBody}
     * @param world the {@link World} in which the given {@link Body} lives
     */
    public DynamicPhysicalBody(final Body body, final World world) {
        super(body, world);
    }

    //TODO: state defined from the class movement (?)
    /**
     * {@inheritDoc}
     */
    @Override
    public State getState() {
        return null;
    }

}
