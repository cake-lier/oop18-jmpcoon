package it.unibo.jmpcoon.model.entities;

import it.unibo.jmpcoon.model.physics.StaticPhysicalBody;

/**
 * A class representing an immovable {@link Entity}.
 */
public abstract class StaticEntity extends AbstractEntity {
    private static final long serialVersionUID = 9104210526680101510L;

    /**
     * Builds a new {@link StaticEntity}.
     * @param body the {@link it.unibo.jmpcoon.model.physics.PhysicalBody} of this {@link StaticEntity}
     */
    public StaticEntity(final StaticPhysicalBody body) {
        super(body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EntityType getType();
}
