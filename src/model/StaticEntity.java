package model;

/**
 * a class representing an immovable {@link Entity}.
 */
public abstract class StaticEntity extends AbstractEntity {

    /**
     * builds a new {@link StaticEntity}.
     * @param body the {@link PhysicalBody} of this {@link StaticEntity}
     */
    public StaticEntity(final PhysicalBody body) {
        super(body);
    }

    @Override
    public abstract EntityType getType();

    /**
     * {@inheritDoc}
     */
    @Override
    public State getState() {
        return State.IDLE;
    }

}
