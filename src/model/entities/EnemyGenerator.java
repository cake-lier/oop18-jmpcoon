package model.entities;

import model.physics.BodyShape;
import model.physics.PhysicalFactory;
import model.physics.StaticPhysicalBody;
import model.world.ModifiableWorld;

import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * An enemy generator inside the {@link model.world.World} of the game.
 * It creates a new instance of the {@link RollingEnemy} on a regular interval of time.
 */
public final class EnemyGenerator extends StaticEntity {
    private static final long serialVersionUID = -3160192139428572083L;
    private static final ImmutablePair<Double, Double> ROLLING_ENEMY_DIMENSIONS = new ImmutablePair<Double, Double>(0.23, 0.23);
    private static final int DELTA = 280;

    private final PhysicalFactory factory;
    private final ModifiableWorld world;
    private int count;

    /**
     * Creates a new {@link EnemyGenerator} with the given {@link StaticPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link AbstractEntityBuilder} when creating a new instance of it and no one else.
     * @param body the {@link StaticPhysicalBody} that should be contained in this {@link EnemyGenerator}
     * @param factory the {@link PhysicalFactory} that generates the RollingEnemy {@link PhysicalBody}
     * @param world the {@link World} to notify when a {@link RollingEnemy} is created
     */
    EnemyGenerator(final StaticPhysicalBody body, final PhysicalFactory factory, final ModifiableWorld world) {
        super(body);
        this.factory = factory;
        this.count = -1;
        this.world = world;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return EntityType.ENEMY_GENERATOR;
    }

    /**
     * It notifies that a lapse of time have passed. If this {@link EnemyGenerator} has created a new {@link RollingEnemy},
     * it adds it to the world.
     */
    public void onTimeAdvanced() {
        if (this.checkTime()) {
            this.world.addGeneratedRollingEnemy(this.createCompleteRollingEnemy());
        }
    }

    private RollingEnemy createCompleteRollingEnemy() {
        final RollingEnemy enemy = this.createRollingEnemy();
        enemy.applyImpulse();
        return enemy;
    }

    private boolean checkTime() {
        this.count++;
        return this.count % DELTA == 0;
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
