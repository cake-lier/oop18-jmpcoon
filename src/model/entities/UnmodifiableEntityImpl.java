package model.entities;

import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;
import com.google.common.hash.Hashing;

import model.physics.BodyShape;

/**
 * Class implementation of {@link UnmodifiableEntity}.
 */
public final class UnmodifiableEntityImpl implements UnmodifiableEntity {
    private final Entity innerEntity;
    private final boolean isDynamic;
    private final Optional<PowerUpType> powerUpType;

    /*
     * This constructor accepts an entity to wrap so as to delegate to it the calls to methods that should be exposed,
     * if is a dynamic entity and, if it's a power up, an optional with its type, otherwise an empty optional.
     */
    private UnmodifiableEntityImpl(final Entity innerEntity, final boolean isDynamic, final Optional<PowerUpType> powerUpType) {
        this.innerEntity = innerEntity;
        this.isDynamic = isDynamic;
        this.powerUpType = powerUpType;
    }

    /**
     * Factory method for creating an {@link UnmodifiableEntity} from a {@link DynamicEntity}.
     * @param entity the {@link DynamicEntity} to wrap
     * @return an {@link UnmodifiableEntity} that wraps the {@link DynamicEntity} passed
     */
    public static UnmodifiableEntity ofDynamicEntity(final DynamicEntity entity) {
        return new UnmodifiableEntityImpl(entity, true, Optional.absent());
    }

    /**
     * Factory method for creating an {@link UnmodifiableEntity} from a {@link StaticEntity}.
     * @param entity the {@link StaticEntity} to wrap
     * @return an {@link UnmodifiableEntity} that wraps the {@link StaticEntity} passed
     */
    public static UnmodifiableEntity ofStaticEntity(final StaticEntity entity) {
        return new UnmodifiableEntityImpl(entity, false, Optional.absent());
    }

    /**
     * Factory method for creating an {@link UnmodifiableEntity} from a {@link PowerUp}.
     * @param powerUp the {@link PowerUp} to wrap
     * @return an {@link UnmodifiableEntity} that wraps the {@link PowerUp} passed
     */
    public static UnmodifiableEntity ofPowerUp(final PowerUp powerUp) {
        return new UnmodifiableEntityImpl(powerUp, false, Optional.of(powerUp.getPowerUpType()));
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
        return this.isDynamic;
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
