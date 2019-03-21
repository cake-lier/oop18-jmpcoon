package model.world;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import model.ClassToInstanceMultimap;
import model.ClassToInstanceMultimapImpl;
import model.MovementType;
import model.entities.Entity;
import model.entities.EntityFactory;
import model.entities.EntityProperties;
import model.entities.EntityType;
import model.entities.GeneratorEnemy;
import model.entities.Platform;
import model.entities.Player;
import model.entities.PowerUp;
import model.entities.RollingEnemy;
import model.physics.PhysicalBody;
import model.physics.PhysicalWorld;
import model.physics.PhysicsFactory;
import model.physics.PhysicsFactoryImpl;

import org.apache.commons.lang3.tuple.Pair;

/**
 * The class implementation of {@link World}.
 */
public final class WorldImpl implements World {
    private static final double WORLD_WIDTH = 8;
    private static final double WORLD_HEIGHT = 4.5;

    private final EntityFactory entityFactory;
    private final PhysicalWorld innerWorld;
    private final ImmutablePair<Double, Double> worldDimensions;
    private final ClassToInstanceMultimap<Entity> entities;
    private Player player;
    private GameState currentState;

    /**
     * Default constructor, delegates the job of managing the physics of the game to the library underneath and decides the size
     * of itself in meters.
     */
    public WorldImpl() {
        final PhysicsFactory physicsFactory = new PhysicsFactoryImpl();
        this.entityFactory = new EntityFactory(physicsFactory);
        this.worldDimensions = new ImmutablePair<>(WORLD_WIDTH, WORLD_HEIGHT);
        this.innerWorld = physicsFactory.createWorld(this.worldDimensions.getLeft(), this.worldDimensions.getRight());
        this.entities = new ClassToInstanceMultimapImpl<>();
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
            this.entities.put(entityClass, creator.create(this.entityFactory,
                                                          entity.getEntityType(),
                                                          entity.getEntityShape(),
                                                          entity.getPosition().getLeft(),
                                                          entity.getPosition().getRight(),
                                                          entity.getDimensions().getLeft(), 
                                                          entity.getDimensions().getRight(),
                                                          entity.getAngle()));
            if (entity.getEntityType() == EntityType.PLAYER) {
                this.player = this.entities.getInstances(Player.class).stream().findFirst().get();
            }
        });
    }

    /**
     * {@inheritDoc}
     * For first, it checks if the game has currently ended or not by checking if during this step the player is no longer alive
     * and has lost or if she has reached the "end level trigger" and has consequently won. Then it removes all {@link Entity}s
     * no longer alive and signaling to all {@link GeneratorEnemy}s that a lapse of time has passed and asking if they have
     * created any new {@link RollingEnemy}.
     */
    public void update() {
        if (this.currentState == GameState.IS_GOING) {
            if (!this.player.isAlive()) {
                this.currentState = GameState.GAME_OVER;
            }
            if (this.innerWorld.arePhysicalBodiesInContact(this.player.getInternalPhysicalBody(),
                                                           this.entities.getInstances(PowerUp.class).stream()
                                                                                                    .findFirst()
                                                                                                    .get()
                                                                                                    .getInternalPhysicalBody())) {
                this.currentState = GameState.PLAYER_WON;
            }
        }
        final Iterator<Entity> iterator = this.entities.values().iterator();
        while (iterator.hasNext()) {
            final Entity current = iterator.next();
            if (!current.isAlive()) {
                iterator.remove();
                this.innerWorld.removePhysicalBody(current.getInternalPhysicalBody());
            }
        }
        this.entities.getInstances(GeneratorEnemy.class).forEach(entity -> this.entities.putAll(RollingEnemy.class, 
                                                                                                entity.onTimeAdvanced()));
    }

    /*
     * Gets if the Player is currently standing on a platform or not. This is true only if is currently in contact with
     * a Platform and the contact point is at the bottom of the Player bounding box and at the top of the Platform bounding box.
     */
    private boolean isPlayerStanding() {
        final PhysicalBody innerPlayer = this.player.getInternalPhysicalBody();
        final Collection<PhysicalBody> platformsBodies = this.entities.getInstances(Platform.class)
                                                                      .parallelStream()
                                                                      .map(platform -> platform.getInternalPhysicalBody())
                                                                      .collect(Collectors.toSet());
        return this.innerWorld.collidingPhysicalBodies(innerPlayer)
                              .parallelStream()
                              .filter(collision -> platformsBodies.contains(collision.getLeft()))
                              .anyMatch(platformStand -> Double.compare(platformStand.getRight().getRight(),
                                                                        innerPlayer.getPosition().getLeft()
                                                                        - innerPlayer.getDimensions().getRight() / 2) == 0
                                                         && Double.compare(platformStand.getRight().getRight(),
                                                                           platformStand.getLeft().getPosition().getLeft()
                                                                           + platformStand.getLeft().getDimensions().getRight()
                                                                           / 2) == 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void movePlayer(final MovementType movement) {
        if (this.currentState == GameState.IS_GOING && (movement != MovementType.JUMP || this.isPlayerStanding())) {
            this.movePlayer(movement);
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
    public Collection<Entity> getEntities() {
        return this.entities.values();
    }
}
