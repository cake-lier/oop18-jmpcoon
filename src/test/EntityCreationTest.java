package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import com.google.common.base.Optional;

import model.entities.EnemyGenerator;
import model.entities.AbstractEntityBuilder;
import model.entities.EntityBuilderUtils;
import model.entities.EntityType;
import model.entities.Ladder;
import model.entities.Platform;
import model.entities.Player;
import model.entities.PowerUp;
import model.entities.PowerUpType;
import model.entities.RollingEnemy;
import model.entities.WalkingEnemy;
import model.physics.BodyShape;
import model.physics.PhysicalFactory;
import model.physics.PhysicalFactoryImpl;
import model.world.WorldFactoryImpl;
import model.world.WorldImpl;

/**
 * Tests for the creation of {@link Entity}.
 */
public class EntityCreationTest {
    private static final double WORLD_WIDTH = 8;
    private static final double WORLD_HEIGHT = 4.5;
    private static final Pair<Double, Double> STD_POSITION = new ImmutablePair<>(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    private static final Pair<Double, Double> STD_RECTANGULAR_DIMENSIONS = new ImmutablePair<>(WORLD_WIDTH / 10, 
                                                                                               WORLD_HEIGHT / 5);
    private static final Pair<Double, Double> STD_CIRCULAR_DIMENSIONS = new ImmutablePair<>(WORLD_WIDTH / 15, WORLD_WIDTH / 15);
    private static final double STD_RANGE = 1.00;
    private static final double STD_ANGLE = Math.PI / 4;
    private static final double PRECISION = 0.001;
    private static final String NOT_BUILDABLE_MSG = "The building shouldn't have been allowed";
    private static final String BUILDABLE_MSG = "The building should have been allowed";
    private static final String NOT_NULL_MESSAGE = "The message regarding the exception thrown is not present";
    private static final String GIVEN_EQUALS_SET_MSG = "The value given during creation to the EntityBuilder doesn't equal the "
                                                        + "one set to the Entity";

    private final PhysicalFactory factory;
    private final WorldImpl world;

    /**
     * Builds a new {@link EntityCreationTest}.
     */
    public EntityCreationTest() {
        this.factory = new PhysicalFactoryImpl();
        this.world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        this.factory.createPhysicalWorld(this.world, WORLD_WIDTH, WORLD_HEIGHT);
    }

    /**
     * Test for the creation of a {@link Ladder}.
     */
    @Test
    public void ladderCreationTest() {
        final AbstractEntityBuilder<Ladder> ladderBuilder = EntityBuilderUtils.getLadderBuilder();
        ladderBuilder.setPosition(STD_POSITION);
        try {
            ladderBuilder.build();
            fail(NOT_BUILDABLE_MSG);
        } catch (final IllegalStateException e) {
            assertNotNull(NOT_NULL_MESSAGE, e.getMessage());
        }
        ladderBuilder.setFactory(this.factory)
                     .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                     .setAngle(STD_ANGLE)
                     .setShape(BodyShape.RECTANGLE);
        Ladder ladder = null;
        try {
            ladder = ladderBuilder.build();
        } catch (final IllegalStateException e) {
            fail(BUILDABLE_MSG);
        }
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, ladder.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, ladder.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, ladder.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, ladder.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.LADDER, ladder.getType());
    }

