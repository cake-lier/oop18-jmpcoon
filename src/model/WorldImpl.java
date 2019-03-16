package model;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import model.entities.Entity;
import model.entities.EntityFactory;
import model.entities.EntityProperties;
import model.entities.Player;
import utils.Pair;
import utils.PairImpl;

/**
 * The class implementation of {@link World}.
 */
public final class WorldImpl implements World {
    private static final double WORLD_WIDTH = 16;
    private static final double WORLD_HEIGHT = 9;

    private boolean gameOver;
    private final PhysicsFactory physicsFactory;
    private final EntityFactory entityFactory;
    private final PhysicalWorld innerWorld;
    private final ClassToInstanceMultimap<Entity> entities;
    /**
     * This class delegates the job of managing the physics of the game to the library
     * underneath and to do so, it wraps an instance of the chosen library World.
     */
    public WorldImpl() {
        this.physicsFactory = new PhysicsFactoryImpl();
        this.entityFactory = new EntityFactory(this.physicsFactory);
        this.innerWorld = this.physicsFactory.createWorld(WORLD_WIDTH, WORLD_HEIGHT);
        this.gameOver = false;
        this.entities = new ClassToInstanceMultimapImpl<>();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getDimensions() {
        return new PairImpl<>(WORLD_WIDTH, WORLD_HEIGHT);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void initLevel(final Collection<EntityProperties> entities) {
        entities.forEach(entity -> {
            final Class<? extends Entity> entityClass = entity.getEntityType()
                                                              .getTypeClass();
            try {
                this.entities.put(entityClass, 
                                  entityClass.cast(
                                      EntityFactory.class
                                                   .getMethod("create"
                                                              + entity.getEntityType()
                                                                      .getTypeName())
                                                   .invoke(this.entityFactory)));
            } catch (IllegalAccessException | IllegalArgumentException 
                     | InvocationTargetException | NoSuchMethodException 
                     | SecurityException ex) {
                ex.printStackTrace();
            }
        });
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void movePlayer(final MovementType movement) {
        this.entities.getInstances(Player.class)
                     .stream()
                     .findFirst()
                     .get()
                     .move(movement);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGameOver() {
        return this.gameOver;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Entity> getEntities() {
        return this.entities.values();
    }
}
