package test;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import com.google.common.base.Optional;

import model.entities.EntityType;
import model.entities.PowerUpType;
import model.physics.BodyShape;
import model.physics.PhysicalBody;
import model.physics.PhysicalFactory;
import model.physics.PhysicalFactoryImpl;
import model.world.World;
import model.world.WorldFactoryImpl;

/**
 * Adjunctive tests over the creation of {@link PhysicalBody}s not covered into the {@link StaticPhysicalBodyCreationTest}
 * and the {@link DynamicPhysicalBodyCreationTest} tests.
 */
public class PhysicalBodyCreationTest {
    private static final double WORLD_WIDTH = 8;
    private static final double WORLD_HEIGHT = 4.5;
    private static final double STD_WIDTH = WORLD_WIDTH / 15;
    private static final double STD_HEIGHT = WORLD_HEIGHT / 15;
    private static final ImmutablePair<Double, Double> STD_POSITION = new ImmutablePair<>(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    private static final double STD_ANGLE = Math.PI / 6;
    private static final String NOT_CONSIDERED_MSG = "Not all possible types have been considered";

    private final PhysicalFactory factory;

    /**
     * Constructor for this test over {@link PhysicalBody}s.
     */
    public PhysicalBodyCreationTest() {
        this.factory = new PhysicalFactoryImpl();
        this.factory.createPhysicalWorld(World.class.cast(new WorldFactoryImpl().create()), WORLD_WIDTH, WORLD_HEIGHT);
    }

    /**
     * Test for the correct initialization of this {@link PhysicalBodyCreationTest}.
     */
    @Test
    public void correctClassInitialization() {
        final List<EntityType> consideredTypes = Arrays.asList(EntityType.LADDER, EntityType.ENEMY_GENERATOR, 
                                                               EntityType.POWERUP, EntityType.PLATFORM, EntityType.ROLLING_ENEMY,
                                                               EntityType.WALKING_ENEMY, EntityType.PLAYER);
        assertTrue(NOT_CONSIDERED_MSG, consideredTypes.containsAll(Arrays.asList(EntityType.values())));
        this.createStandardPlatform(this.factory);
        this.createStandardLadder(this.factory);
        this.createStandardPlayer(this.factory);
        this.createStandardRollingEnemy(this.factory);
        this.createStandardWalkingEnemy(this.factory);
        this.createStandardEnemyGenerator(this.factory);
        this.createStandardPowerUp(this.factory);
    }

    /**
     * Test for the correct creation of a {@link PlayerPhysicalBody} which has a rectangular shape.
     */
    @Test
    public void allowedPlayerBodyCreationTest() {
        this.factory.createPlayerPhysicalBody(STD_POSITION, STD_ANGLE, BodyShape.RECTANGLE, STD_WIDTH, STD_HEIGHT);
    }

    /**
     * Test for the correct failure resulting from creation of a {@link PlayerPhysicalBody} with a circular shape.
     */
    @Test(expected = IllegalArgumentException.class)
    public void notAllowedPlayerBodyCreationTest() {
        this.factory.createPlayerPhysicalBody(STD_POSITION, STD_ANGLE, BodyShape.CIRCLE, STD_WIDTH, STD_HEIGHT);
    }

    /**
     * Test for the creation of a circular {@link PhysicalBody} with the correct size, that is with same width and height.
     */
    @Test
    public void correctlySizedCircularBodyCreationTest() {
        this.factory.createDynamicPhysicalBody(STD_POSITION, STD_ANGLE, BodyShape.CIRCLE, 
                                               STD_WIDTH, STD_WIDTH, EntityType.ROLLING_ENEMY);
    }

    /**
     * Test for the correct failure resulting from creation of a circular {@link PhysicalBody} with the wrong size, that is
     * with same width different from height.
     */
    @Test(expected = IllegalArgumentException.class)
    public void outOfSizeCircularBodyCreationTest() {
        this.factory.createDynamicPhysicalBody(STD_POSITION, STD_ANGLE, BodyShape.CIRCLE, 
                                               STD_WIDTH, STD_HEIGHT, EntityType.ROLLING_ENEMY);
    }

    private PhysicalBody createStandardPlatform(final PhysicalFactory factory) {
        return factory.createStaticPhysicalBody(STD_POSITION, STD_ANGLE, BodyShape.RECTANGLE, STD_WIDTH, STD_HEIGHT,
                                                EntityType.PLATFORM, Optional.absent());
    }

    private PhysicalBody createStandardLadder(final PhysicalFactory factory) {
        return factory.createStaticPhysicalBody(STD_POSITION, 0, BodyShape.RECTANGLE, STD_WIDTH, STD_HEIGHT, EntityType.LADDER,
                                                Optional.absent());
    }

    private PhysicalBody createStandardEnemyGenerator(final PhysicalFactory factory) {
        return factory.createStaticPhysicalBody(STD_POSITION, 0, BodyShape.CIRCLE, STD_WIDTH, STD_WIDTH,
                                                EntityType.ENEMY_GENERATOR, Optional.absent());
    }

    private PhysicalBody createStandardPowerUp(final PhysicalFactory factory) {
        return factory.createStaticPhysicalBody(STD_POSITION, 0, BodyShape.RECTANGLE, STD_WIDTH, STD_HEIGHT, EntityType.POWERUP,
                                                Optional.of(PowerUpType.EXTRA_LIFE));
    }

    private PhysicalBody createStandardPlayer(final PhysicalFactory factory) {
        return factory.createPlayerPhysicalBody(STD_POSITION, 0, BodyShape.RECTANGLE, STD_WIDTH, STD_HEIGHT);
    }

    private PhysicalBody createStandardRollingEnemy(final PhysicalFactory factory) {
        return factory.createDynamicPhysicalBody(STD_POSITION, 0, BodyShape.CIRCLE, STD_WIDTH, STD_WIDTH,
                                                 EntityType.ROLLING_ENEMY);
    }

    private PhysicalBody createStandardWalkingEnemy(final PhysicalFactory factory) {
        return factory.createDynamicPhysicalBody(STD_POSITION, 0, BodyShape.RECTANGLE, STD_WIDTH, STD_HEIGHT,
                                                 EntityType.WALKING_ENEMY);
    }
}
