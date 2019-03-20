package model.entities;

/**
 * An enum collecting all the possible creators of all possible {@link Entity}s.
 */
public enum EntityCreator {
    /**
     * A {@link Ladder} creator.
     */
    LADDER(Ladder.class, factory -> factory.createLadder()),
    /**
     * A {@link Player} creator.
     */
    PLAYER(Player.class, factory -> factory.createPlayer()),
    /**
     * A {@link Platform} creator.
     */
    PLATFORM(Platform.class, factory -> factory.createPlatform()),
    /**
     * A {@link PowerUp} creator.
     */
    POWERUP(PowerUp.class, factory -> factory.createPowerUp()),
    /**
     * A {@link RollingEnemy} creator.
     */
    ROLLING_ENEMY(RollingEnemy.class, factory -> factory.createRollingEnemy()),
    /**
     * A {@link WalkingEnemy} creator.
     */
    WALKING_ENEMY(WalkingEnemy.class, factory -> factory.createWalkingEnemy());

    private final Class<? extends Entity> associatedClass;
    private final EntitySupplier<? extends Entity> supplier;

    /**
     * Creates a new element of this enum by registering the type, its associated class and its name.
     * @param <T> The type 
     * @param type The {@link EntityType} of the element.
     * @param associatedClass The class associated with the element.
     * @param supplier The method for creating more instances associated with this element.
     */
    <T extends Entity> EntityCreator(final Class<T> associatedClass, final EntitySupplier<T> supplier) {
        this.associatedClass = associatedClass;
        this.supplier = supplier;
    }

    /**
     * Gets the class of the {@link Entity} associated with this element.
     * @return The associated {@link Entity} subtype.
     */
    public Class<? extends Entity> getAssociatedClass() {
        return this.associatedClass;
    }

    /**
     * Creates an instance of the {@link Entity} associated with this element.
     * @param factory The {@link EntityFactory} from which produce an instance.
     * @return The instance created.
     */
    public Entity create(final EntityFactory factory) {
        return this.supplier.createEntity(factory);
    }

    /*
     * An interface used only in this enum for describing the type of a supplier of {@link Entity}s. It should create an instance
     * of the specified {@link Entity} providing the {@link EntityFactory} from which create it.
     */
    @FunctionalInterface
    private interface EntitySupplier<T extends Entity> {
        /*
         * Creates an instance of a specific subtype of {@link Entity} given the {@link EntityFactory} from which creating it and
         * then returns it.
         */
        T createEntity(EntityFactory factory);
    }
}
