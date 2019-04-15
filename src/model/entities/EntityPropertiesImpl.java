package model.entities;

import java.util.Objects;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;
import com.google.common.hash.Hashing;

import model.physics.BodyShape;

/**
 * Class implementation of {@link EntityProperties}.
 */
public final class EntityPropertiesImpl implements EntityProperties {
    private static final long serialVersionUID = -6863412268735663647L;

    private final EntityType type;
    private final BodyShape shape;
    private final Pair<Double, Double> position;
    private final Pair<Double, Double> size;
    private final double angle;
    private final Optional<PowerUpType> powerUpType;

    /**
     * Collects the properties of the associated {@link Entity}.
     * @param type The {@link EntityType} of the associated {@link Entity}.
     * @param shape The {@link BodyShape} of the associated {@link Entity}.
     * @param xCoord The x coordinate of the associated {@link Entity}.
     * @param yCoord The y coordinate of the associated {@link Entity}.
     * @param width The width of the associated {@link Entity}.
     * @param height The height of the associated {@link Entity}.
     * @param angle The angle of the associated entity.
     * @param powerUpType The type of the {@link PowerUp}, if these are the properties of a {@link PowerUp}.
     */
    public EntityPropertiesImpl(final EntityType type, final BodyShape shape, final double xCoord, 
                                final double yCoord, final double width, final double height, 
                                final double angle, final Optional<PowerUpType> powerUpType) {
        this.type = type;
        this.shape = shape;
        this.position = new ImmutablePair<>(xCoord, yCoord);
        this.size = new ImmutablePair<Double, Double>(width, height);
        this.angle = angle;
        this.powerUpType = powerUpType;
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
    public BodyShape getEntityShape() {
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
    public Pair<Double, Double> getDimensions() {
        return this.size;
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
                      .putInt(this.size.hashCode())
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
                && Objects.equals(this.size, other.size)
                && this.shape == other.shape && this.type == other.type;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "EntityPropertiesImpl [type=" + this.type + ", shape=" + this.shape
               + ", position=" + this.position + ", dimensions=" + this.size + ", angle=" + this.angle + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PowerUpType> getPowerUpType() {
        return this.powerUpType;
    }
}
