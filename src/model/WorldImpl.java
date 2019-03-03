package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import org.jbox2d.common.Vec2;
import utils.Pair;
import utils.PairImpl;

/**
 * The class implementation of {@link World}.
 */
public final class WorldImpl implements World {
    private static final float X_GRAVITY_ACC = 0;
    private static final float Y_GRAVITY_ACC = -9.81f;
    private static final double X_WORLD_SIZE = 16;
    private static final double Y_WORLD_SIZE = 9;

    private final org.jbox2d.dynamics.World world;
    private final double width;
    private final double height;
    private boolean gameOver;
    private final Collection<Entity> platforms;
    private final Collection<Entity> ladders;
    private final Collection<Entity> walkingEnemies;
    private final Collection<Entity> rollingEnemies;
    private final Collection<Entity> powerUps;
    private final Entity player;
    /**
     * This class delegates the job of managing the physics of the game to the library
     * underneath and to do so, it wraps an instance of {@link org.jbox2d.dynamics.World}.
     */
    public WorldImpl() {
        this.world = new org.jbox2d.dynamics.World(new Vec2(X_GRAVITY_ACC, Y_GRAVITY_ACC));
        this.width = X_WORLD_SIZE;
        this.height = Y_WORLD_SIZE;
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
        return new PairImpl<>(this.width, this.height);
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
