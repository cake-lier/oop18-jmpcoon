package model.entities;

import model.physics.StaticPhysicalBody;

/**
 * An enemy generator inside the {@link model.world.World} of the game.
 */
public final class EnemyGenerator extends StaticEntity {

    private static final long serialVersionUID = -3160192139428572083L;

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

}
