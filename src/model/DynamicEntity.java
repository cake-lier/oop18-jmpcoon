package model;

import model.AbstractEntity;

/**
 * a class representing an movable {@link Entity}.
 */
public abstract class DynamicEntity extends AbstractEntity {

    /**
     * builds a new {@link DynamicEntity}.
     * @param body the {@link PhysicalBody} of this {@link DynamicEntity}
     */
    public DynamicEntity(final DynamicPhysicalBody body) {
        super(body);
    }
}
