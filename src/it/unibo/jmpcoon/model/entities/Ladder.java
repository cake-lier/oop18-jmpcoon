package it.unibo.jmpcoon.model.entities;

import it.unibo.jmpcoon.model.physics.StaticPhysicalBody;

/**
 * A ladder inside the {@link it.unibo.jmpcoon.model.world.World} of the game.
 */
public class Ladder extends StaticEntity {
    private static final long serialVersionUID = -1338548627689639626L;

    /**
     * Creates a new {@link Ladder} with the given {@link StaticPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link AbstractEntityBuilder} when creating a new instance of it and no one else.
     * @param body the {@link StaticPhysicalBody} that should be contained in this {@link Ladder}
     */
    Ladder(final StaticPhysicalBody body) {
        super(body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return EntityType.LADDER;
    }
}
