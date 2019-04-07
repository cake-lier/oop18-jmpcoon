package model.entities;

import model.physics.StaticPhysicalBody;

/**
 * a platform inside the {@link World} of the game.
 */
public final class Platform extends StaticEntity {

    private static final long serialVersionUID = 2006372527364015609L;

    /**
     * Creates a new {@link Platform} with the given {@link StaticPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link EntityBuilder} when creating a new instance of it and no one else.
     * @param body The {@link StaticPhysicalBody} that should be contained in this {@link Platform}.
     */
    Platform(final StaticPhysicalBody body) {
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
