package test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import com.google.common.base.Optional;

import model.entities.EntityType;
import model.entities.PowerUpType;
import model.physics.BodyShape;
import model.physics.DynamicPhysicalBody;
import model.physics.PhysicalBody;
import model.physics.PhysicalFactory;
import model.physics.PhysicalFactoryImpl;
import model.physics.PhysicalWorld;
import model.physics.StaticPhysicalBody;
import model.world.WorldFactoryImpl;
import model.world.WorldImpl;

/**
 * Test for the creation of {@link PhyisicalBody}.
 */
public class PhysicalBodyCreationTest {
    private static final double WORLD_WIDTH = 8;
    private static final double WORLD_HEIGHT = 4.5;
    private static final double STD_WIDTH = WORLD_WIDTH / 15;
    private static final double STD_HEIGHT = WORLD_HEIGHT / 15;
    private static final double STD_ANGLE = Math.PI / 6;
    private static final List<EntityType> STATIC_TYPES = Arrays.asList(EntityType.LADDER, 
                                                                       EntityType.ENEMY_GENERATOR,
                                                                       EntityType.POWERUP,
                                                                       EntityType.PLATFORM);
    private static final List<EntityType> DYNAMIC_TYPES = Arrays.asList(EntityType.ROLLING_ENEMY,
                                                                        EntityType.WALKING_ENEMY);
    private static final ImmutablePair<Double, Double> STD_POSITION = new ImmutablePair<>(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    private static final double STD_DIMENSION = WORLD_WIDTH / 8;
    private static final String SHOULD_BE_ADMISSIBLE_MSG = "This combination of EntityShape and EntityType should be admissible";
    private static final String SHOULD_NOT_BE_ADMISSIBLE_MSG = "This combination of EntityShape and EntityType should not be "
                                                               + "admissible";
    private static final String NOT_NULL_MSG = "The message regarding the exception thrown is not present";
    private static final String EXCEPTION_THROWN_MSG = "This exception shouldn't have been launched";
    private static final String NULL_BODY_MSG = "Instead of a PhysicalBody, null was returned";

    private final Map<EntityType, BodyShape> allowedCombinations;
 
    /**
     * Builds a new {@link PhysicalBodyCreationTest}.
     */
    public PhysicalBodyCreationTest() {
        this.allowedCombinations = new HashMap<>();
        this.allowedCombinations.put(EntityType.LADDER, BodyShape.RECTANGLE);
        this.allowedCombinations.put(EntityType.PLATFORM, BodyShape.RECTANGLE);
        this.allowedCombinations.put(EntityType.ENEMY_GENERATOR, BodyShape.CIRCLE);
        this.allowedCombinations.put(EntityType.POWERUP, BodyShape.RECTANGLE);
        this.allowedCombinations.put(EntityType.PLAYER, BodyShape.RECTANGLE);
        this.allowedCombinations.put(EntityType.ROLLING_ENEMY, BodyShape.CIRCLE);
        this.allowedCombinations.put(EntityType.WALKING_ENEMY, BodyShape.RECTANGLE);
    }

    /**
     * Test for the correct initialization of this {@link PhysicalBodyCreationTest}.
     */
    @Test
    public void correctClassInitialization() {
        final List<EntityType> consideredTypes = new LinkedList<>(STATIC_TYPES);
        consideredTypes.addAll(DYNAMIC_TYPES);
        consideredTypes.add(EntityType.PLAYER);
        assertTrue("Not all possible types have been considered", 
                   consideredTypes.containsAll(Arrays.asList(EntityType.values())));
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        try {
            this.createStandardPlatform(physicalFactory);
            this.createStandardLadder(physicalFactory);
            this.createStandardPlayer(physicalFactory);
            this.createStandardRollingEnemy(physicalFactory);
            this.createStandardWalkingEnemy(physicalFactory);
            this.createStandardEnemyGenerator(physicalFactory);
            this.createStandardPowerUp(physicalFactory);
        } catch (IllegalStateException | IllegalArgumentException e) {
            fail("No exception should have been launched");
        }
    }

    // TODO: PMD signals an error because assert() and fail() are inside a lambda
    /**
     * Test for the creation of rectangular {@link StaticPhysicalBody}.
     */
    @Test
    public void staticAllowedRectangularBodiesCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        STATIC_TYPES.forEach(t -> {
            if (this.allowedCombinations.get(t) == BodyShape.RECTANGLE) {
                try {
                    final StaticPhysicalBody body = physicalFactory.createStaticPhysicalBody(STD_POSITION, 
                                                                                             0,
                                                                                             BodyShape.RECTANGLE,
                                                                                             STD_WIDTH,
                                                                                             STD_HEIGHT,
                                                                                             t,
                                                                                             t == EntityType.POWERUP
                                                                                                 ? Optional.of(PowerUpType.GOAL)
                                                                                                 : Optional.absent());
                    assertNotNull(NULL_BODY_MSG, body);
                } catch (final IllegalArgumentException e) {
                    fail(SHOULD_BE_ADMISSIBLE_MSG);
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
            }
        });
    }

    // TODO: PMD signals an error because assert() and fail() are inside a lambda
    /**
     * Test for the creation of circular {@link StaticPhysicalBody}.
     */
    @Test
    public void staticNotAllowedCircularBodiesCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        STATIC_TYPES.forEach(t -> {
            if (this.allowedCombinations.get(t) == BodyShape.RECTANGLE) {
                try {
                    physicalFactory.createStaticPhysicalBody(STD_POSITION, 
                                                             0,
                                                             BodyShape.CIRCLE,
                                                             STD_DIMENSION,
                                                             STD_DIMENSION,
                                                             t,
                                                             t == EntityType.POWERUP
                                                                 ? Optional.of(PowerUpType.GOAL)
                                                                 : Optional.absent());
                    fail(SHOULD_NOT_BE_ADMISSIBLE_MSG);
                } catch (final IllegalArgumentException e) {
                    assertNotNull(NOT_NULL_MSG, e.getMessage());
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
            }
        });
    }

    // TODO: PMD signals an error because assert() and fail() are inside a lambda
    /**
     * Test for the creation of circular {@link StaticPhysicalBody}.
     */
    @Test
    public void staticAllowedCircularBodiesCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        STATIC_TYPES.forEach(t -> {
            if (this.allowedCombinations.get(t) == BodyShape.CIRCLE) {
                try {
                    final StaticPhysicalBody body = physicalFactory.createStaticPhysicalBody(STD_POSITION, 
                                                                                             0,
                                                                                             BodyShape.CIRCLE,
                                                                                             STD_DIMENSION,
                                                                                             STD_DIMENSION,
                                                                                             t,
                                                                                             t == EntityType.POWERUP
                                                                                                 ? Optional.of(PowerUpType.GOAL)
                                                                                                 : Optional.absent());
                    assertNotNull(NULL_BODY_MSG, body);
                } catch (final IllegalArgumentException e) {
                    fail(SHOULD_BE_ADMISSIBLE_MSG);
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
            }
        });
    }

    // TODO: PMD signals an error because assert() and fail() are inside a lambda
    /**
     * Test for the creation of circular {@link StaticPhysicalBody}.
     */
    @Test
    public void staticNotAllowedRectangularBodiesCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        STATIC_TYPES.forEach(t -> {
            if (this.allowedCombinations.get(t) == BodyShape.CIRCLE) {
                try {
                    physicalFactory.createStaticPhysicalBody(STD_POSITION, 
                                                             0,
                                                             BodyShape.RECTANGLE,
                                                             STD_WIDTH,
                                                             STD_HEIGHT,
                                                             t,
                                                             t == EntityType.POWERUP
                                                                 ? Optional.of(PowerUpType.GOAL)
                                                                 : Optional.absent());
                    fail(SHOULD_NOT_BE_ADMISSIBLE_MSG);
                } catch (final IllegalArgumentException e) {
                    assertNotNull(NOT_NULL_MSG, e.getMessage());
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
            }
        });
    }

    // TODO: PMD signals an error because assert() and fail() are inside a lambda
    /**
     * Test for the creation of rectangular {@link DynamicPhysicalBody}.
     */
    @Test
    public void dynamicAllowedRectangularBodiesCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        DYNAMIC_TYPES.forEach(t -> {
            if (this.allowedCombinations.get(t) == BodyShape.RECTANGLE) {
                try {
                    final DynamicPhysicalBody body = physicalFactory.createDynamicPhysicalBody(STD_POSITION, 
                                                                                               0,
                                                                                               BodyShape.RECTANGLE,
                                                                                               STD_WIDTH,
                                                                                               STD_HEIGHT,
                                                                                               t);
                    assertNotNull(NULL_BODY_MSG, body);
                } catch (final IllegalArgumentException e) {
                    fail(SHOULD_BE_ADMISSIBLE_MSG);
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
            }
        });
    }

    // TODO: PMD signals an error because assert() and fail() are inside a lambda
    /**
     * Test for the creation of circular {@link DynamicPhysicalBody}.
     */
    @Test
    public void dynamicNotAllowedCircularBodiesCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        DYNAMIC_TYPES.forEach(t -> {
            if (this.allowedCombinations.get(t) == BodyShape.RECTANGLE) {
                try {
                    physicalFactory.createDynamicPhysicalBody(STD_POSITION, 
                                                             0,
                                                             BodyShape.CIRCLE,
                                                             STD_DIMENSION,
                                                             STD_DIMENSION,
                                                             t);
                    fail(SHOULD_NOT_BE_ADMISSIBLE_MSG);
                } catch (final IllegalArgumentException e) {
                    assertNotNull(NOT_NULL_MSG, e.getMessage());
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
            }
        });
    }

    // TODO: PMD signals an error because assert() and fail() are inside a lambda
    /**
     * Test for the creation of circular {@link DynamicPhysicalBody}.
     */
    @Test
    public void dynamicAllowedCircularBodiesCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        DYNAMIC_TYPES.forEach(t -> {
            if (this.allowedCombinations.get(t) == BodyShape.CIRCLE) {
                try {
                    final DynamicPhysicalBody body = physicalFactory.createDynamicPhysicalBody(STD_POSITION, 
                                                                                             0,
                                                                                             BodyShape.CIRCLE,
                                                                                             STD_DIMENSION,
                                                                                             STD_DIMENSION,
                                                                                             t);
                    assertNotNull(NULL_BODY_MSG, body);
                } catch (final IllegalArgumentException e) {
                    fail(SHOULD_BE_ADMISSIBLE_MSG);
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
            }
        });
    }

    // TODO: PMD signals an error because assert() and fail() are inside a lambda
    /**
     * Test for the creation of circular {@link StaticPhysicalBody}.
     */
    @Test
    public void dynamicNotAllowedRectangularBodiesCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        DYNAMIC_TYPES.forEach(t -> {
            if (this.allowedCombinations.get(t) == BodyShape.CIRCLE) {
                try {
                    physicalFactory.createDynamicPhysicalBody(STD_POSITION, 
                                                             0,
                                                             BodyShape.RECTANGLE,
                                                             STD_WIDTH,
                                                             STD_HEIGHT,
                                                             t);
                    fail(SHOULD_NOT_BE_ADMISSIBLE_MSG);
                } catch (final IllegalArgumentException e) {
                    assertNotNull(NOT_NULL_MSG, e.getMessage());
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
            }
        });
    }

    /**
     * Test for the correct creation of a {@link PlayerPhysicalBody}.
     */
    @Test
    public void playerBodyCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        try {
            physicalFactory.createPlayerPhysicalBody(STD_POSITION, STD_ANGLE, BodyShape.RECTANGLE, STD_WIDTH, STD_HEIGHT);
        } catch (final IllegalArgumentException e) {
            fail(SHOULD_BE_ADMISSIBLE_MSG);
        }
        try {
            physicalFactory.createPlayerPhysicalBody(STD_POSITION, STD_ANGLE, BodyShape.CIRCLE, STD_WIDTH, STD_HEIGHT);
            fail(SHOULD_NOT_BE_ADMISSIBLE_MSG);
        } catch (final IllegalArgumentException e) {
            assertNotNull(NOT_NULL_MSG, e.getMessage());
        }
    }