    /**
     * Test for the creation of a {@link Platform}.
     */
    @Test
    public void platformCreationTest() {
        final AbstractEntityBuilder<Platform> platformBuilder = EntityBuilderUtils.getPlatformBuilder();
        platformBuilder.setFactory(this.factory);
        try {
            platformBuilder.build();
            fail(NOT_BUILDABLE_MSG);
        } catch (final IllegalStateException e) {
            assertNotNull(NOT_NULL_MESSAGE, e.getMessage());
        }
        platformBuilder.setPosition(STD_POSITION)
                       .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                       .setAngle(STD_ANGLE)
                       .setShape(BodyShape.RECTANGLE);
        Platform platform = null;
        try {
            platform = platformBuilder.build();
        } catch (final IllegalStateException e) {
            fail(BUILDABLE_MSG);
        }
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, platform.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, platform.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, platform.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, platform.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.PLATFORM, platform.getType());
    }

    /**
     * Test for the creation of a {@link RollingEnemy}.
     */
    @Test
    public void rollingEnemyCreationTest() {
        final AbstractEntityBuilder<RollingEnemy> rollingEnemyBuilder = EntityBuilderUtils.getRollingEnemyBuilder();
        rollingEnemyBuilder.setDimensions(STD_CIRCULAR_DIMENSIONS);
        try {
            rollingEnemyBuilder.build();
            fail(NOT_BUILDABLE_MSG);
        } catch (final IllegalStateException e) {
            assertNotNull(NOT_NULL_MESSAGE, e.getMessage());
        }
        rollingEnemyBuilder.setPosition(STD_POSITION)
                           .setFactory(this.factory)
                           .setAngle(STD_ANGLE)
                           .setShape(BodyShape.CIRCLE);
        RollingEnemy rollingEnemy = null;
        try {
            rollingEnemy = rollingEnemyBuilder.build();
        } catch (final IllegalStateException e) {
            fail(BUILDABLE_MSG);
        }
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, rollingEnemy.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_CIRCULAR_DIMENSIONS, rollingEnemy.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, rollingEnemy.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.CIRCLE, rollingEnemy.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.ROLLING_ENEMY, rollingEnemy.getType());
    }

    /**
     * Test for the creation of a {@link WalkingEnemy}.
     */
    @Test
    public void walkingEnemyCreationTest() {
        final AbstractEntityBuilder<WalkingEnemy> walkingEnemyBuilder = EntityBuilderUtils.getWalkingEnemyBuilder();
        walkingEnemyBuilder.setDimensions(STD_RECTANGULAR_DIMENSIONS);
        try {
            walkingEnemyBuilder.build();
            fail(NOT_BUILDABLE_MSG);
        } catch (final IllegalStateException e) {
            assertNotNull(NOT_NULL_MESSAGE, e.getMessage());
        }
        walkingEnemyBuilder.setPosition(STD_POSITION)
                           .setFactory(this.factory)
                           .setAngle(STD_ANGLE)
                           .setShape(BodyShape.RECTANGLE)
                           .setWalkingRange(Optional.of(STD_RANGE));
        WalkingEnemy walkingEnemy = null;
        try {
            walkingEnemy = walkingEnemyBuilder.build();
        } catch (final IllegalStateException e) {
            fail(BUILDABLE_MSG);
        }
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, walkingEnemy.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, walkingEnemy.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, walkingEnemy.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, walkingEnemy.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.WALKING_ENEMY, walkingEnemy.getType());
    }

    /**
     * Test for the creation of a {@link Player}.
     */
    @Test
    public void playerCreationTest() {
        final AbstractEntityBuilder<Player> playerBuilder = EntityBuilderUtils.getPlayerBuilder();
        playerBuilder.setDimensions(STD_RECTANGULAR_DIMENSIONS);
        try {
            playerBuilder.build();
            fail(NOT_BUILDABLE_MSG);
        } catch (final IllegalStateException e) {
            assertNotNull(NOT_NULL_MESSAGE, e.getMessage());
        }
        playerBuilder.setPosition(STD_POSITION)
                           .setFactory(this.factory)
                           .setAngle(STD_ANGLE)
                           .setShape(BodyShape.RECTANGLE);
        Player player = null;
        try {
            player = playerBuilder.build();
        } catch (final IllegalStateException e) {
            fail(BUILDABLE_MSG);
        }
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, player.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, player.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, player.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, player.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.PLAYER, player.getType());
    }

    /**
     * Test for the creation of a {@link PowerUp} with the {@link PowerUpType#EXTRA_LIFE} power.
     */
    @Test
    public void powerUpExtraLifeCreationTest() {
        final AbstractEntityBuilder<PowerUp> powerUpBuilder = EntityBuilderUtils.getPowerUpBuilder();
        powerUpBuilder.setPowerUpType(Optional.of(PowerUpType.EXTRA_LIFE));
        try {
            powerUpBuilder.build();
            fail(NOT_BUILDABLE_MSG);
        } catch (final IllegalStateException e) {
            assertNotNull(NOT_NULL_MESSAGE, e.getMessage());
        }
        powerUpBuilder.setPosition(STD_POSITION)
                      .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                      .setFactory(this.factory)
                      .setAngle(STD_ANGLE)
                      .setShape(BodyShape.RECTANGLE);
        PowerUp powerUp = null;
        try {
            powerUp = powerUpBuilder.build();
        } catch (final IllegalStateException e) {
            fail(BUILDABLE_MSG);
        }
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, powerUp.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, powerUp.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, powerUp.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, powerUp.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.POWERUP, powerUp.getType());
        assertEquals(GIVEN_EQUALS_SET_MSG, PowerUpType.EXTRA_LIFE, powerUp.getPowerUpType());
    }

    /**
     * Test for the creation of a {@link PowerUp} with the {@link PowerUpType#GOAL} power.
     */
    @Test
    public void powerUpGoalCreationTest() {
        final AbstractEntityBuilder<PowerUp> powerUpBuilder = EntityBuilderUtils.getPowerUpBuilder();
        powerUpBuilder.setPowerUpType(Optional.of(PowerUpType.GOAL));
        try {
            powerUpBuilder.build();
            fail(NOT_BUILDABLE_MSG);
        } catch (final IllegalStateException e) {
            assertNotNull(NOT_NULL_MESSAGE, e.getMessage());
        }
        powerUpBuilder.setPosition(STD_POSITION)
                      .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                      .setFactory(this.factory)
                      .setAngle(STD_ANGLE)
                      .setShape(BodyShape.RECTANGLE);
        PowerUp powerUp = null;
        try {
            powerUp = powerUpBuilder.build();
        } catch (final IllegalStateException e) {
            fail(BUILDABLE_MSG);
        }
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, powerUp.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, powerUp.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, powerUp.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, powerUp.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.POWERUP, powerUp.getType());
        assertEquals(GIVEN_EQUALS_SET_MSG, PowerUpType.GOAL, powerUp.getPowerUpType());
    }

    /**
     * Test for the creation of a {@link PowerUp} with the {@link PowerUpType#INVINCIBILITY} power.
     */
    @Test
    public void powerUpInvincibilityCreationTest() {
        final AbstractEntityBuilder<PowerUp> powerUpBuilder = EntityBuilderUtils.getPowerUpBuilder();
        powerUpBuilder.setPowerUpType(Optional.of(PowerUpType.INVINCIBILITY));
        try {
            powerUpBuilder.build();
            fail(NOT_BUILDABLE_MSG);
        } catch (final IllegalStateException e) {
            assertNotNull(NOT_NULL_MESSAGE, e.getMessage());
        }
        powerUpBuilder.setPosition(STD_POSITION)
                      .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                      .setFactory(this.factory)
                      .setAngle(STD_ANGLE)
                      .setShape(BodyShape.RECTANGLE);
        PowerUp powerUp = null;
        try {
            powerUp = powerUpBuilder.build();
        } catch (final IllegalStateException e) {
            fail(BUILDABLE_MSG);
        }
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, powerUp.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, powerUp.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, powerUp.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, powerUp.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.POWERUP, powerUp.getType());
        assertEquals(GIVEN_EQUALS_SET_MSG, PowerUpType.INVINCIBILITY, powerUp.getPowerUpType());
    }

    /**
     * Test for the creation of a {@link EnemyGenerator}.
     */
    @Test
    public void enemyGeneratorCreationTest() {
        final AbstractEntityBuilder<EnemyGenerator> enemyGeneratorBuilder = EntityBuilderUtils.getEnemyGeneratorBuilder();
        enemyGeneratorBuilder.setDimensions(STD_CIRCULAR_DIMENSIONS);
        try {
            enemyGeneratorBuilder.build();
            fail(NOT_BUILDABLE_MSG);
        } catch (final IllegalStateException e) {
            assertNotNull(NOT_NULL_MESSAGE, e.getMessage());
        }
        enemyGeneratorBuilder.setPosition(STD_POSITION)
                           .setFactory(this.factory)
                           .setAngle(STD_ANGLE)
                           .setShape(BodyShape.CIRCLE);
        EnemyGenerator enemyGenerator = null;
        try {
            enemyGenerator = enemyGeneratorBuilder.build();
        } catch (final IllegalStateException e) {
            fail(BUILDABLE_MSG);
        }
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, enemyGenerator.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_CIRCULAR_DIMENSIONS, enemyGenerator.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, enemyGenerator.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.CIRCLE, enemyGenerator.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.ENEMY_GENERATOR, enemyGenerator.getType());
    }
}
