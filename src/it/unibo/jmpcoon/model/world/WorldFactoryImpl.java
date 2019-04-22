package it.unibo.jmpcoon.model.world;

/**
 * The class implementation of a {@link WorldFactory}.
 */
public final class WorldFactoryImpl implements WorldFactory {
    private static final String NO_TWO_WORLDS_MSG = "There should be only one instance of World";

    private boolean worldCreated;

    /**
     * Default constructor, builds a new {@link WorldFactoryImpl}.
     */
    public WorldFactoryImpl() {
        this.worldCreated = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatableWorld create() throws IllegalStateException {
        if (!this.worldCreated) {
            this.worldCreated = true;
            return new WorldImpl();
        }
        throw new IllegalStateException(NO_TWO_WORLDS_MSG);
    }
}
