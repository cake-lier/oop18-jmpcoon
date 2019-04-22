package test;

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

import model.entities.EntityType;
import model.physics.BodyShape;
import model.physics.DynamicPhysicalBody;
import model.physics.PhysicalFactory;
import model.physics.PhysicalFactoryImpl;
import model.world.World;
import model.world.WorldFactoryImpl;

/**
 * Parameterized test for the creation of all types of {@link model.physics.DynamicPhysicalBody}s.
 */
@RunWith(Parameterized.class)
public class DynamicPhysicalBodyCreationTest {
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
     * Builds a new {@link DynamicPhysicalBodyCreationTest} for the given {@link EntityType}.
     * @param type the {@link EntityType} to test
     */
    public DynamicPhysicalBodyCreationTest(final EntityType type) {
        this.consideredType = type;
        this.factory = new PhysicalFactoryImpl();
        this.factory.createPhysicalWorld(World.class.cast(new WorldFactoryImpl().create()), WORLD_WIDTH, WORLD_HEIGHT);
        this.allowedCombinations = new HashMap<>();
        this.allowedCombinations.put(EntityType.ROLLING_ENEMY, BodyShape.CIRCLE);
        this.allowedCombinations.put(EntityType.WALKING_ENEMY, BodyShape.RECTANGLE);
    }

    /**
     * Test for the creation of {@link DynamicPhysicalBody}s which have a rectangular shape.
     */
    @Test
    public void allowedRectangularBodiesCreationTest() {
        assumeTrue(this.allowedCombinations.get(this.consideredType) == BodyShape.RECTANGLE);
        final DynamicPhysicalBody body 
            = this.factory.createDynamicPhysicalBody(STD_POSITION, 0, BodyShape.RECTANGLE, STD_WIDTH, STD_HEIGHT,
                                                     this.consideredType);
        assertNotNull(NULL_BODY_MSG, body);
    }

    /**
     * Test for the correct failure while creating {@link DynamicPhysicalBody}s which don't have a rectangular shape.
     */
    @Test(expected = IllegalArgumentException.class)
    public void notAllowedRectangularBodiesCreationTest() {
        assumeTrue(this.allowedCombinations.get(this.consideredType) == BodyShape.CIRCLE);
        this.factory.createDynamicPhysicalBody(STD_POSITION, 0, BodyShape.RECTANGLE, STD_WIDTH, STD_HEIGHT, this.consideredType);
    }

    /**
     * Test for the creation of {@link DynamicPhysicalBody}s which have a circular shape.
     */
    @Test
    public void allowedCircularBodiesCreationTest() {
        assumeTrue(this.allowedCombinations.get(this.consideredType) == BodyShape.CIRCLE);
        final DynamicPhysicalBody body 
            = this.factory.createDynamicPhysicalBody(STD_POSITION, 0, BodyShape.CIRCLE, STD_DIMENSION,
                                                     STD_DIMENSION, this.consideredType);
        assertNotNull(NULL_BODY_MSG, body);
    }

    /**
     * Test for the correct failure while creating {@link DynamicPhysicalBody}s which don't have a circular shape.
     */
    @Test(expected = IllegalArgumentException.class)
    public void notAllowedCircularBodiesCreationTest() {
        assumeTrue(this.allowedCombinations.get(this.consideredType) == BodyShape.RECTANGLE);
        this.factory.createDynamicPhysicalBody(STD_POSITION, 0, BodyShape.CIRCLE, STD_DIMENSION, STD_DIMENSION,
                                               this.consideredType);
    }

    /**
     * Returns the {@link EntityType}s of {@link model.entities.Entity}s which are considered dynamic.
     * @return a {@link Collection} of {@link EntityType}s of the {@link model.entities.Entity}s which are dynamic
     */
    @Parameters
    public static Collection<EntityType> dynamicTypes() {
        return Arrays.asList(EntityType.ROLLING_ENEMY, EntityType.WALKING_ENEMY);
    }
}
