package it.unibo.jmpcoon.model.entities;

import it.unibo.jmpcoon.model.physics.DynamicPhysicalBody;

/**
 * A class representing a movable {@link it.unibo.jmpcoon.model.entities.Entity}.
 */
public abstract class DynamicEntity extends AbstractEntity {
    private static final long serialVersionUID = 6589322572403884688L;

    /**
     * Builds a new {@link DynamicEntity}.
     * @param body the {@link it.unibo.jmpcoon.model.physics.PhysicalBody} of this {@link DynamicEntity}
     */
    public DynamicEntity(final DynamicPhysicalBody body) {
        super(body);
    }
}
