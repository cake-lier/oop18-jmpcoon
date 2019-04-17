package model.entities;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import model.physics.BodyShape;
import model.physics.PhysicalBody;
import model.physics.PhysicalFactory;
import model.physics.StaticPhysicalBody;
import model.world.World;

/**
 * An enemy generator inside the {@link model.world.World} of the game.
 * It creates a new instance of the {@link RollingEnemy} on a regular interval of time.
 */
public final class EnemyGenerator extends StaticEntity {

    private static final long serialVersionUID = -3160192139428572083L;
    private static final ImmutablePair<Double, Double> ROLLING_ENEMY_DIMENSIONS = new ImmutablePair<Double, Double>(0.23, 0.23);
    private static final int DELTA = 380;

    private final List<RollingEnemy> enemies;
    private int count;
    private final World world;
    private final PhysicalFactory factory;

    /**
     * Creates a new {@link EnemyGenerator} with the given {@link StaticPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link AbstractEntityBuilder} when creating a new instance of it and no one else.
     * @param body the {@link StaticPhysicalBody} that should be contained in this {@link EnemyGenerator}
     * @param world the world where the {@link RollingEnemy} generated will live
     * @param factory the {@link PhysicalFactory} that generates the RollingEnemy {@link PhysicalBody}
     */
    public EnemyGenerator(final StaticPhysicalBody body, final World world, final PhysicalFactory factory) {
        super(body);
        this.world = world;
        this.factory = factory;
        this.enemies = new LinkedList<>();
        this.count = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return EntityType.ENEMY_GENERATOR;
    }

    //TODO: something better?
    /**
     * @param physicsFactory the {@link PhysicsFactory}
     * @return a {@link Collection} containing the enemies
     */
    public Collection<RollingEnemy> onTimeAdvanced() {
        this.enemies.clear();
        if (this.checkTime()) {
            this.enemies.add(this.createRollingEnemy());
            this.enemies.get(0).applyImpulse();
        }
        return this.enemies;
    }

    private boolean checkTime() {
        return this.count++ % DELTA == 0;
    }

    private RollingEnemy createRollingEnemy() {
        return EntityBuilderUtils.getRollingEnemyBuilder()
                                 .setFactory(this.factory)
                                 .setDimensions(ROLLING_ENEMY_DIMENSIONS)
                                 .setAngle(0.0)
                                 .setPosition(this.getPosition())
                                 .setShape(BodyShape.CIRCLE)
                                 .build();
    }
}
