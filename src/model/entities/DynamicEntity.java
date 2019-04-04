package model.entities;

import model.physics.DynamicPhysicalBody;

/**
 * a class representing an movable {@link Entity}.
 */
public abstract class DynamicEntity extends AbstractEntity {

    private static final long serialVersionUID = 6589322572403884688L;

    /**
     * builds a new {@link DynamicEntity}.
     * @param body the {@link PhysicalBody} of this {@link DynamicEntity}
     */
    public DynamicEntity(final DynamicPhysicalBody body) {
        super(body);
    }
}
