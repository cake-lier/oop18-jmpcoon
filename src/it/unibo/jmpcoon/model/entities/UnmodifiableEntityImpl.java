package it.unibo.jmpcoon.model.entities;

import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;
import com.google.common.hash.Hashing;

import it.unibo.jmpcoon.model.physics.BodyShape;

/**
 * Class implementation of {@link UnmodifiableEntity}.
 */
public class UnmodifiableEntityImpl implements UnmodifiableEntity {
    private static final String WRONG_CONSTRUCTOR = "Wrong constructor used, use the other one instead";

    private final Entity innerEntity;
    private final boolean dynamic;
    private final Optional<PowerUpType> powerUpType;

    /**
     * Constructor for creating an {@link UnmodifiableEntity} from a {@link DynamicEntity}. 
     * @param entity the {@link DynamicEntity} to wrap
     */
    public UnmodifiableEntityImpl(final DynamicEntity entity) {
        this.innerEntity = Objects.requireNonNull(entity);
        this.dynamic = true;
        this.powerUpType = Optional.absent();
    }

    /**
     * Constructor for creating an {@link UnmodifiableEntity} from a {@link StaticEntity}.
     * @param entity the {@link StaticEntity} to wrap
     * @throws IllegalArgumentException if the {@link EntityType} of the {@link StaticEntity} passed is
     * {@link EntityType#POWERUP} because the wrong constructor has been used
     */
    public UnmodifiableEntityImpl(final StaticEntity entity) throws IllegalArgumentException {
        if (Objects.requireNonNull(entity).getType() == EntityType.POWERUP) {
            throw new IllegalArgumentException(WRONG_CONSTRUCTOR);
        }
        this.innerEntity = entity;
        this.dynamic = false;
        this.powerUpType = Optional.absent();
    }

    /**
     * Constructor for creating an {@link UnmodifiableEntity} from a {@link PowerUp}.
     * @param powerUp the {@link PowerUp} to wrap
     */
    public UnmodifiableEntityImpl(final PowerUp powerUp) {
        this.innerEntity = Objects.requireNonNull(powerUp);
        this.dynamic = false;
        this.powerUpType = Optional.of(powerUp.getPowerUpType());
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
    public BodyShape getShape() {
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
        return this.dynamic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getVelocity() {
        return this.innerEntity.getVelocity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PowerUpType> getPowerUpType() {
        return this.powerUpType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Hashing.murmur3_128().newHasher().putInt(this.innerEntity.hashCode()).hash().asInt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        return this == obj || (obj != null && this.getClass() == obj.getClass() 
                               && Objects.equals(this.innerEntity, UnmodifiableEntityImpl.class.cast(obj).innerEntity));
    }
}
