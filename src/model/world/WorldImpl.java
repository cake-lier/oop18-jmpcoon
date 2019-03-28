package model.world;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import model.ClassToInstanceMultimap;
import model.ClassToInstanceMultimapImpl;
import model.entities.MovementType;
import model.entities.Entity;
import model.entities.EntityFactory;
import model.entities.EntityProperties;
import model.entities.EntityType;
import model.entities.Ladder;
import model.entities.Platform;
import model.entities.Player;
import model.physics.PhysicalBody;
import model.physics.PhysicalWorld;
import model.physics.PhysicalFactory;
import model.physics.PhysicalFactoryImpl;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.MultimapBuilder;

/**
 * The class implementation of {@link World}.
 */
public final class WorldImpl implements World {
    private static final double WORLD_WIDTH = 8;
    private static final double WORLD_HEIGHT = 4.5;
    private static final double WIN_ZONE_X = 0.37;
    private static final double WIN_ZONE_Y = 3.71;
    private static final double PRECISION = 0.001;

    private final EntityFactory entityFactory;
    private final PhysicalWorld innerWorld;
    private final Pair<Double, Double> worldDimensions;
    private final ClassToInstanceMultimap<Entity> aliveEntities;
    private final Set<Entity> deadEntities;
    private Player player;
    private GameState currentState;

    /**
     * Default constructor, delegates the job of managing the physics of the game to the library underneath and decides the size
     * of itself in meters.
     */
    public WorldImpl() {
        final PhysicalFactory physicsFactory = new PhysicalFactoryImpl();
        this.entityFactory = new EntityFactory(physicsFactory);
        this.worldDimensions = new ImmutablePair<>(WORLD_WIDTH, WORLD_HEIGHT);
        this.innerWorld = physicsFactory.createPhysicalWorld(this.worldDimensions.getLeft(), this.worldDimensions.getRight());
        this.aliveEntities = new ClassToInstanceMultimapImpl<>(MultimapBuilder.linkedHashKeys().linkedHashSetValues().build());
        this.deadEntities = new LinkedHashSet<>();
        this.currentState = GameState.IS_GOING;
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
            this.aliveEntities.put(entityClass, creator.create(this.entityFactory,
                                                          entity.getEntityType(),
                                                          entity.getPosition(),
                                                          entity.getDimensions().getLeft(), 
                                                          entity.getDimensions().getRight(),
                                                          entity.getAngle()));
            if (entity.getEntityType() == EntityType.PLAYER) {
                this.player = this.aliveEntities.getInstances(Player.class).stream().findFirst().get();
            }
        });
    }

    /**
     * {@inheritDoc}
     * For first, it checks if the game has currently ended or not by checking if during this step the {@link Player} is no longer
     * alive and has lost or if the "end level trigger" was reached and has consequently won. Then it separates all
     * {@link Entity}s no longer alive from the others and for last it signals to all {@link GeneratorEnemy}s that a lapse of time
     * has passed and asking if they have created any new {@link RollingEnemy}.
     */
    public void update() {
        this.innerWorld.update();
        if (this.currentState == GameState.IS_GOING) {
            if (!this.player.isAlive()) {
                this.currentState = GameState.GAME_OVER;
            }
            if (this.player.getPosition().getLeft() < WIN_ZONE_X && this.player.getPosition().getRight() > WIN_ZONE_Y) {
                this.currentState = GameState.PLAYER_WON;
            }
        }
        this.deadEntities.clear();
        final Iterator<Entity> iterator = this.aliveEntities.values().iterator();
        while (iterator.hasNext()) {
            final Entity current = iterator.next();
            if (!current.isAlive()) {
                this.deadEntities.add(current);
                iterator.remove();
                this.innerWorld.removeBody(current.getPhysicalBody());
            }
        }
        /*this.entities.getInstances(GeneratorEnemy.class).forEach(entity -> this.entities.putAll(RollingEnemy.class, 
                                                                                                entity.onTimeAdvanced()));*/
    }

    /*
     * Gets if the Player is currently standing on a platform or not. This is true only if is currently in contact with
     * a Platform and the contact point is at the bottom of the Player bounding box and at the top of the Platform bounding box.
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
                              .anyMatch(platformStand -> (innerPlayer.getPosition().getRight()
                                                          - innerPlayer.getDimensions().getRight() / 2)
                                                         - platformStand.getRight().getRight() < PRECISION
                                                         && platformStand.getRight().getRight()
                                                            - (platformStand.getLeft().getPosition().getRight()
                                                               + platformStand.getLeft().getDimensions().getRight() / 2)
                                                            < PRECISION);
    }

    /*
     * Gets if the Player is currently standing in front of a ladder or not, and this is true only if is currently in contact with
     * one of them.
     */
    private boolean isPlayerInFrontLadder() {
        return this.aliveEntities.getInstances(Ladder.class).parallelStream()
                                                       .map(ladder -> ladder.getPhysicalBody())
                                                       .anyMatch(ladderBody -> 
                                                                 this.innerWorld
                                                                     .areBodiesInContact(this.player.getPhysicalBody(), 
                                                                                         ladderBody));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void movePlayer(final MovementType movement) {
        if (this.currentState == GameState.IS_GOING  && ((movement == MovementType.JUMP && this.isPlayerStanding()) 
                                                         || (movement == MovementType.CLIMB_UP && this.isPlayerInFrontLadder())
                                                         || (movement != MovementType.JUMP && movement != MovementType.CLIMB_UP))) {
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
}
