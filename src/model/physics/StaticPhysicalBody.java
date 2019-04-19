package model.physics;

import model.entities.EntityState;
import model.serializable.SerializableBody;

/**
 * A class representing a {@link PhysicalBody} that can't be moved.
 */
public class StaticPhysicalBody extends AbstractPhysicalBody {
    private static final long serialVersionUID = 5964392941673826320L;

    /**
     * Builds a new {@link StaticPhysicalBody}. This constructor is package protected because it should be only invoked 
     * by the {@link PhysicalFactoryImpl} when creating a new instance of it and no one else.
     * @param body the {@link SerializableBody} encapsulated by this {@link StaticPhysicalBody}
     */
    StaticPhysicalBody(final SerializableBody body) {
        super(body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityState getState() {
        return EntityState.IDLE;
    }
}
