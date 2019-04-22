package it.unibo.jmpcoon.model.entities;

import it.unibo.jmpcoon.model.physics.StaticPhysicalBody;

/**
 * A platform inside the {@link it.unibo.jmpcoon.model.world.World} of the game.
 */
public final class Platform extends StaticEntity {
    private static final long serialVersionUID = 2006372527364015609L;

    /**
     * Creates a new {@link Platform} with the given {@link StaticPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link AbstractEntityBuilder} when creating a new instance of it and no one else.
     * @param body the {@link StaticPhysicalBody} that should be contained in this {@link Platform}
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
}
