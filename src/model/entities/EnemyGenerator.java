package model.entities;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import model.physics.PhysicalFactory;
import model.physics.StaticPhysicalBody;

/**
 * An enemy generator inside the {@link model.world.World} of the game.
 */
public final class EnemyGenerator extends StaticEntity {

    private static final long serialVersionUID = -3160192139428572083L;
    private static final ImmutablePair<Double, Double> ROLLING_ENEMY_DIMENSIONS = new ImmutablePair<Double, Double>(0.15, 0.15);
    private static final int DELTA = 380;

    private List<RollingEnemy> enemies = new LinkedList<>();
    private int count = 0;

    /**
     * Creates a new {@link EnemyGenerator} with the given {@link StaticPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link EntityBuilder} when creating a new instance of it and no one else.
     * @param body The {@link StaticPhysicalBody} that should be contained in this {@link EnemyGenerator}.
     */
    EnemyGenerator(final StaticPhysicalBody body) {
        super(body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return EntityType.ENEMY_GENERATOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityShape getShape() {
        return EntityShape.RECTANGLE;
    }

    /**
     * @param physicsFactory the {@link Physics
     * @return ddh
     */
    public Iterable<RollingEnemy> onTimeAdvanced(final PhysicalFactory physicsFactory) {
        this.enemies.clear();
        if (checkTime()) {
            this.enemies.add(createRollingEnemy(physicsFactory));
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
                .setShape(EntityShape.CIRCLE)
                .build();
    }
}
