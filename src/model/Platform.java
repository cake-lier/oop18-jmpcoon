package model;

/**
 * a platform inside the {@link World} of the game.
 */
public class Platform extends StaticEntity {

    /**
     * builds a new {@link Platform}.
     * @param body the {@link PhysicalBody} of this {@link Platform}
     */
    public Platform(final PhysicalBody body) {
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
