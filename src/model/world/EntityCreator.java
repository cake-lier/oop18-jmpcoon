package model.world;

import model.entities.Entity;
import model.entities.EntityFactory;
import model.entities.EntityShape;
import model.entities.EntityType;
import model.entities.GeneratorEnemy;
import model.entities.Ladder;
import model.entities.Platform;
import model.entities.Player;
import model.entities.PowerUp;
import model.entities.RollingEnemy;
import model.entities.WalkingEnemy;

/**
 * An enum collecting all the possible creators of all possible {@link Entity}s. Its scope is package protected because it should
 * be used by the sole {@link World} which is the one who should create new {@link Entity}s.
 */
enum EntityCreator {
    /**
     * A {@link Ladder} creator.
     */
    LADDER(Ladder.class, EntityFactory::createLadder),
    /**
     * A {@link Player} creator.
     */
    PLAYER(Player.class, EntityFactory::createPlayer),
    /**
     * A {@link Platform} creator.
     */
    PLATFORM(Platform.class, EntityFactory::createPlatform),
    /**
     * A {@link PowerUp} creator.
     */
    POWERUP(PowerUp.class, EntityFactory::createPowerUp),
    /**
     * A {@link RollingEnemy} creator.
     */
    ROLLING_ENEMY(RollingEnemy.class, EntityFactory::createRollingEnemy),
    /**
     * A {@link WalkingEnemy} creator.
     */
    WALKING_ENEMY(WalkingEnemy.class, EntityFactory::createWalkingEnemy),
    /**
     * A {@link GeneratorEnemy} creator.
     */
    GENERATOR_ENEMY(GeneratorEnemy.class, EntityFactory::createGeneratorEnemy);

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
      * Creates an instance of a specific subtype of {@link Entity} given the {@link EntityFactory} from which creating it and
      * the parameters from which creating it.
      * @param factory The instance of {@link EntityFactory} from which create {@link Entity}s.
      * @param type The {@link EntityType} of the {@link Entity} being created.
      * @param shape The {@link EntityShape} of the {@link Entity} being created.
      * @param xCoord The x coordinate of the {@link Entity} being created in meters in world coordinates.
      * @param yCoord The y coordinate of the {@link Entity} being created in meters in world coordinates.
      * @param width The width of the {@link Entity} being created in meters.
      * @param height The height of the {@link Entity} being created in meters.
      * @param angle The {@link EntityType} of the {@link Entity} being created in radians from the x axis.
      * @return The {@link Entity} created in this way.
      */
    public Entity create(final EntityFactory factory, final EntityType type, final EntityShape shape, final double xCoord, 
                         final double yCoord, final double width, final double height, final double angle) {
        return this.supplier.createEntity(factory, type, shape, xCoord, yCoord, width, height, angle);
    }

    /*
     * An interface used only in this enum for describing the type of a supplier of entities. It should create an instance
     * of the specified Entity provided the EntityFactory from which creating it.
     */
    @FunctionalInterface
    private interface EntitySupplier<T extends Entity> {
        /*
         * Creates an instance of a specific subtype of Entity given the EntityFactory and the parameters from which creating it.
         */
        T createEntity(EntityFactory factory, EntityType type, EntityShape shape, double xCoord, double yCoord, double width,
                       double height, double angle);
    }
}
