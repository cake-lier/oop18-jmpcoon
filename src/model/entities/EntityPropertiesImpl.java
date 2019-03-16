package model.entities;

import utils.Pair;
import utils.PairImpl;

/**
 * Class implementation of {@link EntityProperties}.
 */
public final class EntityPropertiesImpl implements EntityProperties {
    private final EntityType type;
    private final EntityShape shape;
    private final Pair<Double, Double> position;
    private final double angle;
    /**
     * Collects the properties of the associated {@link Entity}.
     * @param type The {@link EntityType} of the associated entity.
     * @param shape The {@link EntityShape} of the associated entity.
     * @param xCoord The x coordinate of the associated entity.
     * @param yCoord The y coordinate of the associated entity.
     * @param angle The angle of the associated entity.
     */
    public EntityPropertiesImpl(final EntityType type, final EntityShape shape,
                                final double xCoord, final double yCoord,
                                final double angle) {
        this.type = type;
        this.shape = shape;
        this.position = new PairImpl<>(xCoord, yCoord);
        this.angle = angle;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getEntityType() {
        return this.type;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public EntityShape getEntityShape() {
        return this.shape;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getPosition() {
        return this.position;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public double getAngle() {
        return this.angle;
    }
}
