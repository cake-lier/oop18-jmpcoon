package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import utils.Pair;
import utils.PairImpl;

/**
 * The class implementation of {@link World}.
 */
public final class WorldImpl implements World {
    private static final double WORLD_WIDTH = 16;
    private static final double WORLD_HEIGHT = 9;

    private boolean gameOver;
    private final PhysicsFactory factory;
    private final PhysicalWorld innerWorld;
    private final Collection<Entity> platforms;
    private final Collection<Entity> ladders;
    private final Collection<Entity> walkingEnemies;
    private final Collection<Entity> rollingEnemies;
    private final Collection<Entity> powerUps;
    private final Entity player;
    /**
     * This class delegates the job of managing the physics of the game to the library
     * underneath and to do so, it wraps an instance of the chosen library World.
     */
    public WorldImpl() {
        this.factory = new PhysicsFactoryImpl();
        this.innerWorld = this.factory.createWorld(WORLD_WIDTH, WORLD_HEIGHT);
        this.gameOver = false;
        this.platforms = new LinkedHashSet<>();
        this.ladders = new LinkedHashSet<>();
        this.powerUps = new LinkedHashSet<>();
        this.walkingEnemies = new LinkedHashSet<>();
        this.rollingEnemies = new LinkedHashSet<>();
        this.player = new Player();
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
    public void initLevel() {
        // TODO Auto-generated method stub
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void movePlayer(final MovementType movement) {
        // TODO Auto-generated method stub
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
    public EnumMap<EntityType, Collection<Entity>> getEntities() {
        final EnumMap<EntityType, Collection<Entity>> entityMap = new EnumMap<>(EntityType.class);
        entityMap.put(EntityType.LADDER, new LinkedHashSet<>(this.ladders));
        entityMap.put(EntityType.PLATFORM, new LinkedHashSet<>(this.platforms));
        entityMap.put(EntityType.PLAYER, new LinkedHashSet<>(new ArrayList<>(Arrays.asList(this.player))));
        entityMap.put(EntityType.POWERUP, new LinkedHashSet<>(this.powerUps));
        entityMap.put(EntityType.ROLLING_ENEMY, new LinkedHashSet<>(this.rollingEnemies));
        entityMap.put(EntityType.WALKING_ENEMY, new LinkedHashSet<>(this.walkingEnemies));
        return entityMap;
    }
}
