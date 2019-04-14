package model.world;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import model.ClassToInstanceMultimap;
import model.ClassToInstanceMultimapImpl;
import model.entities.MovementType;
import model.entities.EnemyGenerator;
import model.entities.Entity;
import model.entities.EntityBuilderUtils;
import model.entities.EntityProperties;
import model.entities.EntityShape;
import model.entities.EntityType;
import model.entities.Ladder;
import model.entities.Platform;
import model.entities.Player;
import model.entities.RollingEnemy;
import model.entities.EntityState;
import model.entities.WalkingEnemy;
import model.physics.PhysicalBody;
import model.physics.PhysicalWorld;
import model.physics.PhysicsUtils;
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
    private static final double WIN_ZONE_X = 0.37;
    private static final double WIN_ZONE_Y = 3.71;
    private static final int ROLLING_POINTS = 50;
    private static final int WALKING_POINTS = 100;
    private static final String NO_INIT_MSG = "It's needed to initialize this world by initLevel() before using it";

    private final PhysicalFactory physicsFactory;
    private final PhysicalWorld innerWorld;
    private final Pair<Double, Double> worldDimensions;
    private final ClassToInstanceMultimap<Entity> aliveEntities;
    private final Set<Entity> deadEntities;
    private Optional<Player> player;
    private GameState currentState;
    private boolean initialized;
    private int score;

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
        this.player = Optional.absent();
        this.score = 0;
        this.initialized = false;
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
            this.aliveEntities.put(entityClass, creator.getEntityBuilder().setFactory(this.physicsFactory)
                                                                          .setDimensions(entity.getDimensions())
                                                                          .setAngle(entity.getAngle())
                                                                          .setPosition(entity.getPosition())
                                                                          .setShape(entity.getEntityShape())
                                                                          .build());
            if (entity.getEntityType() == EntityType.PLAYER) {
                this.player = Optional.fromJavaUtil(this.aliveEntities.getInstances(Player.class).stream().findFirst());
            }
        });

        //TODO: delete once the level is fixed; it'll be left to make the program work
        /*this.aliveEntities.put(EnemyGenerator.class, EntityBuilderUtils.getEnemyGeneratorBuilder()
                                                                       .setFactory(this.physicsFactory)
                                                                       .setDimensions(new ImmutablePair<Double, Double>(0.23, 0.23))
                                                                       .setAngle(0.0)
                                                                       .setPosition(new ImmutablePair<Double, Double>(0.37, 4.50))
                                                                       .setShape(EntityShape.CIRCLE)
                                                                       .build());*/
        this.initialized = true;
    }

    private void checkInitialization() {
        if (!this.initialized) {
            throw new IllegalStateException(NO_INIT_MSG);
        }
    }

    /**
     * {@inheritDoc}
     * For first, it checks if the game has currently ended or not by checking if during this step the {@link Player} is no longer
     * alive and has lost or if the "end level trigger" was reached and has consequently won. Then it separates all
     * {@link Entity}s no longer alive from the others and for last it signals to all {@link model.entities.EnemyGenerator}s that
     * a lapse of time has passed and asking if they have created any new {@link model.entities.RollingEnemy}.
     * The synchronization is required because an update of the {@link World} cannot be interleaved with a user movement,
     * otherwise these two operations could interfere and make the state of the {@link Player} entity inconsistent.
     */
    public synchronized void update() {
        this.checkInitialization();
        this.innerWorld.update();
        if (this.currentState == GameState.IS_GOING && this.player.isPresent()) {
            if (!this.player.get().isAlive()) {
                this.currentState = GameState.GAME_OVER;
            }
            if (this.player.get().getPosition().getLeft() < WIN_ZONE_X && this.player.get().getPosition().getRight() > WIN_ZONE_Y) {
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

        this.aliveEntities.getInstances(WalkingEnemy.class).forEach(WalkingEnemy::computeMovement);
        this.aliveEntities.getInstances(EnemyGenerator.class).forEach(entity -> this.aliveEntities.putAll(RollingEnemy.class, 
                                                                                                entity.onTimeAdvanced(this.physicsFactory)));
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
        return this.innerWorld.getCollidingBodies(body)
                              .parallelStream()
                              .filter(collision -> platformsBodies.contains(collision.getLeft()))
                              .anyMatch(platformStand -> PhysicsUtils.isBodyOnTop(body, platformStand.getLeft(), 
                                                                                  platformStand.getRight()));
    }

    /*
     * Gets if the Player is currently standing in front of a ladder and it could be specified where to check the player is
     * with respect to the ladder.
     */
    private boolean isBodyInFrontLadder(final PhysicalBody body, final Predicate<PhysicalBody> where) {
        return this.aliveEntities.getInstances(Ladder.class).parallelStream()
                                                            .map(Ladder::getPhysicalBody)
                                                            .anyMatch(ladderBody -> this.innerWorld
                                                                                        .areBodiesInContact(body, ladderBody)
                                                                                    && where.test(ladderBody));
    }

    /**
     * {@inheritDoc}
     * The synchronization is required because an update of the {@link World} cannot be interleaved with a user movement,
     * otherwise these two operations could interfere and make the state of the {@link Player} entity inconsistent.
     */
    @Override
    public synchronized void movePlayer(final MovementType movement) {
        this.checkInitialization();
        if (this.player.isPresent()) {
            final PhysicalBody playerBody = this.player.get().getPhysicalBody();
            final EntityState playerState = playerBody.getState();
            final Predicate<PhysicalBody> isPlayerAtBottom = ladderBody -> PhysicsUtils.isBodyAtBottomHalf(playerBody, ladderBody);
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
                this.player.get().move(movement);
            }
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
    public Collection<UnmodifiableEntity> getAliveEntities() {
        return this.aliveEntities.values().parallelStream()
                                          .map(UnmodifiableEntity::new)
                                          .collect(ImmutableSet.toImmutableSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<UnmodifiableEntity> getDeadEntities() {
        return this.deadEntities.parallelStream()
                                .map(UnmodifiableEntity::new)
                                .collect(ImmutableSet.toImmutableSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentScore() {
        return this.score;
    }
}
