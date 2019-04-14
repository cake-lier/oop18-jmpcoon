package model.entities;

import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.hash.Hashing;

/**
 * Class implementation of {@link UnmodifiableEntity}.
 */
public final class UnmodifiableEntityImpl implements UnmodifiableEntity {
    private final Entity innerEntity;

    /**
     * This constructor accepts an {@link Entity} to wrap so as to delegate to it the calls to methods that should be exposed.
     * @param innerEntity The {@link Entity} to wrap.
     */
    public UnmodifiableEntityImpl(final Entity innerEntity) {
        this.innerEntity = innerEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getPosition() {
        return this.innerEntity.getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityShape getShape() {
        return this.innerEntity.getShape();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAngle() {
        return this.innerEntity.getAngle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return this.innerEntity.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityState getState() {
        return this.innerEntity.getState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getDimensions() {
        return this.innerEntity.getDimensions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDynamic() {
        final EntityType type = this.getType();
        return type == EntityType.PLAYER || type == EntityType.ROLLING_ENEMY || type == EntityType.WALKING_ENEMY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Hashing.murmur3_128().newHasher().putInt(this.innerEntity.hashCode()).hash().asInt();
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || (obj != null && this.getClass() == obj.getClass() 
                               && Objects.equals(this.innerEntity, UnmodifiableEntityImpl.class.cast(obj).innerEntity));
    }
}
