package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import model.entities.EntityProperties;
import model.entities.EntityPropertiesImpl;
import model.entities.EntityType;
import model.entities.MovementType;
import model.entities.UnmodifiableEntity;
import model.physics.BodyShape;
import model.world.UpdatableWorld;
import model.world.WorldFactory;
import model.world.WorldFactoryImpl;

/**
 * Test class for {@link WorldImpl}.
 */
public class WorldTest {
    private static final double WORLD_WIDTH = 8;
    private static final double WORLD_HEIGHT = 4.5;
    private static final double PLATFORM_WIDTH = WORLD_WIDTH / 2;
    private static final double PLATFORM_HEIGHT = 0.29;
    private static final double PLAYER_DIMENSION = 0.3;
    private static final double LADDER_HEIGHT = 1;
    private static final double LADDER_WIDTH = 0.3;
    private static final double ANGLE = 0;
    private static final double PRECISION = 0.005;
    private static final int SHORT_UPDATE_STEPS = 10;
    private static final int LONG_UPDATE_STEPS = 100;
    private static final String WRONG_DIMENSIONS = "The world created had the wrong dimensions";
    private static final String NO_PLAYER = "No player was inserted in the world";
    private static final String PLAYER_MOVE = "The player shouldn't have moved when prompted";
    private static final String WRONG_ENTITIES_NUMBER = "A different number of entities were created";
    private static final String NO_PLAYER_RIGHT = "The player didn't move right";
    private static final String NO_PLAYER_LEFT = "The player didn't move left";
    private static final String NO_PLAYER_JUMP = "The player didn't jump";
    private static final String NO_PLAYER_CLIMB_UP = "The player didn't climb up when prompted";
    private static final String NO_PLAYER_CLIMB_DOWN = "The player didn't climb down when prompted";

    private final EntityProperties platformProperties;
    private final EntityProperties playerProperties;
    private final EntityProperties ladderProperties;
    private UpdatableWorld world;

    /**
     * Builds a new {@link WorldTest}.
     */
    public WorldTest() {
        this.platformProperties = new EntityPropertiesImpl(EntityType.PLATFORM, BodyShape.RECTANGLE, WORLD_WIDTH / 2, 
                                                           WORLD_HEIGHT / 2, PLATFORM_WIDTH, PLATFORM_HEIGHT, 
                                                           ANGLE, Optional.absent(), Optional.absent());
        this.playerProperties = new EntityPropertiesImpl(EntityType.PLAYER, BodyShape.RECTANGLE, WORLD_WIDTH / 2, 
                                                         WORLD_HEIGHT / 2 + PLATFORM_HEIGHT / 2 + PLAYER_DIMENSION / 2, 
                                                         PLAYER_DIMENSION, PLAYER_DIMENSION, ANGLE, Optional.absent(),
                                                         Optional.absent());
        this.ladderProperties = new EntityPropertiesImpl(EntityType.LADDER, BodyShape.RECTANGLE, WORLD_WIDTH / 2, 
                                                         WORLD_HEIGHT / 2 + LADDER_HEIGHT / 2, LADDER_WIDTH, 
                                                         LADDER_HEIGHT, ANGLE, Optional.absent(), Optional.absent());
    }

    /**
     * Initialization method for having a new {@link UpdatableWorld} every test.
     */
    @Before
    public void initializeWorld() {
        this.world = new WorldFactoryImpl().create();
    }

    /**
     * Test for the illegality of creating two {@link World}s from the same {@link WorldFactory}.
     */
    @Test(expected = IllegalStateException.class)
    public void failedCreationWorldTwiceTest() {
        final WorldFactory factory = new WorldFactoryImpl();
        IntStream.range(0, 2).forEach(i -> factory.create());
        factory.create();
    }

    /**
     * Test for the correct dimensions of the world.
     */
    @Test
    public void worldDimensionsTest() {
        assertEquals(WRONG_DIMENSIONS, this.world.getDimensions(), new ImmutablePair<>(WORLD_WIDTH, WORLD_HEIGHT));
    }

