package model.entities;

import java.util.Objects;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.hash.Hashing;

/**
 * Class implementation of {@link EntityProperties}.
 */
public final class EntityPropertiesImpl implements EntityProperties {
    private static final long serialVersionUID = 6911353545986657204L;

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
        this.position = new ImmutablePair<>(xCoord, yCoord);
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
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Hashing.murmur3_128()
                      .newHasher()
                      .putDouble(this.angle)
                      .putInt(this.position.hashCode())
                      .putInt(this.shape.hashCode())
                      .putInt(this.type.hashCode())
                      .hash()
                      .asInt();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final EntityPropertiesImpl other = (EntityPropertiesImpl) obj;
        return Double.doubleToLongBits(this.angle) == Double.doubleToLongBits(other.angle)
                && Objects.equals(this.position, other.position)
                && this.shape == other.shape && this.type == other.type;
    }
    /** 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "EntityPropertiesImpl [type=" + this.type + ", shape=" + this.shape
               + ", position=" + this.position + ", angle=" + this.angle + "]";
    }
}
