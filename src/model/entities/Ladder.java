package model.entities;

import model.physics.StaticPhysicalBody;

/**
 * a ladder inside the {@link World} of the game.
 */
public final class Ladder extends StaticEntity {

    private static final long serialVersionUID = -1338548627689639626L;

    /**
     * Creates a new {@link Ladder} with the given {@link StaticPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link EntityBuilder} when creating a new instance of it and no one else.
     * @param body The {@link StaticPhysicalBody} that should be contained in this {@link Ladder}.
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

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityShape getShape() {
        return EntityShape.RECTANGLE;
    }

}
