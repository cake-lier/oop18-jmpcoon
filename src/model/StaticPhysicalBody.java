package model;

import org.jbox2d.dynamics.Body;

/**
 * a class representing a {@link PhysicalBody} that can't be moved.
 */
public class StaticPhysicalBody extends AbstractPhysicalBody {

    /**
     * builds a new {@link StaticPhysicalBody}.
     * @param body the {@link Body} encapsulated by this {@link StaticPhysicalBody}
     */
    public StaticPhysicalBody(final Body body) {
        super(body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State getState() {
        return State.IDLE;
    }

}
