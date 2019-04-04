package model.physics;

import model.entities.State;
import model.serializable.SerializableBody;

/**
 * a class representing a {@link PhysicalBody} that can't be moved.
 */
public class StaticPhysicalBody extends AbstractPhysicalBody {

    private static final long serialVersionUID = 5964392941673826320L;

    /**
     * builds a new {@link StaticPhysicalBody}.
     * @param body the {@link SerializableBody} encapsulated by this {@link StaticPhysicalBody}
     */
    public StaticPhysicalBody(final SerializableBody body) {
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
