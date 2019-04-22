package it.unibo.jmpcoon.test;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Optional;

import it.unibo.jmpcoon.model.entities.AbstractEntityBuilder;
import it.unibo.jmpcoon.model.entities.Entity;
import it.unibo.jmpcoon.model.entities.EntityBuilderUtils;
import it.unibo.jmpcoon.model.entities.PowerUpType;
import it.unibo.jmpcoon.model.physics.BodyShape;
import it.unibo.jmpcoon.model.physics.PhysicalFactory;
import it.unibo.jmpcoon.model.physics.PhysicalFactoryImpl;
import it.unibo.jmpcoon.model.world.World;
import it.unibo.jmpcoon.model.world.WorldFactoryImpl;

/**
 * Parameterized test for checking that all builders fail when initialized with too few parameters.
 */
@RunWith(Parameterized.class)
public class EntityBuilderErrorsTest {
    private static final double WORLD_WIDTH = 8;
    private static final double WORLD_HEIGHT = 4.5;
    private static final Pair<Double, Double> STD_POSITION = new ImmutablePair<>(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    private static final PhysicalFactory FACTORY = new PhysicalFactoryImpl();
    private static final Pair<Double, Double> STD_CIRCULAR_DIMENSIONS = new ImmutablePair<>(WORLD_WIDTH / 10, WORLD_HEIGHT / 10);
    private static final BodyShape STD_SHAPE = BodyShape.CIRCLE;
    private static final double STD_ANGLE = Math.PI / 4;
    private static final String NOT_BUILDABLE_MSG = "The building shouldn't have been allowed";

    private final AbstractEntityBuilder<? extends Entity> builder;

    /**
     * Creates a new instance of this class with the given builder.
     * @param builder the {@link AbstractEntityBuilder} to use
     */
    public EntityBuilderErrorsTest(final AbstractEntityBuilder<? extends Entity> builder) {
        this.builder = builder;
    }

    /**
     * Test which controls that the build should fail if the passed {@link AbstractEntityBuilder} is set with only the
     * position parameter.
     */
    @Test(expected = IllegalStateException.class)
    public void creationWithOnlyPositionFail() {
        this.builder.setPosition(STD_POSITION);
        this.builder.build();
        fail(NOT_BUILDABLE_MSG);
    }

    /**
     * Test which controls that the build should fail if the passed {@link AbstractEntityBuilder} is set with only the
     * factory parameter.
     */
    @Test(expected = IllegalStateException.class)
    public void creationWithOnlyFactoryFail() {
        this.builder.setFactory(FACTORY);
        this.builder.build();
        fail(NOT_BUILDABLE_MSG);
    }

    /**
     * Test which controls that the build should fail if the passed {@link AbstractEntityBuilder} is set with only the
     * dimensions parameter.
     */
    @Test(expected = IllegalStateException.class)
    public void creationWithOnlyDimensionsFail() {
        this.builder.setDimensions(STD_CIRCULAR_DIMENSIONS);
        this.builder.build();
        fail(NOT_BUILDABLE_MSG);
    }

    /**
     * Test which controls that the build should fail if the passed {@link AbstractEntityBuilder} is set with only the
     * shape parameter.
     */
    @Test(expected = IllegalStateException.class)
    public void creationWithOnlyShapeFail() {
        this.builder.setShape(STD_SHAPE);
        this.builder.build();
        fail(NOT_BUILDABLE_MSG);
    }

    /**
     * Test which controls that the build should fail if the passed {@link AbstractEntityBuilder} is set with only the
     * angle parameter.
     */
    @Test(expected = IllegalStateException.class)
    public void creationWithOnlyAngleFail() {
        this.builder.setAngle(STD_ANGLE);
        this.builder.build();
        fail(NOT_BUILDABLE_MSG);
    }

    /**
     * Test which controls that the build should fail if the passed {@link AbstractEntityBuilder} is set with only the
     * {@link PowerUpType} parameter.
     */
    @Test(expected = IllegalStateException.class)
    public void creationWithOnlyPowerUpTypeFail() {
        this.builder.setPowerUpType(Optional.of(PowerUpType.INVINCIBILITY));
        this.builder.build();
        fail(NOT_BUILDABLE_MSG);
    }

    /**
     * Test which controls that the build should fail if the passed {@link AbstractEntityBuilder} is set with only the
     * walking range parameter.
     */
    @Test(expected = IllegalStateException.class)
    public void creationWithOnlyWalkingRangeFail() {
        this.builder.setWalkingRange(Optional.of(1.0));
        this.builder.build();
        fail(NOT_BUILDABLE_MSG);
    }

    /**
     * Test which controls that the build should fail if the passed {@link AbstractEntityBuilder} is set with only the
     * walking range parameter.
     */
    @Test(expected = IllegalStateException.class)
    public void creationWithOnlyModifiableWorldFail() {
        this.builder.setWorld(Optional.of(World.class.cast(new WorldFactoryImpl().create())));
        this.builder.build();
        fail(NOT_BUILDABLE_MSG);
    }

    /**
     * Parameters for the test of failed creation with only one value set.
     * @return a {@link Collection} of {@link AbstractEntityBuilder}
     */
    @Parameters
    public static Collection<AbstractEntityBuilder<? extends Entity>> builders() {
        return Arrays.asList(EntityBuilderUtils.getLadderBuilder(),
                             EntityBuilderUtils.getPlatformBuilder(),
                             EntityBuilderUtils.getEnemyGeneratorBuilder(),
                             EntityBuilderUtils.getPowerUpBuilder(),
                             EntityBuilderUtils.getWalkingEnemyBuilder(),
                             EntityBuilderUtils.getPlayerBuilder(),
                             EntityBuilderUtils.getRollingEnemyBuilder());
    }
}
