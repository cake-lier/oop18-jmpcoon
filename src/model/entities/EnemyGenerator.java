package model.entities;

import model.physics.StaticPhysicalBody;

/**
 * an enemy generator inside the {@link World} of the game.
 */
public class EnemyGenerator extends StaticEntity {

    private static final long serialVersionUID = -3160192139428572083L;

    /**
     * builds a new {@link EnemyGenerator}.
     * 
     * @param body
     *            the {@link PhysicalBody} of this {@link EnemyGenerator}
     */
    public EnemyGenerator(final StaticPhysicalBody body) {
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
