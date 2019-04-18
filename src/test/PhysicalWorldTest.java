package test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import model.entities.EntityType;
import model.physics.BodyShape;
import model.physics.PhysicalFactory;
import model.physics.PhysicalFactoryImpl;
import model.world.WorldFactoryImpl;
import model.world.WorldImpl;

/**
 * Test for the creation of {@link PhyisicalBody}.
 */
public class PhysicalWorldTest {
    private static final double WORLD_WIDTH = 8;
    private static final double WORLD_HEIGHT = 4.5;
    private static final double STD_WIDTH = WORLD_WIDTH / 15;
    private static final double STD_HEIGHT = WORLD_HEIGHT / 15;
    private static final ImmutablePair<Double, Double> STD_POSITION = new ImmutablePair<>(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    private static final String NOT_NULL_MSG = "The message regarding the exception thrown is not present";

    /**
     * Test for {@link World} creation.
     */
    @Test
    public void worldCreationTest() {
        final PhysicalFactory physicalFactory = new PhysicalFactoryImpl();
        final WorldImpl world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        /* the world has yet to be created */
        try {
            physicalFactory.createDynamicPhysicalBody(STD_POSITION,
                                                      0,
                                                      BodyShape.RECTANGLE,
                                                      STD_WIDTH,
                                                      STD_HEIGHT,
                                                      EntityType.PLAYER);
            fail("The creation of the body shouldn't have been allowed");
        } catch (final IllegalStateException e) {
            assertNotNull(NOT_NULL_MSG, e.getMessage());
        } catch (final IllegalArgumentException e1) {
            fail("This exception shouldn't have been thrown");
        }
        physicalFactory.createPhysicalWorld(world, WORLD_WIDTH, WORLD_HEIGHT);
        /* the world has been created */
        try {
            physicalFactory.createDynamicPhysicalBody(STD_POSITION,
                                                      0,
                                                      BodyShape.RECTANGLE,
                                                      STD_WIDTH,
                                                      STD_HEIGHT,
                                                      EntityType.PLAYER);
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
}
