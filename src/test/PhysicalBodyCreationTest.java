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
import model.physics.PhysicalBody;
import model.physics.PhysicalFactory;
import model.physics.PhysicalFactoryImpl;
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
    private static final List<EntityType> DYNAMIC_TYPES = Arrays.asList(EntityType.PLAYER,
                                                                        EntityType.ROLLING_ENEMY,
                                                                        EntityType.WALKING_ENEMY);
    private static final ImmutablePair<Double, Double> STD_POSITION = new ImmutablePair<>(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    private static final double STD_DIMENSION = WORLD_WIDTH / 8;
    private static final String NOT_ALLOWED_MSG = "The creation of the body shouldn't have been allowed";
    private static final String NOT_NULL_MSG = "The message regarding the exception thrown is not present";
    private static final String EXCEPTION_THROWN_MSG = "This exception shouldn't have been launched";

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

    /**
     * Test for {@link World} creation.
     */
    @Test
    public void worldCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        /* the world has yet to be created */
        try {
            createStandardPlatform(physicalFactory);
            fail(NOT_ALLOWED_MSG);
        } catch (final IllegalStateException e) {
            assertNotNull(NOT_NULL_MSG, e.getMessage());
        } catch (final IllegalArgumentException e1) {
            fail("This exception shouldn't have been thrown");
        }
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        /* the world has been created */
        try {
            createStandardPlatform(physicalFactory);
        } catch (final IllegalStateException e) {
            fail("The creation of the body should have been allowed");
        } catch (final IllegalArgumentException e1) {
            fail("This exception shouldn't have been thrown");
        }
        try {
            physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
            fail("The creation of more than one world shouldn't have been allowed");
        } catch (final IllegalStateException e) {
            assertNotNull(NOT_NULL_MSG, e.getMessage());
        } catch (final IllegalArgumentException e1) {
            fail("This exception shouldn't have been thrown");
        }
    }

    /**
     * Test for the creation of {@link StaticPhysicalBody}.
     */
    @Test
    public void staticBodiesCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        STATIC_TYPES.forEach(t -> {
            if (this.allowedCombinations.get(t) == BodyShape.RECTANGLE) {
                try {
                    physicalFactory.createStaticPhysicalBody(STD_POSITION, 
                                                                0,
                                                                BodyShape.RECTANGLE,
                                                                STD_DIMENSION,
                                                                STD_DIMENSION,
                                                                t,
                                                                t == EntityType.POWERUP
                                                                    ? Optional.of(PowerUpType.GOAL)
                                                                    : Optional.absent());
                } catch (final IllegalArgumentException e) {
                    fail("This combination of EntityShape and EntityType should be admissible for static bodies");
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
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
                    fail("This combination of EntityShape and EntityType should not be admissible for static bodies");
                } catch (final IllegalArgumentException e) {
                    assertNotNull(NOT_NULL_MSG, e.getMessage());
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
            } else {
                /* the shape is circular */
                try {
                    physicalFactory.createStaticPhysicalBody(STD_POSITION, 
                                                                0,
                                                                BodyShape.RECTANGLE,
                                                                STD_DIMENSION,
                                                                STD_DIMENSION,
                                                                t,
                                                                t == EntityType.POWERUP
                                                                    ? Optional.of(PowerUpType.GOAL)
                                                                    : Optional.absent());
                    fail("This combination of EntityShape and EntityType shouldn't be admissible for static bodies");
                } catch (final IllegalArgumentException e) {
                    assertNotNull(NOT_NULL_MSG, e.getMessage());
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
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
                } catch (final IllegalArgumentException e) {
                    fail("This combination of EntityShape and EntityType should be admissible for static bodies");
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
            }
        });
    }

    /**
     * Test for the creation of {@link DynamicPhysicalBody}.
     */
    @Test
    public void dynamicBodiesCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        DYNAMIC_TYPES.forEach(t -> {
            if (this.allowedCombinations.get(t) == BodyShape.RECTANGLE) {
                try {
                    physicalFactory.createDynamicPhysicalBody(STD_POSITION, 
                                                                0,
                                                                BodyShape.RECTANGLE,
                                                                STD_DIMENSION,
                                                                STD_DIMENSION,
                                                                t);
                } catch (final IllegalArgumentException e) {
                    fail("This combination of EntityShape and EntityType should be admissible for dynamic bodies");
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
                try {
                    physicalFactory.createDynamicPhysicalBody(STD_POSITION, 
                                                                0,
                                                                BodyShape.CIRCLE,
                                                                STD_DIMENSION,
                                                                STD_DIMENSION,
                                                                t);
                    fail("This combination of EntityShape and EntityType should not be admissible for dynamic bodies");
                } catch (final IllegalArgumentException e) {
                    assertNotNull(NOT_NULL_MSG, e.getMessage());
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
            } else {
                /* the shape is circular */
                try {
                    physicalFactory.createDynamicPhysicalBody(STD_POSITION, 
                                                                0,
                                                                BodyShape.RECTANGLE,
                                                                STD_DIMENSION,
                                                                STD_DIMENSION,
                                                                t);
                    fail("This combination of EntityShape and EntityType shouldn't be admissible for dynamic bodies");
                } catch (final IllegalArgumentException e) {
                    assertNotNull(NOT_NULL_MSG, e.getMessage());
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
                try {
                    physicalFactory.createDynamicPhysicalBody(STD_POSITION, 
                                                                0,
                                                                BodyShape.CIRCLE,
                                                                STD_DIMENSION,
                                                                STD_DIMENSION,
                                                                t);
                } catch (final IllegalArgumentException e) {
                    fail("This combination of EntityShape and EntityType should be admissible for dynamic bodies");
                } catch (final IllegalStateException e1) {
                    fail(EXCEPTION_THROWN_MSG);
                }
            }
        });
    }

    /**
     * test for the creation of {@link PhysicalBody} outside the bounds of the {@link PhysicalWorld}.
     */
    @Test
    public void positionTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        try {
            physicalFactory.createStaticPhysicalBody(
                    new ImmutablePair<Double, Double>(WORLD_WIDTH + STD_DIMENSION, WORLD_HEIGHT + STD_DIMENSION),
                    0,
                    this.allowedCombinations.get(EntityType.LADDER),
                    STD_DIMENSION, 
                    STD_DIMENSION, 
                    EntityType.LADDER,
                    Optional.absent());
            fail("The PhysicalBody created is outside the bounds of the world, its creation should have failed");
        } catch (final IllegalArgumentException e) {
            assertNotNull(NOT_NULL_MSG, e.getMessage());
        } catch (final IllegalStateException e1) {
            fail(EXCEPTION_THROWN_MSG);
        }
        try {
            physicalFactory.createStaticPhysicalBody(
                    new ImmutablePair<Double, Double>(WORLD_WIDTH + STD_DIMENSION, WORLD_HEIGHT + STD_DIMENSION),
                    0,
                    this.allowedCombinations.get(EntityType.WALKING_ENEMY),
                    STD_DIMENSION, 
                    STD_DIMENSION, 
                    EntityType.WALKING_ENEMY,
                    Optional.absent());
            fail("The PhysicalBody created is outside the bounds of the world, its creation should have failed");
        } catch (final IllegalArgumentException e) {
            assertNotNull(NOT_NULL_MSG, e.getMessage());
        } catch (final IllegalStateException e1) {
            fail(EXCEPTION_THROWN_MSG);
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
        return factory.createDynamicPhysicalBody(STD_POSITION,
                                                    0,
                                                    BodyShape.RECTANGLE,
                                                    STD_WIDTH,
                                                    STD_HEIGHT,
                                                    EntityType.PLAYER);
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
