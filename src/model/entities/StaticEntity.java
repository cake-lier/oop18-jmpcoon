package model.entities;

import model.physics.StaticPhysicalBody;

/**
 * a class representing an immovable {@link Entity}.
 */
public abstract class StaticEntity extends AbstractEntity {

    private static final long serialVersionUID = 9104210526680101510L;

    /**
     * builds a new {@link StaticEntity}.
     * @param body the {@link PhysicalBody} of this {@link StaticEntity}
     */
    public StaticEntity(final StaticPhysicalBody body) {
        super(body);
    }

    @Override
    public abstract EntityType getType();

}
