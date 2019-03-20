package model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import model.entities.Entity;
import model.entities.EntityCreator;
import model.entities.EntityFactory;
import model.entities.EntityProperties;
import model.entities.EntityType;
import model.entities.Platform;
import model.entities.Player;
import model.entities.PowerUp;
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
    private final ClassToInstanceMultimap<Entity> entities;
    private Player player;

    /**
     * Default constructor, delegates the job of managing the physics of the game to the library underneath and decides the size
     * of itself in meters.
     */
    public WorldImpl() {
        final PhysicsFactory physicsFactory = new PhysicsFactoryImpl();
        this.entityFactory = new EntityFactory(physicsFactory);
        this.innerWorld = physicsFactory.createWorld(WORLD_WIDTH, WORLD_HEIGHT);
        this.entities = new ClassToInstanceMultimapImpl<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getDimensions() {
        return new ImmutablePair<>(WORLD_WIDTH, WORLD_HEIGHT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initLevel(final Collection<EntityProperties> entities) {
        entities.forEach(entity -> {
            final EntityCreator creator = EntityCreator.valueOf(entity.getEntityType().name());
            if (entity.getEntityType() != EntityType.PLAYER) {
                final Class<? extends Entity> entityClass = creator.getAssociatedClass();
                this.entities.put(entityClass, creator.create(this.entityFactory));
            } else {
                this.player = this.entityFactory.createPlayer();
            }
        });
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
        if (movement != MovementType.JUMP || this.isPlayerStanding()) {
            this.movePlayer(movement);
        }
    }

    /**
     * {@inheritDoc}
     * This is done by checking whether the {@link Player} is still alive or not.
     */
    @Override
    public boolean isGameOver() {
        return !this.player.isAlive();
    }

    /**
     * {@inheritDoc}
     * This is done by checking whether the {@link Player} is touching the "end level trigger" entity.
     */
    @Override
    public boolean hasPlayerWon() {
        return this.innerWorld.arePhysicalBodiesInContact(this.player.getInternalPhysicalBody(),
                                                          this.entities.getInstances(PowerUp.class).stream()
                                                                                                   .findFirst()
                                                                                                   .get()
                                                                                                   .getInternalPhysicalBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Entity> getEntities() {
        return this.entities.values();
    }
}