    /**
     * Test for the correct throwing of exception by a {@link WorldImpl} updating without initialization.
     */
    @Test(expected = IllegalStateException.class)
    public void worldUpdateWithoutInitializatonExceptionTest() {
        this.world.update();
    }

    /**
     * Test for the correct throwing of exception when trying to move the player inside a {@link WorldImpl} without 
     * initialization.
     */
    @Test(expected = IllegalStateException.class)
    public void worldMovePlayerWithoutInitializatonExceptionTest() {
        this.world.movePlayer(MovementType.MOVE_RIGHT);
    }

    /**
     * Test for the correct throwing of exception when trying to know the lives of the player inside a {@link WorldImpl} without
     *  initialization.
     */
    @Test(expected = IllegalStateException.class)
    public void worldPlayerLivesWithoutInitializatonExceptionTest() {
        this.world.getPlayerLives();
    }

    /**
     * Test for the correct creation of the entities inside the world.
     */
    @Test
    public void playerCreationTest() {
        final Pair<Double, Double> playerInitialPosition = this.playerProperties.getPosition();
        this.world.initLevel(Arrays.asList(this.playerProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer();
        if (!player.isPresent()) {
            fail(NO_PLAYER);
        }
        assertEquals(PLAYER_MOVE, playerInitialPosition.getLeft(), player.get().getPosition().getLeft(), PRECISION);
        assertEquals(PLAYER_MOVE, playerInitialPosition.getRight(), player.get().getPosition().getRight(), PRECISION);
    }

    /**
     * Test for the creation of the correct number of entities inside the world.
     */
    @Test
    public void numberOfEntitiesCreationTest() {
        final List<EntityProperties> entities = Arrays.asList(this.playerProperties, this.platformProperties,
                                                              this.ladderProperties);
        this.world.initLevel(entities);
        assertEquals(WRONG_ENTITIES_NUMBER, entities.size(), this.world.getAliveEntities().size());
    }

    /**
     * Test for the transmission of a movement to the right to the player inside the world.
     */
    @Test
    public void worldMovementRightTransmission() {
        final Pair<Double, Double> playerInitialPosition = this.playerProperties.getPosition();
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer();
        this.world.movePlayer(MovementType.MOVE_RIGHT);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertTrue(NO_PLAYER_RIGHT, playerInitialPosition.getLeft() < player.get().getPosition().getLeft());
    }

    /**
     * Test for the transmission of a movement to the left to the player inside the world.
     */
    @Test
    public void worldMovementLeftTransmission() {
        final Pair<Double, Double> playerInitialPosition = this.playerProperties.getPosition();
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer();
        this.world.movePlayer(MovementType.MOVE_LEFT);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertTrue(NO_PLAYER_LEFT, playerInitialPosition.getLeft() > player.get().getPosition().getLeft());
    }

    /**
     * Test for the transmission of a jump to the player inside the world.
     */
    @Test
    public void worldMovementJumpTransmission() {
        final Pair<Double, Double> playerInitialPosition = this.playerProperties.getPosition();
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer();
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        this.world.movePlayer(MovementType.JUMP);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertTrue(NO_PLAYER_JUMP, playerInitialPosition.getRight() < player.get().getPosition().getRight());
    }

    /**
     * Test for the block of the transmission of a climb up command to the player inside the world, when it isn't in front
     * of a ladder.
     */
    @Test
    public void worldMovementClimbUpWithoutLadderTransmission() {
        final Pair<Double, Double> playerInitialPosition = this.playerProperties.getPosition();
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer();
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        this.world.movePlayer(MovementType.CLIMB_UP);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertEquals(PLAYER_MOVE, playerInitialPosition.getLeft(), player.get().getPosition().getLeft(), PRECISION);
        assertEquals(PLAYER_MOVE, playerInitialPosition.getRight(), player.get().getPosition().getRight(), PRECISION);
    }

    /**
     * Test for the transmission of a climb up command to the player inside the world, when it is in front of a ladder.
     */
    @Test
    public void worldMovementClimbUpWithLadderTransmission() {
        final Pair<Double, Double> playerInitialPosition = this.playerProperties.getPosition();
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, this.ladderProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer();
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        this.world.movePlayer(MovementType.CLIMB_UP);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertTrue(NO_PLAYER_CLIMB_UP, playerInitialPosition.getRight() < player.get().getPosition().getRight());
    }

    /**
     * Test for the block of the transmission of a climb down command to the player inside the world, when it isn't in front
     * on a ladder.
     */
    @Test
    public void worldMovementClimbDownNotOnLadderTransmission() {
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, this.ladderProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer();
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        final Pair<Double, Double> playerInitialPosition = player.get().getPosition();
        this.world.movePlayer(MovementType.CLIMB_DOWN);
        for (int i = 0; i < LONG_UPDATE_STEPS; i++) {
            this.world.update();
        }
        assertEquals(PLAYER_MOVE, playerInitialPosition.getLeft(), player.get().getPosition().getLeft(), PRECISION);
        assertEquals(PLAYER_MOVE, playerInitialPosition.getRight(), player.get().getPosition().getRight(), PRECISION);
    }

    /**
     * Test for the transmission of a climb down command to the player inside the world, after climbing up on a ladder.
     */
    @Test
    public void worldMovementClimbDownOnLadderTransmission() {
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, this.ladderProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer();
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        this.world.movePlayer(MovementType.CLIMB_UP);
        for (int i = 0; i < LONG_UPDATE_STEPS; i++) {
            this.world.update();
        }
        final Pair<Double, Double> playerTopPosition = player.get().getPosition();
        this.world.movePlayer(MovementType.CLIMB_DOWN);
        for (int i = 0; i < LONG_UPDATE_STEPS; i++) {
            this.world.update();
        }
        assertTrue(NO_PLAYER_CLIMB_DOWN, playerTopPosition.getRight() > player.get().getPosition().getRight());
    }

    /**
     * Test for the block of the transmission of a movement to the right to the player while on a ladder.
     */
    @Test
    public void worldMovementRightOnLadderTransmission() {
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, this.ladderProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer();
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        this.world.movePlayer(MovementType.CLIMB_UP);
        for (int i = 0; i < 2 * LONG_UPDATE_STEPS; i++) {
            this.world.update();
        }
        final Pair<Double, Double> playerTopPosition = player.get().getPosition();
        this.world.movePlayer(MovementType.MOVE_RIGHT);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertEquals(PLAYER_MOVE, playerTopPosition.getLeft(),  player.get().getPosition().getLeft(), PRECISION);
        assertEquals(PLAYER_MOVE, playerTopPosition.getRight(), player.get().getPosition().getRight(), PRECISION);
    }

    /**
     * Test for the block of the transmission of a movement to the left to the player while on a ladder.
     */
    @Test
    public void worldMovementLeftOnLadderTransmission() {
        this.world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, this.ladderProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer();
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        this.world.movePlayer(MovementType.CLIMB_UP);
        for (int i = 0; i < 2 * LONG_UPDATE_STEPS; i++) {
            this.world.update();
        }
        final Pair<Double, Double> playerTopPosition = player.get().getPosition();
        this.world.movePlayer(MovementType.MOVE_LEFT);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            this.world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertEquals(PLAYER_MOVE, playerTopPosition.getLeft(), player.get().getPosition().getLeft(), PRECISION);
        assertEquals(PLAYER_MOVE, playerTopPosition.getRight(), player.get().getPosition().getRight(), PRECISION);
    }

    private Optional<UnmodifiableEntity> getPlayer() {
        return Optional.fromJavaUtil(this.world.getAliveEntities()
                                               .parallelStream()
                                               .filter(e -> e.getType() == EntityType.PLAYER)
                                               .findFirst());
    }
}
