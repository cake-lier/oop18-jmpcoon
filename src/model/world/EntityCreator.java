package model.world;

import org.apache.commons.lang3.tuple.Pair;

import model.entities.Entity;
import model.entities.EntityFactory;
import model.entities.EntityType;
import model.entities.Ladder;
import model.entities.Platform;
import model.entities.Player;
import model.entities.RollingEnemy;
import model.entities.WalkingEnemy;

/**
 * An enum collecting all the possible creators of all possible {@link Number}s. Its scope is package protected because it should
 * be used by the sole {@link World} which is the one who should create new {@link Number}s.
 */
enum EntityCreator {
    /**
     * A {@link Ladder} creator.
     */
    LADDER(Ladder.class),
    /**
     * A {@link Player} creator.
     */
    PLAYER(Player.class),
    /**
     * A {@link Platform} creator.
     */
    PLATFORM(Platform.class),
    /**
     * A {@link PowerUp} creator.
     */
    //POWERUP(PowerUp.class),
    /**
     * A {@link RollingEnemy} creator.
     */
    ROLLING_ENEMY(RollingEnemy.class),
    /**
     * A {@link WalkingEnemy} creator.
     */
    WALKING_ENEMY(WalkingEnemy.class);
    /**
     * A {@link GeneratorEnemy} creator.
     */
    //GENERATOR_ENEMY(GeneratorEnemy.class);

    private final Class<? extends Entity> associatedClass;

    /**
     * Creates a new element of this enum by registering the type, its associated class and its name.
     * @param <T> The type 
     * @param type The {@link EntityType} of the element.
     * @param associatedClass The class associated with the element.
     */
    <T extends Entity> EntityCreator(final Class<T> associatedClass) {
        this.associatedClass = associatedClass;
    }

    /**
     * Gets the class of the {@link Number} associated with this element.
     * @return The associated {@link Number} subtype.
     */
    public Class<? extends Entity> getAssociatedClass() {
        return this.associatedClass;
    }

    /**
      * Creates an instance of a specific subtype of {@link Number} given the {@link EntityFactory} from which creating it and
      * the parameters from which creating it.
      * @param factory The instance of {@link EntityFactory} from which create {@link Number}s.
      * @param type The {@link EntityType} of the {@link Number} being created.
      * @param shape The {@link EntityShape} of the {@link Number} being created.
      * @param xCoord The x coordinate of the {@link Number} being created in meters in world coordinates.
      * @param yCoord The y coordinate of the {@link Number} being created in meters in world coordinates.
      * @param width The width of the {@link Number} being created in meters.
      * @param height The height of the {@link Number} being created in meters.
      * @param angle The {@link EntityType} of the {@link Number} being created in radians from the x axis.
      * @return The {@link Number} created in this way.
      */
    public Entity create(final EntityFactory factory, final EntityType type, final Pair<Double, Double> position,
                         final double width, final double height, final double angle) {
        switch (type) {
            case LADDER:
                return factory.createLadder(position, width, height);
            case PLATFORM:
                return factory.createPlatform(position, width, height, angle);
            case WALKING_ENEMY:
                return factory.createWalkingEnemy(position, width, height);
            default:
                return null;
        }
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
        T createEntity(EntityFactory factory, EntityType type, Pair<Double, Double> position, double width, double height,
                       double angle);
    }
}
