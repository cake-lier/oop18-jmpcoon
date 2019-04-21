package model.world;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.queue.UnmodifiableQueue;
import org.apache.commons.lang3.tuple.ImmutablePair;

import model.ClassToInstanceMultimap;
import model.ClassToInstanceMultimapImpl;
import model.entities.MovementType;
import model.entities.EnemyGenerator;
import model.entities.Entity;
import model.entities.EntityProperties;
import model.entities.EntityType;
import model.entities.Ladder;
import model.entities.Platform;
import model.entities.Player;
import model.entities.PowerUp;
import model.entities.RollingEnemy;
import model.entities.UnmodifiableEntity;
import model.entities.UnmodifiableEntityImpl;
import model.entities.EntityState;
import model.entities.WalkingEnemy;
import model.physics.PhysicalBody;
import model.physics.PhysicsUtils;
import model.physics.UpdatablePhysicalWorld;
import model.physics.PhysicalFactory;
import model.physics.PhysicalFactoryImpl;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
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
    private static final String NO_INIT_MSG = "It's needed to initialize this world by initLevel() before using it";

    private final PhysicalFactory physicsFactory;
    private final UpdatablePhysicalWorld innerWorld;
    private final Pair<Double, Double> worldDimensions;
    private final ClassToInstanceMultimap<Entity> aliveEntities;
    private final ClassToInstanceMultimap<Entity> deadEntities;
    private final Queue<CollisionEvent> currentEvents;
    private Optional<Player> player;
    private GameState currentState;
    private boolean initialized;
    private boolean jump;
    private int score;

    /**
     * Default constructor, decides what are the dimensions of this {@link World}, which should be 8m by 4.5m. It's package
     * protected because the only class that should access this constructor is its factory {@link WorldFactory}.
     */
    WorldImpl() {
        this.physicsFactory = new PhysicalFactoryImpl();
        this.worldDimensions = new ImmutablePair<>(WORLD_WIDTH, WORLD_HEIGHT);
        this.innerWorld = this.physicsFactory.createPhysicalWorld(this, this.worldDimensions.getLeft(), 
                                                                  this.worldDimensions.getRight());
        this.aliveEntities = new ClassToInstanceMultimapImpl<>(MultimapBuilder.linkedHashKeys().linkedHashSetValues().build());
        this.deadEntities = new ClassToInstanceMultimapImpl<>(MultimapBuilder.linkedHashKeys().linkedHashSetValues().build());
        this.currentEvents = new LinkedList<>();
        this.currentState = GameState.IS_GOING;
        this.player = Optional.absent();
        this.score = 0;
        this.initialized = false;
        this.jump = false;
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
            final EntityCreator creator = Arrays.asList(EntityCreator.values()).stream()
                                                .filter(et -> et.getAssociatedType() == entity.getEntityType())
                                                .findFirst()
                                                .get();
            final Class<? extends Entity> entityClass = creator.getAssociatedClass();
            this.aliveEntities.put(entityClass, creator.getEntityBuilder()
                                                       .setFactory(this.physicsFactory)
                                                       .setDimensions(entity.getDimensions())
                                                       .setAngle(entity.getAngle())
                                                       .setPosition(entity.getPosition())
                                                       .setShape(entity.getEntityShape())
                                                       .setPowerUpType(entity.getPowerUpType())
                                                       .setWalkingRange(entity.getWalkingRange())
                                                       .setWorld(entity.getEntityType() == EntityType.ENEMY_GENERATOR
                                                                 ? Optional.of(this)
                                                                 : Optional.absent())
                                                       .build());
            if (entity.getEntityType() == EntityType.PLAYER) {
                this.player = Optional.fromJavaUtil(this.aliveEntities.getInstances(Player.class).stream().findFirst());
            }
        });
        this.initialized = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addGeneratedRollingEnemy(final RollingEnemy generatedEnemy) {
        this.aliveEntities.putInstance(RollingEnemy.class, generatedEnemy);
    }

    /**
     * {@inheritDoc}
     * For first, it checks if the game has currently ended or not by checking if during this step the {@link Player} is no 
     * longer alive and has lost or if the "end level trigger" was reached and has consequently won. Then it separates all
     * {@link Entity}s no longer alive from the others and for last it signals to all {@link model.entities.EnemyGenerator}s 
     * that a lapse of time has passed and asking if they have created any new {@link model.entities.RollingEnemy}.
     * The synchronization is required because an update of the {@link World} cannot be interleaved with a user movement,
     * otherwise these two operations could interfere and make the state of the {@link Player} entity inconsistent.
     */
    public synchronized void update() {
        this.checkInitialization();
        this.currentEvents.clear();
        this.deadEntities.clear();
        this.innerWorld.update();
        final Iterator<Map.Entry<Class<? extends Entity>, Entity>> iterator = this.aliveEntities.entries().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<Class<? extends Entity>, Entity> current = iterator.next();
            if (!current.getValue().isAlive()) {
                this.deadEntities.put(current.getKey(), current.getValue());
                iterator.remove();
                this.innerWorld.removeBody(current.getValue().getPhysicalBody());
            }
        }
        if (this.currentState == GameState.IS_GOING && this.player.isPresent() && !this.player.get().isAlive()) {
            this.currentState = GameState.GAME_OVER;
        }
        this.aliveEntities.getInstances(WalkingEnemy.class).forEach(WalkingEnemy::computeMovement);
        this.aliveEntities.getInstances(EnemyGenerator.class).forEach(EnemyGenerator::onTimeAdvanced);
    }

    /**
     * {@inheritDoc}
     * The synchronization is required because an update of the {@link World} cannot be interleaved with a user movement,
     * otherwise these two operations could interfere and make the state of the {@link Player} entity inconsistent.
     */
    @Override
    public synchronized boolean movePlayer(final MovementType movement) {
        this.checkInitialization();
        if (this.player.isPresent()) {
            final PhysicalBody playerBody = this.player.get().getPhysicalBody();
            final EntityState playerState = playerBody.getState();
            final Predicate<PhysicalBody> isPlayerAtBottom = ladderBody -> PhysicsUtils.isBodyAtBottomHalf(playerBody, 
                                                                                                           ladderBody);
            if (this.currentState == GameState.IS_GOING 
                && ((movement == MovementType.JUMP && this.isBodyStanding(playerBody)) 
                    || (movement == MovementType.CLIMB_UP 
                        && (this.isBodyInFrontLadder(playerBody, isPlayerAtBottom)
                            || (playerState == EntityState.CLIMBING_UP || playerState == EntityState.CLIMBING_DOWN)))
                    || (movement == MovementType.CLIMB_DOWN
                        && (this.isBodyInFrontLadder(playerBody, isPlayerAtBottom.negate())
                            || (playerState == EntityState.CLIMBING_UP || playerState == EntityState.CLIMBING_DOWN)))
                    || ((movement == MovementType.MOVE_LEFT || movement == MovementType.MOVE_RIGHT)
                        && (playerState != EntityState.CLIMBING_DOWN && playerState != EntityState.CLIMBING_UP)))) {
                if (movement == MovementType.JUMP) {
                    this.jump = true;
                }
                this.player.get().move(movement);
                return true;
            }
        }
        return false;
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
    public Collection<UnmodifiableEntity> getAliveEntities() {
        return Stream.concat(Stream.concat(this.getEntitiesStream(this.aliveEntities, 
                                                                  UnmodifiableEntityImpl::ofStaticEntity, 
                                                                  Arrays.asList(Platform.class, Ladder.class)),
                                           this.getDynamicEntitiesStream(this.aliveEntities)),
                             this.getPowerUpStream(this.aliveEntities))
                     .collect(ImmutableSet.toImmutableSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<UnmodifiableEntity> getDeadEntities() {
        return Stream.concat(this.getPowerUpStream(this.deadEntities), 
                             this.getDynamicEntitiesStream(this.deadEntities))
                     .collect(ImmutableSet.toImmutableSet());
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
    public void notifyCollision(final CollisionEvent collisionType) {
        switch (collisionType) {
            case ROLLING_ENEMY_KILLED:
                this.score += ROLLING_POINTS;
                break;
            case WALKING_ENEMY_KILLED:
                this.score += WALKING_POINTS;
                break;
            case GOAL_HIT:
                this.currentState = GameState.PLAYER_WON;
                break;
            case PLAYER_KILLED:
                this.currentState = GameState.GAME_OVER;
                break;
            default:
                break;
        }
        this.currentEvents.offer(collisionType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlayerLives() {
        this.checkInitialization();
        return this.player.isPresent() ? this.player.get().getLives() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Queue<CollisionEvent> getCurrentEvents() {
        return UnmodifiableQueue.unmodifiableQueue(this.currentEvents);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasJumpHappened() {
        final boolean jumpHappened = this.jump;
        this.jump = false;
        return jumpHappened;
    }

    private void checkInitialization() {
        if (!this.initialized) {
            throw new IllegalStateException(NO_INIT_MSG);
        }
    }

    /*
     * Gets if the Player is currently standing on a platform or not. This is true only if is currently in contact with
     * a Platform and the contact point is at the bottom of the player bounding box and at the top of the platform bounding box.
     */
    private boolean isBodyStanding(final PhysicalBody body) {
        final Collection<PhysicalBody> platformsBodies = this.aliveEntities.getInstances(Platform.class)
                                                                           .parallelStream()
                                                                           .map(Platform::getPhysicalBody)
                                                                           .collect(Collectors.toSet());
        /* 
         * If it's in contact with the platform, but still jumping, it means the player has yet to complete (or start)
         * the previous jump, so it isn't standing.
         */
        return this.innerWorld.getCollidingBodies(body)
                              .parallelStream()
                              .filter(collision -> platformsBodies.contains(collision.getLeft()))
                              .anyMatch(platformStand -> PhysicsUtils.isBodyOnTop(body, platformStand.getLeft(), 
                                                                                  platformStand.getRight()))
               && body.getState() != EntityState.JUMPING;
    }

    /*
     * Gets if the Player is currently standing in front of a ladder and it could be specified where to check the player
     * is with respect to the ladder.
     */
    private boolean isBodyInFrontLadder(final PhysicalBody body, final Predicate<PhysicalBody> where) {
        return this.aliveEntities.getInstances(Ladder.class).parallelStream()
                                                            .map(Ladder::getPhysicalBody)
                                                            .anyMatch(ladderBody -> 
                                                                      this.innerWorld.areBodiesInContact(body, ladderBody)
                                                                      && where.test(ladderBody)
                                                                      && PhysicsUtils.isBodyInside(body, ladderBody));
    }

    private Stream<UnmodifiableEntity> getDynamicEntitiesStream(final ClassToInstanceMultimap<Entity> multimap) {
        return this.getEntitiesStream(multimap,
                                      UnmodifiableEntityImpl::ofDynamicEntity,
                                      Arrays.asList(Player.class, RollingEnemy.class, WalkingEnemy.class));
    }

    private <E extends Entity> Stream<UnmodifiableEntity> getEntitiesStream(final ClassToInstanceMultimap<Entity> multimap,
                                                                            final Function<E, UnmodifiableEntity> mapper,
                                                                            final Collection<Class<? extends E>> keys) {
        return keys.parallelStream().flatMap(type -> this.getEntityKeyStream(multimap, mapper, type));
    }

    private Stream<UnmodifiableEntity> getPowerUpStream(final ClassToInstanceMultimap<Entity> multimap) {
        return this.getEntityKeyStream(multimap, UnmodifiableEntityImpl::ofPowerUp, PowerUp.class);
    }

    private <E extends Entity> Stream<UnmodifiableEntity> getEntityKeyStream(final ClassToInstanceMultimap<Entity> multimap,
                                                                             final Function<E, UnmodifiableEntity> mapper,
                                                                             final Class<? extends E> key) {
        return multimap.getInstances(key).parallelStream().map(mapper::apply);
    }
}
