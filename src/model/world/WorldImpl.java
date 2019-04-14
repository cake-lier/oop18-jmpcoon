package model.world;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import model.ClassToInstanceMultimap;
import model.ClassToInstanceMultimapImpl;
import model.entities.MovementType;
import model.entities.Entity;
import model.entities.EntityProperties;
import model.entities.EntityShape;
import model.entities.EntityType;
import model.entities.Ladder;
import model.entities.Platform;
import model.entities.Player;
import model.entities.PowerUp;
import model.entities.PowerUpManager;
import model.entities.PowerUpType;
import model.entities.State;
import model.physics.PhysicalBody;
import model.physics.PhysicalWorld;
import model.physics.PhysicsUtils;
import model.physics.WholePhysicalWorld;
import model.physics.PhysicalFactory;
import model.physics.PhysicalFactoryImpl;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicate;
import com.google.common.collect.MultimapBuilder;

/**
 * The class implementation of {@link World}.
 */
public final class WorldImpl implements World {
    private static final long serialVersionUID = 4663479513512261181L;
    private static final double WORLD_WIDTH = 8;
    private static final double WORLD_HEIGHT = 4.5;
    private static final int ROLLING_POINTS = 50;
    private static final int WALKING_POINTS = 100;

    private final PhysicalFactory physicsFactory;
    private final PhysicalWorld innerWorld;
    private final Pair<Double, Double> worldDimensions;
    private final ClassToInstanceMultimap<Entity> aliveEntities;
    private final Set<Entity> deadEntities;
    private Player player;
    private GameState currentState;
    private int score;

    private transient Optional<PowerUpManager> powerUpManager; 

