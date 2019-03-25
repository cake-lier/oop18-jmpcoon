package model;

/**
 * a ladder inside the {@link World} of the game.
 */
public class Ladder extends StaticEntity {

    /**
     * builds a new {@link Ladder}.
     * @param body the {@link PhysicalBody} of this {@link Ladder}
     */
    public Ladder(final StaticPhysicalBody body) {
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
