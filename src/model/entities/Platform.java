package model.entities;

import model.physics.StaticPhysicalBody;

/**
 * a platform inside the {@link World} of the game.
 */
public class Platform extends StaticEntity {

    /**
     * builds a new {@link Platform}.
     * @param body the {@link PhysicalBody} of this {@link Platform}
     */
    public Platform(final StaticPhysicalBody body) {
        super(body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return EntityType.PLATFORM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityShape getShape() {
        return EntityShape.RECTANGLE;
    }

}
