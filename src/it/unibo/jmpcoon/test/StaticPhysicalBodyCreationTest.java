package it.unibo.jmpcoon.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Optional;

import it.unibo.jmpcoon.model.entities.EntityType;
import it.unibo.jmpcoon.model.entities.PowerUpType;
import it.unibo.jmpcoon.model.physics.BodyShape;
import it.unibo.jmpcoon.model.physics.PhysicalFactory;
import it.unibo.jmpcoon.model.physics.PhysicalFactoryImpl;
import it.unibo.jmpcoon.model.physics.StaticPhysicalBody;
import it.unibo.jmpcoon.model.world.World;
import it.unibo.jmpcoon.model.world.WorldFactoryImpl;

/**
 * Parameterized test for the creation of all types of {@link it.unibo.jmpcoon.model.physics.StaticPhysicalBody}s.
 */
@RunWith(Parameterized.class)
public class StaticPhysicalBodyCreationTest {
    private static final double WORLD_WIDTH = 8;
    private static final double WORLD_HEIGHT = 4.5;
    private static final double STD_WIDTH = WORLD_WIDTH / 15;
    private static final double STD_HEIGHT = WORLD_HEIGHT / 15;
    private static final ImmutablePair<Double, Double> STD_POSITION = new ImmutablePair<>(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    private static final double STD_DIMENSION = WORLD_WIDTH / 8;
    private static final String NULL_BODY_MSG = "Instead of a PhysicalBody, null was returned";

    private final Map<EntityType, BodyShape> allowedCombinations;
    private final EntityType consideredType;
    private final PhysicalFactory factory;
 
    /**
     * Builds a new {@link StaticPhysicalBodyCreationTest} for the given {@link EntityType}.
     * @param consideredType the {@link EntityType} to test
     */
    public StaticPhysicalBodyCreationTest(final EntityType consideredType) {
        this.consideredType = consideredType;
        this.factory = new PhysicalFactoryImpl();
        this.factory.createPhysicalWorld(World.class.cast(new WorldFactoryImpl().create()), WORLD_WIDTH, WORLD_HEIGHT);
        this.allowedCombinations = new HashMap<>();
        this.allowedCombinations.put(EntityType.LADDER, BodyShape.RECTANGLE);
        this.allowedCombinations.put(EntityType.PLATFORM, BodyShape.RECTANGLE);
        this.allowedCombinations.put(EntityType.ENEMY_GENERATOR, BodyShape.CIRCLE);
        this.allowedCombinations.put(EntityType.POWERUP, BodyShape.RECTANGLE);
    }

    /**
     * Test for the creation of {@link StaticPhysicalBody}s which have a rectangular shape.
     */
    @Test
    public void allowedRectangularBodiesCreationTest() {
        assumeTrue(this.allowedCombinations.get(this.consideredType) == BodyShape.RECTANGLE);
        final StaticPhysicalBody body 
            = this.factory.createStaticPhysicalBody(STD_POSITION, 0, BodyShape.RECTANGLE, STD_WIDTH,
                                                    STD_HEIGHT, this.consideredType, this.consideredType == EntityType.POWERUP
                                                                                     ? Optional.of(PowerUpType.GOAL)
                                                                                     : Optional.absent());
        assertNotNull(NULL_BODY_MSG, body);
    }

    /**
     * Test for the correct failure while creating {@link StaticPhysicalBody}s which don't have a rectangular shape.
     */
    @Test(expected = IllegalArgumentException.class)
    public void notAllowedRectangularBodiesCreationTest() {
        assumeTrue(this.allowedCombinations.get(this.consideredType) == BodyShape.CIRCLE);
        this.factory.createStaticPhysicalBody(STD_POSITION, 0, BodyShape.RECTANGLE, STD_WIDTH, STD_HEIGHT,
                                                  this.consideredType, this.consideredType == EntityType.POWERUP
                                                                       ? Optional.of(PowerUpType.GOAL)
                                                                       : Optional.absent());
    }

    /**
     * Test for the creation of {@link StaticPhysicalBody}s which have a circular shape.
     */
    @Test
    public void allowedCircularBodiesCreationTest() {
        assumeTrue(this.allowedCombinations.get(this.consideredType) == BodyShape.CIRCLE);
        final StaticPhysicalBody body 
            = this.factory.createStaticPhysicalBody(STD_POSITION, 0, BodyShape.CIRCLE, STD_DIMENSION,
                                                    STD_DIMENSION, this.consideredType, this.consideredType == EntityType.POWERUP
                                                                                        ? Optional.of(PowerUpType.GOAL)
                                                                                        : Optional.absent());
        assertNotNull(NULL_BODY_MSG, body);
    }

    /**
     * Test for the correct failure while creating {@link StaticPhysicalBody}s which don't have a circular shape.
     */
    @Test(expected = IllegalArgumentException.class)
    public void notAllowedCircularBodiesCreationTest() {
        assumeTrue(this.allowedCombinations.get(this.consideredType) == BodyShape.RECTANGLE);
        this.factory.createStaticPhysicalBody(STD_POSITION, 0, BodyShape.CIRCLE, STD_DIMENSION, STD_DIMENSION,
                                              this.consideredType, this.consideredType == EntityType.POWERUP
                                                                   ? Optional.of(PowerUpType.GOAL) : Optional.absent());
    }

    /**
     * Returns the {@link EntityType}s of {@link it.unibo.jmpcoon.model.entities.Entity}s which are considered static.
     * @return a {@link Collection} of {@link EntityType}s of the {@link it.unibo.jmpcoon.model.entities.Entity}s which are static
     */
    @Parameters
    public static Collection<EntityType> staticTypes() {
        return Arrays.asList(EntityType.LADDER, EntityType.ENEMY_GENERATOR, EntityType.POWERUP, EntityType.PLATFORM);
    }
}