    /**
     * Default constructor, delegates the job of managing the physics of the game to the library underneath.
     */
    public WorldImpl() {
        this.physicsFactory = new PhysicalFactoryImpl();
        this.worldDimensions = new ImmutablePair<>(WORLD_WIDTH, WORLD_HEIGHT);
        this.innerWorld = physicsFactory.createPhysicalWorld(this.worldDimensions.getLeft(), this.worldDimensions.getRight());
        this.aliveEntities = new ClassToInstanceMultimapImpl<>(MultimapBuilder.linkedHashKeys().linkedHashSetValues().build());
        this.deadEntities = new LinkedHashSet<>();
        this.currentState = GameState.IS_GOING;
        this.score = 0;
        this.powerUpManager = Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getDimensions() {
        return this.worldDimensions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initLevel(final Collection<EntityProperties> entities) {
        entities.forEach(entity -> {
            final EntityCreator creator = EntityCreator.valueOf(entity.getEntityType().name());
            final Class<? extends Entity> entityClass = creator.getAssociatedClass();
            this.aliveEntities.put(entityClass, creator.getEntityBuilder().setFactory(this.physicsFactory)
                                                                          .setDimensions(entity.getDimensions())
                                                                          .setAngle(entity.getAngle())
                                                                          .setPosition(entity.getPosition())
                                                                          .setShape(entity.getEntityShape())
                                                                          .setPowerUpType(entity.getPowerUpType())
                                                                          .build());
            if (entity.getEntityType() == EntityType.PLAYER) {
                this.player = this.aliveEntities.getInstances(Player.class).stream().findFirst().get();
            }
        });
        List<PowerUp> powerups = this.aliveEntities.values().stream()
                                                            .filter(e -> e.getType() == EntityType.POWERUP)
                                                            .map((powerup) -> PowerUp.class.cast(powerup))
                                                            .collect(Collectors.toList());

        this.powerUpManager = Optional.of(new PowerUpManager(this.player, powerups));
        WholePhysicalWorld.class.cast(this.innerWorld).setManager(this.powerUpManager.get());
    }

    /**
     * {@inheritDoc}
     * For first, it checks if the game has currently ended or not by checking if during this step the {@link Player} is no longer
     * alive and has lost or if the "end level trigger" was reached and has consequently won. Then it separates all
     * {@link Entity}s no longer alive from the others and for last it signals to all {@link GeneratorEnemy}s that a lapse of time
     * has passed and asking if they have created any new {@link RollingEnemy}.
     * The synchronization is required because an update of the {@link World} cannot be interleaved with a user movement,
     * otherwise these two operations could interfere and make the state of the {@link Player} entity inconsistent.
     */
    public synchronized void update() {
        this.innerWorld.update();
        if (this.powerUpManager.isPresent() && this.currentState == GameState.IS_GOING) {
            PowerUpManager powerUpManager = this.powerUpManager.get();
            powerUpManager.checkPowerUps();
            if (!powerUpManager.isPlayerAlive()) {
                this.currentState = GameState.GAME_OVER;
            }
            if (powerUpManager.isGoalReached()) {
                this.currentState = GameState.PLAYER_WON;
            }
        }
        this.deadEntities.clear();
        final Iterator<Entity> iterator = this.aliveEntities.values().iterator();
        while (iterator.hasNext()) {
            final Entity current = iterator.next();
            if (!current.isAlive()) {
                this.deadEntities.add(current);
                final EntityType currentType = current.getType();
                if (currentType == EntityType.WALKING_ENEMY) {
                    this.score += WALKING_POINTS;
                } else if (currentType == EntityType.ROLLING_ENEMY) {
                    this.score += ROLLING_POINTS;
                }
                iterator.remove();
                this.innerWorld.removeBody(current.getPhysicalBody());
            }
        }
        /*this.entities.getInstances(GeneratorEnemy.class).forEach(entity -> this.entities.putAll(RollingEnemy.class, 
                                                                                                entity.onTimeAdvanced()));*/
    }

    /*
     * Gets if the Player is currently standing on a platform or not. This is true only if is currently in contact with
     * a Platform and the contact point is at the bottom of the player bounding box and at the top of the platform bounding box.
     */
    private boolean isPlayerStanding() {
        final PhysicalBody innerPlayer = this.player.getPhysicalBody();
        final Collection<PhysicalBody> platformsBodies = this.aliveEntities.getInstances(Platform.class)
                                                                           .parallelStream()
                                                                           .map(platform -> platform.getPhysicalBody())
                                                                           .collect(Collectors.toSet());
        return this.innerWorld.getCollidingBodies(innerPlayer)
                              .parallelStream()
                              .filter(collision -> platformsBodies.contains(collision.getLeft()))
                              .anyMatch(platformStand -> PhysicsUtils.isBodyOnTop(innerPlayer,
                                                                                  platformStand.getLeft(),
                                                                                  platformStand.getRight()));
    }

    /*
     * Gets if the Player is currently standing in front of a ladder and it could be specified where to check the player is
     * with respect to the ladder.
     */
    private boolean isPlayerinFrontLadder(final Predicate<PhysicalBody> where) {
        return this.aliveEntities.getInstances(Ladder.class).parallelStream()
                                                            .map(ladder -> ladder.getPhysicalBody())
                                                            .anyMatch(ladderBody -> 
                                                                      this.innerWorld
                                                                          .areBodiesInContact(this.player.getPhysicalBody(),
                                                                                              ladderBody)
                                                                      && where.apply(ladderBody));
    }

    /*
     * Gets if the Player is currently standing in front of a ladder at its bottom or not.
     */
    private boolean isPlayerAtBottomLadder() {
        return this.isPlayerinFrontLadder(ladderBody -> PhysicsUtils.isBodyAtBottomHalf(this.player.getPhysicalBody(), ladderBody));
    }

    /*
     * Gets if the Player is currently standing in front of a ladder at its top or not.
     */
    private boolean isPlayerAtTopLadder() {
        return this.isPlayerinFrontLadder(ladderBody -> !PhysicsUtils.isBodyAtBottomHalf(this.player.getPhysicalBody(), ladderBody));
    }

    /**
     * {@inheritDoc}
     * The synchronization is required because an update of the {@link World} cannot be interleaved with a user movement,
     * otherwise these two operations could interfere and make the state of the {@link Player} entity inconsistent.
     */
    @Override
    public synchronized void movePlayer(final MovementType movement) {
        final State playerState = this.player.getPhysicalBody().getState();
        if (this.currentState == GameState.IS_GOING 
            && ((movement == MovementType.JUMP && this.isPlayerStanding()) 
                || (movement == MovementType.CLIMB_UP 
                    && (this.isPlayerAtBottomLadder() || (playerState == State.CLIMBING_UP || playerState == State.CLIMBING_DOWN)))
                || (movement == MovementType.CLIMB_DOWN
                    && (this.isPlayerAtTopLadder() || (playerState == State.CLIMBING_UP || playerState == State.CLIMBING_DOWN)))
                || ((movement == MovementType.MOVE_LEFT || movement == MovementType.MOVE_RIGHT)
                    && (playerState != State.CLIMBING_DOWN && playerState != State.CLIMBING_UP)))) {
            this.player.move(movement);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGameOver() {
        return this.currentState == GameState.GAME_OVER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPlayerWon() {
        return this.currentState == GameState.PLAYER_WON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Entity> getAliveEntities() {
        return this.aliveEntities.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Entity> getDeadEntities() {
        return this.deadEntities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentScore() {
        return this.score;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlayerLives() {
        return this.player.getLives();
    }
}
