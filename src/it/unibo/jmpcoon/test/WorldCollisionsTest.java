package it.unibo.jmpcoon.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import it.unibo.jmpcoon.model.entities.EntityProperties;
import it.unibo.jmpcoon.model.entities.EntityPropertiesImpl;
import it.unibo.jmpcoon.model.entities.EntityType;
import it.unibo.jmpcoon.model.entities.PowerUpType;
import it.unibo.jmpcoon.model.physics.BodyShape;
import it.unibo.jmpcoon.model.world.UpdatableWorld;
import it.unibo.jmpcoon.model.world.WorldFactoryImpl;

/**
 * Class of tests for the collisions happening inside a {@link UpdatableWorld}.
 */
public class WorldCollisionsTest {
    private static final double WORLD_WIDTH = 8;
    private static final double WORLD_HEIGHT = 4.5;
    private static final double PLATFORM_WIDTH = WORLD_WIDTH / 2;
    private static final double PLATFORM_HEIGHT = 0.29;
    private static final double PLAYER_DIMENSION = 0.3;
    private static final double ROLLING_ENEMY_DIMENSION = 0.2;
    private static final double WALKING_ENEMY_DIMENSION = 0.25;
    private static final double WALKING_RANGE = 0.9;
    private static final double POWER_UP_DIMENSION = 0.4;
    private static final double ANGLE = 0;
    private static final String NOT_STARTED_MSG = "The simulation has yet to start";
    private static final String NO_WIN = "The player is colliding with the goal but not winning";
    private static final String NO_LOSE = "The player is colliding with the enemy but not losing";
    private static final String TWO_ALIVE_ENTITIES = "The alive entities should be two";
    private static final String ONE_DEAD_ENTITY = "There should be only one dead entity";
    private static final String ROLLING_DEAD = "The dead entities should only be rolling enemies";
    private static final String WALKING_DEAD = "The dead entities should only be walking enemies";
    private static final String EXTRA_LIFE = "The player should have acquired a life";

    private final EntityProperties platformProperties;
    private final EntityProperties playerProperties;
    private UpdatableWorld world;

    /**
     * Builds a new {@link WorldTest}.
     */
    public WorldCollisionsTest() {
        this.platformProperties 
            = new EntityPropertiesImpl(EntityType.PLATFORM, BodyShape.RECTANGLE, WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 
                                       PLATFORM_WIDTH, PLATFORM_HEIGHT, ANGLE, Optional.absent(), Optional.absent());
        this.playerProperties 
            = new EntityPropertiesImpl(EntityType.PLAYER, BodyShape.RECTANGLE, WORLD_WIDTH / 2,
                                       WORLD_HEIGHT / 2 + PLATFORM_HEIGHT / 2 + PLAYER_DIMENSION / 2, PLAYER_DIMENSION, 
                                       PLAYER_DIMENSION, ANGLE, Optional.absent(), Optional.absent());
    }

    /**
     * Initialization method needed for recreating a new {@link UpdatableWorld} for each test for performing a clean
     * test.
     */
    @Before
    public void initializeWorld() {
        this.world = new WorldFactoryImpl().create();
    }

