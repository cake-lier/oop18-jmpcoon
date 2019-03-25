package model.entities;

import model.physics.StaticPhysicalBody;

/**
 * a class representing an immovable {@link Number}.
 */
public abstract class StaticEntity extends AbstractEntity {

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
