package it.unibo.jmpcoon.test;

import static org.junit.Assert.assertNotNull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import it.unibo.jmpcoon.model.entities.EntityType;
import it.unibo.jmpcoon.model.physics.BodyShape;
import it.unibo.jmpcoon.model.physics.PhysicalFactory;
import it.unibo.jmpcoon.model.physics.PhysicalFactoryImpl;
import it.unibo.jmpcoon.model.world.World;
import it.unibo.jmpcoon.model.world.WorldFactoryImpl;

/**
 * Adjunctive tests over the creation of {@link it.unibo.jmpcoon.model.physics.PhysicalBody}s not covered into the
 * {@link StaticPhysicalBodyCreationTest} and the {@link DynamicPhysicalBodyCreationTest} tests.
 */
public class PhysicalBodyCreationTest {
    private static final double WORLD_WIDTH = 8;
    private static final double WORLD_HEIGHT = 4.5;
    private static final double STD_WIDTH = WORLD_WIDTH / 15;
    private static final double STD_HEIGHT = WORLD_HEIGHT / 15;
    private static final ImmutablePair<Double, Double> STD_POSITION = new ImmutablePair<>(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    private static final double STD_ANGLE = Math.PI / 6;
    private static final String NOT_CREATED = "This PhysicalBody should have been created correctly";

    private final PhysicalFactory factory;

    /**
     * Constructor for this test over {@link it.unibo.jmpcoon.model.physics.PhysicalBody}s.
     */
    public PhysicalBodyCreationTest() {
        this.factory = new PhysicalFactoryImpl();
        this.factory.createPhysicalWorld(World.class.cast(new WorldFactoryImpl().create()), WORLD_WIDTH, WORLD_HEIGHT);
    }

    /**
     * Test for the correct creation of a {@link it.unibo.jmpcoon.model.physics.PlayerPhysicalBody} which has a rectangular shape.
     */
    @Test
    public void allowedPlayerBodyCreationTest() {
        assertNotNull(NOT_CREATED, this.factory.createPlayerPhysicalBody(STD_POSITION, STD_ANGLE, BodyShape.RECTANGLE, STD_WIDTH,
                                                                         STD_HEIGHT));
    }

    /**
     * Test for the correct failure resulting from creation of a {@link it.unibo.jmpcoon.model.physics.PlayerPhysicalBody} with a circular shape.
     */
    @Test(expected = IllegalArgumentException.class)
    public void notAllowedPlayerBodyCreationTest() {
        this.factory.createPlayerPhysicalBody(STD_POSITION, STD_ANGLE, BodyShape.CIRCLE, STD_WIDTH, STD_HEIGHT);
    }

    /**
     * Test for the creation of a circular {@link it.unibo.jmpcoon.model.physics.PhysicalBody} with the correct size, that is with same width
     * and height.
     */
    @Test
    public void correctlySizedCircularBodyCreationTest() {
        assertNotNull(NOT_CREATED, this.factory.createDynamicPhysicalBody(STD_POSITION, STD_ANGLE, BodyShape.CIRCLE, STD_WIDTH,
                                                                          STD_WIDTH, EntityType.ROLLING_ENEMY));
    }

    /**
     * Test for the correct failure resulting from creation of a circular {@link it.unibo.jmpcoon.model.physics.PhysicalBody} with the wrong
     * size, that is with width different from height.
     */
    @Test(expected = IllegalArgumentException.class)
    public void outOfSizeCircularBodyCreationTest() {
        this.factory.createDynamicPhysicalBody(STD_POSITION, STD_ANGLE, BodyShape.CIRCLE, 
                                               STD_WIDTH, STD_HEIGHT, EntityType.ROLLING_ENEMY);
    }
}