    /**
     * Test for the collision of the {@link Player} with a {@link PowerUp} with {@link PowerUpType#INVINCIBILITY} power.
     */
    @Test
    public void playerGoalCollisionTest() {
        final Pair<Double, Double> playerPosition = this.playerProperties.getPosition();
        final EntityProperties goalProperties 
            = new EntityPropertiesImpl(EntityType.POWERUP, BodyShape.RECTANGLE,
                                       playerPosition.getLeft() + POWER_UP_DIMENSION / 2 + PLAYER_DIMENSION / 2,
                                       playerPosition.getRight(), POWER_UP_DIMENSION, POWER_UP_DIMENSION, ANGLE,
                                       Optional.of(PowerUpType.GOAL), Optional.absent());
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, goalProperties));
        assertFalse(NOT_STARTED_MSG, this.world.hasPlayerWon());
        this.world.update();
        assertTrue(NO_WIN, this.world.hasPlayerWon());
    }

    /**
     * Test for the fatal collision of the {@link Player} with a {@link RollingEnemy}.
     */
    @Test
    public void playerKilledByRollingEnemyCollisionTest() {
        final Pair<Double, Double> playerPosition = this.playerProperties.getPosition();
        final EntityProperties rollingEnemyProperties
            = new EntityPropertiesImpl(EntityType.ROLLING_ENEMY, BodyShape.CIRCLE, 
                                       playerPosition.getLeft() + ROLLING_ENEMY_DIMENSION / 2 + PLAYER_DIMENSION / 2,
                                       playerPosition.getRight(), ROLLING_ENEMY_DIMENSION, ROLLING_ENEMY_DIMENSION,
                                       ANGLE, Optional.absent(), Optional.absent());
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, rollingEnemyProperties));
        assertFalse(NOT_STARTED_MSG, this.world.isGameOver());
        this.world.update();
        assertTrue(NO_LOSE, this.world.isGameOver());
    }

    /**
     * Test for the collision of the {@link Player} with a {@link RollingEnemy} that kills the latter.
     */
    @Test
    public void playerKillingARollingEnemyCollisionTest() {
        final Pair<Double, Double> playerPosition = this.playerProperties.getPosition();
        final EntityProperties rollingEnemyProperties 
            = new EntityPropertiesImpl(EntityType.ROLLING_ENEMY, BodyShape.CIRCLE, playerPosition.getLeft(),
                                       playerPosition.getRight() - PLAYER_DIMENSION / 2 - ROLLING_ENEMY_DIMENSION / 2,
                                       ROLLING_ENEMY_DIMENSION, ROLLING_ENEMY_DIMENSION, ANGLE, Optional.absent(),
                                       Optional.absent());
        final EntityProperties playerJumpingProperties
            = new EntityPropertiesImpl(EntityType.PLAYER, BodyShape.RECTANGLE, WORLD_WIDTH / 2,
                                       playerPosition.getRight() + PLATFORM_HEIGHT / 2 + ROLLING_ENEMY_DIMENSION 
                                       + PLAYER_DIMENSION / 2,
                                       PLAYER_DIMENSION,
                                       PLAYER_DIMENSION,
                                       ANGLE,
                                       Optional.absent(),
                                       Optional.absent());
        this.world.initLevel(Arrays.asList(this.platformProperties, playerJumpingProperties, rollingEnemyProperties));
        while (this.world.getAliveEntities().size() == 3) {
            this.world.update();
        }
        assertEquals(TWO_ALIVE_ENTITIES, 2, this.world.getAliveEntities().size());
        assertEquals(ONE_DEAD_ENTITY, 1, this.world.getDeadEntities().size());
        assertTrue(ROLLING_DEAD, this.world.getDeadEntities().stream().allMatch(e -> e.getType() == EntityType.ROLLING_ENEMY));
    }

    /**
     * Test for the fatal collision of the {@link Player} with a {@link WalkingEnemy}.
     */
    @Test
    public void playerKilledByWalkingEnemyCollisionTest() {
        final Pair<Double, Double> playerPosition = this.playerProperties.getPosition();
        final EntityProperties walkingEnemyProperties
            = new EntityPropertiesImpl(EntityType.WALKING_ENEMY, BodyShape.RECTANGLE, 
                                       playerPosition.getLeft() + WALKING_ENEMY_DIMENSION / 2 + PLAYER_DIMENSION / 2,
                                       playerPosition.getRight(), WALKING_ENEMY_DIMENSION, WALKING_ENEMY_DIMENSION, ANGLE,
                                       Optional.absent(), Optional.of(WALKING_RANGE));
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, walkingEnemyProperties));
        assertFalse(NOT_STARTED_MSG, this.world.isGameOver());
        this.world.update();
        assertTrue(NO_LOSE, this.world.isGameOver());
    }

    /**
     * Test for the collision of the {@link Player} with a {@link WalkingEnemy} that kills the latter.
     */
    @Test
    public void playerKillingAWalkingEnemyCollisionTest() {
        final Pair<Double, Double> playerPosition = this.playerProperties.getPosition();
        final EntityProperties walkingEnemyProperties
            = new EntityPropertiesImpl(EntityType.WALKING_ENEMY, BodyShape.RECTANGLE, WORLD_WIDTH / 2,
                                       playerPosition.getRight() + PLATFORM_HEIGHT / 2 + WALKING_ENEMY_DIMENSION / 2,
                                       WALKING_ENEMY_DIMENSION, WALKING_ENEMY_DIMENSION, ANGLE, Optional.absent(),
                                       Optional.of(WALKING_RANGE));
        final EntityProperties playerJumpingProperties
            = new EntityPropertiesImpl(EntityType.PLAYER, BodyShape.RECTANGLE, WORLD_WIDTH / 2,
                                       playerPosition.getRight() + PLATFORM_HEIGHT / 2 + WALKING_ENEMY_DIMENSION
                                       + PLAYER_DIMENSION / 2,
                                       PLAYER_DIMENSION,
                                       PLAYER_DIMENSION,
                                       ANGLE,
                                       Optional.absent(),
                                       Optional.absent());
        this.world.initLevel(Arrays.asList(this.platformProperties, playerJumpingProperties, walkingEnemyProperties));
        while (this.world.getAliveEntities().size() == 3) {
            this.world.update();
        }
        assertEquals(TWO_ALIVE_ENTITIES, 2, this.world.getAliveEntities().size());
        assertEquals(ONE_DEAD_ENTITY, 1, this.world.getDeadEntities().size());
        assertTrue(WALKING_DEAD, this.world.getDeadEntities().stream().allMatch(e -> e.getType() == EntityType.WALKING_ENEMY));
    }

    /**
     * Test for the collision of the {@link Player} with a {@link PowerUp} with {@link PowerUpType#EXTRA_LIFE}.
     */
    @Test
    public void playerCollisionWithExtraLifeTest() {
        final Pair<Double, Double> playerPosition = this.playerProperties.getPosition();
        final EntityProperties extraLifeProperties 
            = new EntityPropertiesImpl(EntityType.POWERUP, BodyShape.RECTANGLE,
                                       playerPosition.getLeft() + PLAYER_DIMENSION / 2 + POWER_UP_DIMENSION / 2,
                                       playerPosition.getRight(), POWER_UP_DIMENSION, POWER_UP_DIMENSION, ANGLE,
                                       Optional.of(PowerUpType.EXTRA_LIFE), Optional.absent());
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, extraLifeProperties));
        final int playerLives = this.world.getPlayerLives();
        this.world.update();
        assertEquals(EXTRA_LIFE, playerLives + 1, this.world.getPlayerLives());
    }
}
