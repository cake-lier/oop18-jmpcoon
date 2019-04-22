package test;

import static org.junit.Assert.assertNotNull;

import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;

import model.entities.EntityType;
import model.physics.BodyShape;
import model.physics.PhysicalFactory;
import model.physics.PhysicalFactoryImpl;
import model.world.World;
import model.world.WorldFactoryImpl;
import model.world.WorldImpl;

/**
 * Test for the creation of {@link PhyisicalWorld}.
 */
public class PhysicalWorldTest {
    private static final double WORLD_WIDTH = 8;
    private static final double WORLD_HEIGHT = 4.5;
    private static final double STD_WIDTH = WORLD_WIDTH / 15;
    private static final double STD_HEIGHT = WORLD_HEIGHT / 15;
    private static final ImmutablePair<Double, Double> STD_POSITION = new ImmutablePair<>(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    private static final String NOT_CREATED = "This instance should have been created correctly";

    private final World world = WorldImpl.class.cast(new WorldFactoryImpl().create());
    private PhysicalFactory factory;

    /**
     * Initialization method needed for recreating a new {@link PhysicalFactory} for each test for performing a clean
     * test.
     */
    @Before
    public void initializePhysicalFactory() {
        this.factory = new PhysicalFactoryImpl();
    }

    /**
     * Test for the correct failure of creating a {@link PhysicalBody} before creating the {@link PhysicalWorld}.
     */
    @Test(expected = IllegalStateException.class)
    public void notYetCreatedWorldFail() {
        this.factory.createDynamicPhysicalBody(STD_POSITION, 0, BodyShape.RECTANGLE, STD_WIDTH, STD_HEIGHT,
                                               EntityType.WALKING_ENEMY);
    }

    /**
     * Test for correct {@link PhysicalWorld} creation.
     */
    @Test
    public void worldCreationTest() {
        assertNotNull(NOT_CREATED, this.factory.createPhysicalWorld(this.world, WORLD_WIDTH, WORLD_HEIGHT));
        assertNotNull(NOT_CREATED, this.factory.createPlayerPhysicalBody(STD_POSITION, 0, BodyShape.RECTANGLE, STD_WIDTH,
                                                                         STD_HEIGHT));
    }

    /**
     * Test for the correct failure of creating a {@link PhysicalWorld} after the first created.
     */
    @Test(expected = IllegalStateException.class)
    public void alreadyCreatedWorldFail() {
        IntStream.range(0, 2).forEach(i -> this.factory.createPhysicalWorld(this.world, WORLD_WIDTH, WORLD_HEIGHT));
    }
}
