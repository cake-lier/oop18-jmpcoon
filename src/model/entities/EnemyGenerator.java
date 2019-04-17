package model.entities;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import model.physics.BodyShape;
import model.physics.PhysicalFactory;
import model.physics.StaticPhysicalBody;

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

    /**
     * Creates a new {@link EnemyGenerator} with the given {@link StaticPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link AbstractEntityBuilder} when creating a new instance of it and no one else.
     * @param body The {@link StaticPhysicalBody} that should be contained in this {@link EnemyGenerator}.
     */
    EnemyGenerator(final StaticPhysicalBody body) {
        super(body);
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
     * @return a Iterable of the enemies
     */
    public Collection<RollingEnemy> onTimeAdvanced(final PhysicalFactory physicsFactory) {
        this.enemies.clear();
        if (this.checkTime()) {
            this.enemies.add(this.createRollingEnemy(physicsFactory));
            this.enemies.get(0).applyImpulse();
        }
        return this.enemies;
    }

    private boolean checkTime() {
        return this.count++ % DELTA == 0;
    }

    //TODO: better way to give physicsFactory to RollingEnemy?
    private RollingEnemy createRollingEnemy(final PhysicalFactory physicsFactory) {
        return EntityBuilderUtils.getRollingEnemyBuilder()
                .setFactory(physicsFactory)
                .setDimensions(ROLLING_ENEMY_DIMENSIONS)
                .setAngle(0.0)
                .setPosition(this.getPosition())
                .setShape(BodyShape.CIRCLE)
                .build();
    }
}