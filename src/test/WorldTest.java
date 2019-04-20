package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import com.google.common.base.Optional;

import model.entities.EntityProperties;
import model.entities.EntityPropertiesImpl;
import model.entities.EntityType;
import model.entities.MovementType;
import model.entities.UnmodifiableEntity;
import model.physics.BodyShape;
import model.world.UpdatableWorld;
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

    private final EntityProperties platformProperties;
    private final EntityProperties playerProperties;
    private final EntityProperties ladderProperties;

    /**
     * Builds a new {@link WorldTest}.
     */
    public WorldTest() {
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
        this.ladderProperties = new EntityPropertiesImpl(EntityType.LADDER, 
                                                         BodyShape.RECTANGLE,
                                                         WORLD_WIDTH / 2, 
                                                         WORLD_HEIGHT / 2 + LADDER_HEIGHT / 2, 
                                                         LADDER_WIDTH, 
                                                         LADDER_HEIGHT, 
                                                         ANGLE, 
                                                         Optional.absent(),
                                                         Optional.absent());
    }

    /**
     * Test for the correct dimensions of the world.
     */
    @Test
    public void worldDimensionsTest() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        assertEquals("The world created had the wrong dimensions", 
                     world.getDimensions(),
                     new ImmutablePair<>(WORLD_WIDTH, WORLD_HEIGHT));
    }

    /**
     * Test for the correct throwing of exception by a {@link WorldImpl} updating without initialization.
     */
    @Test(expected = IllegalStateException.class)
    public void worldUpdateWithoutInitializatonExceptionTest() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        world.update();
    }

    /**
     * Test for the correct throwing of exception when trying to move the player inside a {@link WorldImpl} without 
     * initialization.
     */
    @Test(expected = IllegalStateException.class)
    public void worldMovePlayerWithoutInitializatonExceptionTest() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        world.movePlayer(MovementType.MOVE_RIGHT);
    }

    /**
     * Test for the correct throwing of exception when trying to know the lives of the player inside a {@link WorldImpl} without
     *  initialization.
     */
    @Test(expected = IllegalStateException.class)
    public void worldPlayerLivesWithoutInitializatonExceptionTest() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        world.getPlayerLives();
    }

    /**
     * Test for the correct creation of the entities inside the world.
     */
    @Test
    public void playerCreationTest() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        final Pair<Double, Double> playerInitialPosition = this.playerProperties.getPosition();
        world.initLevel(Arrays.asList(this.playerProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer(world);
        if (!player.isPresent()) {
            fail("No player was inserted in the world");
        }
        assertEquals("The player moved when prompted",
                     playerInitialPosition.getLeft(), 
                     player.get().getPosition().getLeft(), 
                     PRECISION);
        assertEquals("The player moved when prompted",
                     playerInitialPosition.getRight(), 
                     player.get().getPosition().getRight(), 
                     PRECISION);
    }

    /**
     * Test for the creation of the correct number of entities inside the world.
     */
    @Test
    public void numberOfEntitiesCreationTest() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        final List<EntityProperties> entities = Arrays.asList(this.playerProperties, 
                                                              this.platformProperties, 
                                                              this.ladderProperties);
        world.initLevel(entities);
        assertEquals("A different number of entities were created", entities.size(), world.getAliveEntities().size());
    }

    /**
     * Test for the transmission of a movement to the right to the player inside the world.
     */
    @Test
    public void worldMovementRightTransmission() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        final Pair<Double, Double> playerInitialPosition = playerProperties.getPosition();
        world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer(world);
        world.movePlayer(MovementType.MOVE_RIGHT);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertTrue("The player didn't move right", playerInitialPosition.getLeft() < player.get().getPosition().getLeft());
    }

    /**
     * Test for the transmission of a movement to the left to the player inside the world.
     */
    @Test
    public void worldMovementLeftTransmission() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        final Pair<Double, Double> playerInitialPosition = playerProperties.getPosition();
        world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer(world);
        world.movePlayer(MovementType.MOVE_LEFT);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertTrue("The player didn't move left", playerInitialPosition.getLeft() > player.get().getPosition().getLeft());
    }

    /**
     * Test for the transmission of a jump to the player inside the world.
     */
    @Test
    public void worldMovementJumpTransmission() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        final Pair<Double, Double> playerInitialPosition = playerProperties.getPosition();
        world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer(world);
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        world.movePlayer(MovementType.JUMP);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertTrue("The player didn't jump", playerInitialPosition.getRight() < player.get().getPosition().getRight());
    }

    /**
     * Test for the transmission of a climb up command to the player inside the world, when it isn't in front of a ladder.
     */
    @Test
    public void worldMovementClimbUpWithoutLadderTransmission() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        final Pair<Double, Double> playerInitialPosition = playerProperties.getPosition();
        world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer(world);
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        world.movePlayer(MovementType.CLIMB_UP);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertEquals("The player moved when prompted",
                     playerInitialPosition.getLeft(), 
                     player.get().getPosition().getLeft(), 
                     PRECISION);
        assertEquals("The player moved when prompted",
                playerInitialPosition.getRight(), 
                player.get().getPosition().getRight(), 
                PRECISION);
    }

    /**
     * Test for the transmission of a climb up command to the player inside the world, when it is in front of a ladder.
     */
    @Test
    public void worldMovementClimbUpWithLadderTransmission() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        final Pair<Double, Double> playerInitialPosition = playerProperties.getPosition();
        world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, this.ladderProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer(world);
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        world.movePlayer(MovementType.CLIMB_UP);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertTrue("The player didn't climb up when prompted",
                   playerInitialPosition.getRight() < player.get().getPosition().getRight());
    }

    /**
     * Test for the transmission of a climb down command to the player inside the world, when it isn't on a ladder.
     */
    @Test
    public void worldMovementClimbDownNotOnLadderTransmission() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, this.ladderProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer(world);
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        final Pair<Double, Double> playerInitialPosition = player.get().getPosition();
        world.movePlayer(MovementType.CLIMB_DOWN);
        for (int i = 0; i < LONG_UPDATE_STEPS; i++) {
            world.update();
        }
        assertEquals("The player moved when prompted",
                     playerInitialPosition.getLeft(), 
                     player.get().getPosition().getLeft(), 
                     PRECISION);
        assertEquals("The player moved when prompted",
                     playerInitialPosition.getRight(), 
                     player.get().getPosition().getRight(), 
                     PRECISION);
    }

    /**
     * Test for the transmission of a climb down command to the player inside the world, when it is on a ladder.
     */
    @Test
    public void worldMovementClimbDownOnLadderTransmission() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, this.ladderProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer(world);
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        world.movePlayer(MovementType.CLIMB_UP);
        for (int i = 0; i < LONG_UPDATE_STEPS; i++) {
            world.update();
        }
        final Pair<Double, Double> playerTopPosition = player.get().getPosition();
        world.movePlayer(MovementType.CLIMB_DOWN);
        for (int i = 0; i < LONG_UPDATE_STEPS; i++) {
            world.update();
        }
        assertTrue("The player didn't climb down when prompted",
                   playerTopPosition.getRight() > player.get().getPosition().getRight());
    }

    /**
     * Test for the transmission of a movement to the right to the player while on a ladder.
     */
    @Test
    public void worldMovementRightOnLadderTransmission() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, this.ladderProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer(world);
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        world.movePlayer(MovementType.CLIMB_UP);
        for (int i = 0; i < 2 * LONG_UPDATE_STEPS; i++) {
            world.update();
        }
        final Pair<Double, Double> playerTopPosition = player.get().getPosition();
        world.movePlayer(MovementType.MOVE_RIGHT);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertEquals("The player moved when prompted",
                     playerTopPosition.getLeft(), 
                     player.get().getPosition().getLeft(), 
                     PRECISION);
        assertEquals("The player moved when prompted",
                     playerTopPosition.getRight(), 
                     player.get().getPosition().getRight(), 
                     PRECISION);
    }

    /**
     * Test for the transmission of a movement to the left to the player inside the world.
     */
    @Test
    public void worldMovementLeftOnLadderTransmission() {
        final UpdatableWorld world = new WorldFactoryImpl().create();
        world.initLevel(Arrays.asList(this.platformProperties, this.playerProperties, this.ladderProperties));
        final Optional<UnmodifiableEntity> player = this.getPlayer(world);
        /* updates to let the player fall and touch the platform, if it was created slightly above the platform*/
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        world.movePlayer(MovementType.CLIMB_UP);
        for (int i = 0; i < 2 * LONG_UPDATE_STEPS; i++) {
            world.update();
        }
        final Pair<Double, Double> playerTopPosition = player.get().getPosition();
        world.movePlayer(MovementType.MOVE_LEFT);
        for (int i = 0; i < SHORT_UPDATE_STEPS; i++) {
            world.update();
        }
        /* assuming player was created correctly, because previous tests said so */
        assertEquals("The player moved when prompted",
                     playerTopPosition.getLeft(), 
                     player.get().getPosition().getLeft(), 
                     PRECISION);
        assertEquals("The player moved when prompted",
                     playerTopPosition.getRight(), 
                     player.get().getPosition().getRight(), 
                     PRECISION);
    }

    private Optional<UnmodifiableEntity> getPlayer(final UpdatableWorld world) {
        return Optional.fromJavaUtil(world.getAliveEntities()
                                          .stream()
                                          .filter(e -> e.getType() == EntityType.PLAYER)
                                          .findFirst());
    }
}
