package test;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import com.google.common.base.Optional;

import model.entities.AbstractEntityBuilder;
import model.entities.EnemyGenerator;
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
import model.world.World;
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
    private static final String GIVEN_EQUALS_SET_MSG = "The value given during creation to the EntityBuilder doesn't equal the "
                                                       + "one set to the Entity";

    private final PhysicalFactory factory;
    private final World world;

    /**
     * Builds a new {@link EntityCreationTest}.
     */
    public EntityCreationTest() {
        this.factory = new PhysicalFactoryImpl();
        this.world = WorldImpl.class.cast(new WorldFactoryImpl().create());
        this.factory.createPhysicalWorld(this.world, WORLD_WIDTH, WORLD_HEIGHT);
    }

    /**
     * Test for the creation of a {@link Ladder} via its builder and for the failure of the builder itself when
     * trying to create another instance.
     */
    @Test(expected = IllegalStateException.class)
    public void ladderCreationTest() {
        final AbstractEntityBuilder<Ladder> ladderBuilder = EntityBuilderUtils.getLadderBuilder()
                                                                              .setPosition(STD_POSITION)
                                                                              .setFactory(this.factory)
                                                                              .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                                                                              .setAngle(STD_ANGLE)
                                                                              .setShape(BodyShape.RECTANGLE);
        final Ladder ladder = ladderBuilder.build();
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, ladder.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, ladder.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, ladder.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, ladder.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.LADDER, ladder.getType());
        ladderBuilder.build();
    }

    /**
     * Test for the creation of a {@link Platform} via its builder and for the failure of the builder itself when
     * trying to create another instance.
     */
    @Test(expected = IllegalStateException.class)
    public void platformCreationTest() {
        final AbstractEntityBuilder<Platform> platformBuilder = EntityBuilderUtils.getPlatformBuilder()
                                                                                  .setPosition(STD_POSITION)
                                                                                  .setFactory(this.factory)
                                                                                  .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                                                                                  .setAngle(STD_ANGLE)
                                                                                  .setShape(BodyShape.RECTANGLE);
        final Platform platform = platformBuilder.build();
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, platform.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, platform.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, platform.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, platform.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.PLATFORM, platform.getType());
        platformBuilder.build();
    }

    /**
     * Test for the creation of a {@link RollingEnemy} via its builder and for the failure of the builder itself when
     * trying to create another instance.
     */
    @Test(expected = IllegalStateException.class)
    public void rollingEnemyCreationTest() {
        final AbstractEntityBuilder<RollingEnemy> rollingBuilder = EntityBuilderUtils.getRollingEnemyBuilder()
                                                                                     .setDimensions(STD_CIRCULAR_DIMENSIONS)
                                                                                     .setPosition(STD_POSITION)
                                                                                     .setFactory(this.factory)
                                                                                     .setAngle(STD_ANGLE)
                                                                                     .setShape(BodyShape.CIRCLE);
        final RollingEnemy rollingEnemy = rollingBuilder.build();
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, rollingEnemy.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_CIRCULAR_DIMENSIONS, rollingEnemy.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, rollingEnemy.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.CIRCLE, rollingEnemy.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.ROLLING_ENEMY, rollingEnemy.getType());
        rollingBuilder.build();
    }

    /**
     * Test for the creation of a {@link WalkingEnemy} via its builder and for the failure of the builder itself when
     * trying to create another instance.
     */
    @Test(expected = IllegalStateException.class)
    public void walkingEnemyCreationTest() {
        final AbstractEntityBuilder<WalkingEnemy> walkingBuilder = EntityBuilderUtils.getWalkingEnemyBuilder()
                                                                                     .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                                                                                     .setPosition(STD_POSITION)
                                                                                     .setFactory(this.factory)
                                                                                     .setAngle(STD_ANGLE)
                                                                                     .setShape(BodyShape.RECTANGLE)
                                                                                     .setWalkingRange(Optional.of(STD_RANGE));
        final WalkingEnemy walkingEnemy = walkingBuilder.build();
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, walkingEnemy.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, walkingEnemy.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, walkingEnemy.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, walkingEnemy.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.WALKING_ENEMY, walkingEnemy.getType());
        walkingBuilder.build();
    }

    /**
     * Test which controls that the build should fail if the {@link AbstractEntityBuilder} for {@link WalkingEnemy}s tries to
     * build it without inserting the walking range of this {@link Entity}.
     */
    @Test(expected = IllegalStateException.class)
    public void walkingEnemyCreationWithoutWalkingRangeFail() {
        EntityBuilderUtils.getWalkingEnemyBuilder().setPosition(STD_POSITION)
                                                   .setFactory(this.factory)
                                                   .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                                                   .setShape(BodyShape.RECTANGLE)
                                                   .setAngle(STD_ANGLE)
                                                   .build();
    }

    /**
     * Test for the creation of a {@link Player} via its builder and for the failure of the builder itself when
     * trying to create another instance.
     */
    @Test(expected = IllegalStateException.class)
    public void playerCreationTest() {
        final AbstractEntityBuilder<Player> playerBuilder = EntityBuilderUtils.getPlayerBuilder()
                                                                              .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                                                                              .setPosition(STD_POSITION)
                                                                              .setFactory(this.factory)
                                                                              .setAngle(STD_ANGLE)
                                                                              .setShape(BodyShape.RECTANGLE);
        final Player player = playerBuilder.build();
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, player.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, player.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, player.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, player.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.PLAYER, player.getType());
        playerBuilder.build();
    }

    /**
     * Test for the creation of a {@link PowerUp} with the {@link PowerUpType#EXTRA_LIFE} power via its builder and for the
     * failure of the builder itself when trying to create another instance.
     */
    @Test(expected = IllegalStateException.class)
    public void powerUpExtraLifeCreationTest() {
        final AbstractEntityBuilder<PowerUp> powerUpBuilder
            = EntityBuilderUtils.getPowerUpBuilder()
                                .setPosition(STD_POSITION)
                                .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                                .setFactory(this.factory)
                                .setAngle(STD_ANGLE)
                                .setShape(BodyShape.RECTANGLE)
                                .setPowerUpType(Optional.of(PowerUpType.EXTRA_LIFE));
        final PowerUp powerUp = powerUpBuilder.build();
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, powerUp.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, powerUp.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, powerUp.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, powerUp.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.POWERUP, powerUp.getType());
        assertEquals(GIVEN_EQUALS_SET_MSG, PowerUpType.EXTRA_LIFE, powerUp.getPowerUpType());
        powerUpBuilder.build();
    }

    /**
     * Test for the creation of a {@link PowerUp} with the {@link PowerUpType#GOAL} power via its builder and for the failure
     * of the builder itself when trying to create another instance.
     */
    @Test(expected = IllegalStateException.class)
    public void powerUpGoalCreationTest() {
        final AbstractEntityBuilder<PowerUp> powerUpBuilder = EntityBuilderUtils.getPowerUpBuilder()
                                                                                .setPosition(STD_POSITION)
                                                                                .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                                                                                .setFactory(this.factory)
                                                                                .setAngle(STD_ANGLE)
                                                                                .setShape(BodyShape.RECTANGLE)
                                                                                .setPowerUpType(Optional.of(PowerUpType.GOAL));
        final PowerUp powerUp = powerUpBuilder.build();
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, powerUp.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, powerUp.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, powerUp.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, powerUp.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.POWERUP, powerUp.getType());
        assertEquals(GIVEN_EQUALS_SET_MSG, PowerUpType.GOAL, powerUp.getPowerUpType());
        powerUpBuilder.build();
    }

    /**
     * Test for the creation of a {@link PowerUp} with the {@link PowerUpType#INVINCIBILITY} power via its builder and
     * for the failure of the builder itself when trying to create another instance.
     */
    @Test(expected = IllegalStateException.class)
    public void powerUpInvincibilityCreationTest() {
        final AbstractEntityBuilder<PowerUp> powerUpBuilder =
                EntityBuilderUtils.getPowerUpBuilder()
                                  .setPosition(STD_POSITION)
                                  .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                                  .setFactory(this.factory)
                                  .setAngle(STD_ANGLE)
                                  .setShape(BodyShape.RECTANGLE)
                                  .setPowerUpType(Optional.of(PowerUpType.INVINCIBILITY));
        final PowerUp powerUp = powerUpBuilder.build();
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, powerUp.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_RECTANGULAR_DIMENSIONS, powerUp.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, powerUp.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.RECTANGLE, powerUp.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.POWERUP, powerUp.getType());
        assertEquals(GIVEN_EQUALS_SET_MSG, PowerUpType.INVINCIBILITY, powerUp.getPowerUpType());
        powerUpBuilder.build();
    }

    /**
     * Test which controls that the build should fail if the {@link AbstractEntityBuilder} for {@link PowerUp}s tries to
     * build it without inserting the {@link PowerUpType}.
     */
    @Test(expected = IllegalStateException.class)
    public void creationWithoutTypeFail() {
        EntityBuilderUtils.getPowerUpBuilder().setPosition(STD_POSITION)
                                              .setFactory(this.factory)
                                              .setDimensions(STD_RECTANGULAR_DIMENSIONS)
                                              .setShape(BodyShape.RECTANGLE)
                                              .setAngle(STD_ANGLE)
                                              .build();
    }

    /**
     * Test for the creation of a {@link EnemyGenerator} via its builder and for the failure of the builder itself when
     * trying to create another instance.
     */
    @Test(expected = IllegalStateException.class)
    public void enemyGeneratorCreationTest() {
        final AbstractEntityBuilder<EnemyGenerator> enemyGeneratorBuilder =
            EntityBuilderUtils.getEnemyGeneratorBuilder()
                              .setPosition(STD_POSITION)
                              .setDimensions(STD_CIRCULAR_DIMENSIONS)
                              .setFactory(this.factory)
                              .setAngle(STD_ANGLE)
                              .setShape(BodyShape.CIRCLE)
                              .setWorld(Optional.of(this.world));
        final EnemyGenerator enemyGenerator = enemyGeneratorBuilder.build();
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_POSITION, enemyGenerator.getPosition());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_CIRCULAR_DIMENSIONS, enemyGenerator.getDimensions());
        assertEquals(GIVEN_EQUALS_SET_MSG, STD_ANGLE, enemyGenerator.getAngle(), PRECISION);
        assertEquals(GIVEN_EQUALS_SET_MSG, BodyShape.CIRCLE, enemyGenerator.getShape());
        assertEquals(GIVEN_EQUALS_SET_MSG, EntityType.ENEMY_GENERATOR, enemyGenerator.getType());
        enemyGeneratorBuilder.build();
    }

    /**
     * Test which controls that the build should fail if the {@link AbstractEntityBuilder} for {@link EnemyGenerator}s tries to
     * build it without inserting the {@link ModifiableWorld}.
     */
    @Test(expected = IllegalStateException.class)
    public void enemyGeneratorCreationWithoutWorldFail() {
        EntityBuilderUtils.getEnemyGeneratorBuilder().setPosition(STD_POSITION)
                                                     .setFactory(this.factory)
                                                     .setDimensions(STD_CIRCULAR_DIMENSIONS)
                                                     .setShape(BodyShape.CIRCLE)
                                                     .setAngle(STD_ANGLE)
                                                     .build();
    }
}