    /**
     * Test for the correct creation of a circular {@link PhysicalBody}.
     */
    @Test
    public void circularBodyCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        try {
            physicalFactory.createDynamicPhysicalBody(STD_POSITION, STD_ANGLE, BodyShape.CIRCLE, 
                                                        STD_WIDTH, STD_WIDTH, EntityType.ROLLING_ENEMY);
        } catch (final IllegalArgumentException e) {
            fail("This exception shouldn't have been launched, width and height were the same");
        }
        try {
            physicalFactory.createDynamicPhysicalBody(STD_POSITION, STD_ANGLE, BodyShape.CIRCLE, 
                                                        STD_WIDTH, STD_HEIGHT, EntityType.ROLLING_ENEMY);
            fail("Width and height are not the same");
        } catch (final IllegalArgumentException e) {
            assertNotNull(NOT_NULL_MSG, e.getMessage());
        }
    }

    private PhysicalBody createStandardPlatform(final PhysicalFactory factory) {
        return factory.createStaticPhysicalBody(STD_POSITION,
                                                STD_ANGLE,
                                                BodyShape.RECTANGLE,
                                                STD_WIDTH,
                                                STD_HEIGHT,
                                                EntityType.PLATFORM,
                                                Optional.absent());
    }

    private PhysicalBody createStandardLadder(final PhysicalFactory factory) {
        return factory.createStaticPhysicalBody(STD_POSITION,
                                                0,
                                                BodyShape.RECTANGLE,
                                                STD_WIDTH,
                                                STD_HEIGHT,
                                                EntityType.LADDER,
                                                Optional.absent());
    }

    private PhysicalBody createStandardEnemyGenerator(final PhysicalFactory factory) {
        return factory.createStaticPhysicalBody(STD_POSITION,
                                                0,
                                                BodyShape.CIRCLE,
                                                STD_WIDTH,
                                                STD_WIDTH,
                                                EntityType.ENEMY_GENERATOR,
                                                Optional.absent());
    }

    private PhysicalBody createStandardPowerUp(final PhysicalFactory factory) {
        return factory.createStaticPhysicalBody(STD_POSITION,
                                                0,
                                                BodyShape.RECTANGLE,
                                                STD_WIDTH,
                                                STD_HEIGHT,
                                                EntityType.POWERUP,
                                                Optional.of(PowerUpType.EXTRA_LIFE));
    }

    private PhysicalBody createStandardPlayer(final PhysicalFactory factory) {
        return factory.createPlayerPhysicalBody(STD_POSITION,
                                                0,
                                                BodyShape.RECTANGLE,
                                                STD_WIDTH,
                                                STD_HEIGHT);
    }

    private PhysicalBody createStandardRollingEnemy(final PhysicalFactory factory) {
        return factory.createDynamicPhysicalBody(STD_POSITION,
                                                 0,
                                                 BodyShape.CIRCLE,
                                                 STD_WIDTH,
                                                 STD_WIDTH,
                                                 EntityType.ROLLING_ENEMY);
    }

    private PhysicalBody createStandardWalkingEnemy(final PhysicalFactory factory) {
        return factory.createDynamicPhysicalBody(STD_POSITION,
                                                 0,
                                                 BodyShape.RECTANGLE,
                                                 STD_WIDTH,
                                                 STD_HEIGHT,
                                                 EntityType.WALKING_ENEMY);
    }
}
