package model.world;

/**
 * The class implementation of a {@link WorldFactory}.
 */
public final class WorldFactoryImpl implements WorldFactory {
    private static final String NO_TWO_WORLDS_MSG = "There should be only one instance of World";

    private boolean worldCreated;

    /**
     * Default constructor, checks if the factory has already been instantiated and in that case, throws an exception.
     * @throws IllegalStateException If this factory has already been instantiated.
     */
    public WorldFactoryImpl() throws IllegalStateException {
        this.worldCreated = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public World create() throws IllegalStateException {
        if (!this.worldCreated) {
            this.worldCreated = true;
            return new WorldImpl();
        }
        throw new IllegalStateException(NO_TWO_WORLDS_MSG);
    }

}
