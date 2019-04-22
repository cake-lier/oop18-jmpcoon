package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import model.entities.EntityProperties;
import model.entities.EntityPropertiesImpl;
import model.entities.EntityType;
import model.entities.PowerUpType;
import model.physics.BodyShape;
import model.world.UpdatableWorld;
import model.world.WorldFactoryImpl;

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

    private final EntityProperties platformProperties;
    private final EntityProperties playerProperties;
    private UpdatableWorld world;

    /**
     * Builds a new {@link WorldTest}.
     */
    public WorldCollisionsTest() {
        this.platformProperties = new EntityPropertiesImpl(EntityType.PLATFORM, 
                                                           BodyShape.RECTANGLE,
                                                           WORLD_WIDTH / 2, 
                                                           WORLD_HEIGHT / 2, 
                                                           PLATFORM_WIDTH, 
                                                           PLATFORM_HEIGHT, 
                                                           ANGLE, 
                                                           Optional.absent(),
                                                           Optional.absent());
        this.playerProperties = new EntityPropertiesImpl(EntityType.PLAYER, 
                                                         BodyShape.RECTANGLE,
                                                         WORLD_WIDTH / 2, 
                                                         WORLD_HEIGHT / 2 + PLATFORM_HEIGHT / 2 + PLAYER_DIMENSION / 2, 
                                                         PLAYER_DIMENSION, 
                                                         PLAYER_DIMENSION, 
                                                         ANGLE, 
                                                         Optional.absent(),
                                                         Optional.absent());
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
        final EntityProperties goalProperties = new EntityPropertiesImpl(EntityType.POWERUP, 
                                                                         BodyShape.RECTANGLE, 
                                                                         this.playerProperties.getPosition().getLeft()
                                                                         + POWER_UP_DIMENSION / 2 + PLAYER_DIMENSION / 2,
                                                                         this.playerProperties.getPosition().getRight(),
                                                                         POWER_UP_DIMENSION,
                                                                         POWER_UP_DIMENSION,
                                                                         ANGLE,
                                                                         Optional.of(PowerUpType.GOAL),
                                                                         Optional.absent());
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, goalProperties));
        assertFalse(NOT_STARTED_MSG, this.world.hasPlayerWon());
        this.world.update();
        assertTrue("The player is colliding with the goal but not winning", this.world.hasPlayerWon());
    }

    /**
     * Test for the fatal collision of the {@link Player} with a {@link RollingEnemy}.
     */
    @Test
    public void playerKilledByRollingEnemyCollisionTest() {
        final EntityProperties rollingEnemyProperties = new EntityPropertiesImpl(EntityType.ROLLING_ENEMY, 
                                                                                 BodyShape.CIRCLE, 
                                                                                 this.playerProperties.getPosition().getLeft()
                                                                                 + ROLLING_ENEMY_DIMENSION / 2 
                                                                                 + PLAYER_DIMENSION / 2,
                                                                                 this.playerProperties.getPosition().getRight(),
                                                                                 ROLLING_ENEMY_DIMENSION,
                                                                                 ROLLING_ENEMY_DIMENSION,
                                                                                 ANGLE,
                                                                                 Optional.absent(),
                                                                                 Optional.absent());
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, rollingEnemyProperties));
        assertFalse(NOT_STARTED_MSG, this.world.isGameOver());
        this.world.update();
        assertTrue("The player is colliding with the enemy but not losing", this.world.isGameOver());
    }

    /**
     * Test for the collision of the {@link Player} with a {@link RollingEnemy} that kills the latter.
     */
    @Test
    public void playerKillingARollingEnemyCollisionTest() {
        final EntityProperties rollingEnemyProperties = new EntityPropertiesImpl(EntityType.ROLLING_ENEMY, 
                                                                                 BodyShape.CIRCLE, 
                                                                                 this.playerProperties.getPosition().getLeft(),
                                                                                 this.playerProperties.getPosition().getRight()
                                                                                 - PLAYER_DIMENSION / 2 
                                                                                 - ROLLING_ENEMY_DIMENSION / 2,
                                                                                 ROLLING_ENEMY_DIMENSION,
                                                                                 ROLLING_ENEMY_DIMENSION,
                                                                                 ANGLE,
                                                                                 Optional.absent(),
                                                                                 Optional.absent());
        final EntityProperties playerJumpingProperties = new EntityPropertiesImpl(EntityType.PLAYER, 
                                                                                  BodyShape.RECTANGLE,
                                                                                  WORLD_WIDTH / 2,
                                                                                  this.platformProperties.getPosition().getRight()
                                                                                  + PLATFORM_HEIGHT / 2 
                                                                                  + ROLLING_ENEMY_DIMENSION
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
        assertEquals("The alive entities should be two", 2, this.world.getAliveEntities().size());
        assertEquals("There should be only one dead entity", 1, this.world.getDeadEntities().size());
        assertTrue("The dead entities should only be rolling enemies", 
                   this.world.getDeadEntities().stream().allMatch(e -> e.getType() == EntityType.ROLLING_ENEMY));
    }

    /**
     * Test for the fatal collision of the {@link Player} with a {@link WalkingEnemy}.
     */
    @Test
    public void playerKilledByWalkingEnemyCollisionTest() {
        final EntityProperties walkingEnemyProperties = new EntityPropertiesImpl(EntityType.WALKING_ENEMY, 
                                                                                 BodyShape.RECTANGLE, 
                                                                                 this.playerProperties.getPosition().getLeft()
                                                                                 + WALKING_ENEMY_DIMENSION / 2 
                                                                                 + PLAYER_DIMENSION / 2,
                                                                                 this.playerProperties.getPosition().getRight(),
                                                                                 WALKING_ENEMY_DIMENSION,
                                                                                 WALKING_ENEMY_DIMENSION,
                                                                                 ANGLE,
                                                                                 Optional.absent(),
                                                                                 Optional.of(WALKING_RANGE));
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, walkingEnemyProperties));
        assertFalse(NOT_STARTED_MSG, this.world.isGameOver());
        this.world.update();
        assertTrue("The player is colliding with the enemy but not losing", this.world.isGameOver());
    }

    /**
     * Test for the collision of the {@link Player} with a {@link WalkingEnemy} that kills the latter.
     */
    @Test
    public void playerKillingAWalkingEnemyCollisionTest() {
        final EntityProperties walkingEnemyProperties = new EntityPropertiesImpl(EntityType.WALKING_ENEMY, 
                                                                                 BodyShape.RECTANGLE, 
                                                                                 WORLD_WIDTH / 2,
                                                                                 this.platformProperties.getPosition().getRight()
                                                                                 + PLATFORM_HEIGHT / 2 
                                                                                 + WALKING_ENEMY_DIMENSION / 2,
                                                                                 WALKING_ENEMY_DIMENSION,
                                                                                 WALKING_ENEMY_DIMENSION,
                                                                                 ANGLE,
                                                                                 Optional.absent(),
                                                                                 Optional.of(WALKING_RANGE));
        final EntityProperties playerJumpingProperties = new EntityPropertiesImpl(EntityType.PLAYER, 
                                                                                  BodyShape.RECTANGLE,
                                                                                  WORLD_WIDTH / 2,
                                                                                  this.platformProperties.getPosition().getRight()
                                                                                  + PLATFORM_HEIGHT / 2 
                                                                                  + WALKING_ENEMY_DIMENSION
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
        assertEquals("The alive entities should be two", 2, this.world.getAliveEntities().size());
        assertEquals("There should be only one dead entity", 1, this.world.getDeadEntities().size());
        assertTrue("The dead entities should only be rolling enemies", 
                   this.world.getDeadEntities().stream().allMatch(e -> e.getType() == EntityType.WALKING_ENEMY));
    }

    /**
     * Test for the collision of the {@link Player} with a {@link PowerUp} with {@link PowerUpType#EXTRA_LIFE}.
     */
    @Test
    public void playerCollisionWithExtraLifeTest() {
        final EntityProperties extraLifeProperties = new EntityPropertiesImpl(EntityType.POWERUP,
                                                                              BodyShape.RECTANGLE,
                                                                              this.playerProperties.getPosition().getLeft()
                                                                              + PLAYER_DIMENSION / 2
                                                                              + POWER_UP_DIMENSION / 2,
                                                                              this.playerProperties.getPosition().getRight(),
                                                                              POWER_UP_DIMENSION,
                                                                              POWER_UP_DIMENSION,
                                                                              ANGLE,
                                                                              Optional.of(PowerUpType.EXTRA_LIFE),
                                                                              Optional.absent());
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, extraLifeProperties));
        final int playerLives = this.world.getPlayerLives();
        this.world.update();
        assertEquals("The player should have acquired a life", playerLives + 1, this.world.getPlayerLives());
    }
}
