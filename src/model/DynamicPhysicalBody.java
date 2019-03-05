package model;

import org.jbox2d.dynamics.Body;
/**
 * a class representing a {@link PhysicalBody} that can move (players and enemies).
 */
public class DynamicPhysicalBody extends AbstractPhysicalBody {

    /**
     * builds a new {@link DynamicPhysicalBody}.
     * @param body the {@link Body} encapsulated by this {@link DynamicPhysicalBody}
     */
    public DynamicPhysicalBody(final Body body) {
        super(body);
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
