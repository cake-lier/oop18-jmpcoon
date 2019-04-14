package model.world;

import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.hash.Hashing;

import model.entities.Entity;
import model.entities.EntityShape;
import model.entities.EntityType;
import model.entities.EntityState;

/**
 * A wrapper class for an {@link Entity} instance, used to expose only those methods which are necessary from the outside (for
 * example, to the view of this game) to get informations about this {@link Entity}. All useless methods or methods that could
 * change the {@link Entity} itself are then concealed.
 */
public final class UnmodifiableEntity {
    private static final String NO_TYPE_MSG = "The Entity passed has no known type, see EntityType for reference";

    private final Entity innerEntity;
    private final boolean dynamic;

    /**
     * This constructor accepts an {@link Entity} to wrap so as to delegate to it the calls to methods that should be exposed.
     * @param innerEntity The {@link Entity} to wrap.
     */
    public UnmodifiableEntity(final Entity innerEntity) {
        this.innerEntity = innerEntity;
        final EntityType type = innerEntity.getType();
        if (type == EntityType.LADDER || type == EntityType.PLATFORM || type == EntityType.POWERUP
            || type == EntityType.ENEMY_GENERATOR) {
            this.dynamic = false;
        } else if (type == EntityType.PLAYER || type == EntityType.ROLLING_ENEMY || type == EntityType.WALKING_ENEMY) {
            this.dynamic = true;
        } else {
            throw new IllegalArgumentException(NO_TYPE_MSG);
        }
    }

    /**
     * Gets current position of this {@link UnmodifiableEntity}, as a {@link Pair} of coordinates which represents the center of it.
     * @return The {@link Pair} of coordinates of the center of this {@link UnmodifiableEntity}.
     */
    public Pair<Double, Double> getPosition() {
        return this.innerEntity.getPosition();
    }

    /**
     * Gets the shape of this {@link UnmodifiableEntity} as a value of {@link EntityShape}.
     * @return The {@link EntityShape} of this {@link UnmodifiableEntity}.
     */
    public EntityShape getShape() {
        return this.innerEntity.getShape();
    }

    /**
     * Gets the angle of rotation around the center of this {@link UnmodifiableEntity} from its position aligned with the coordinate
     * system of the world calculated in radians counterclockwise in the interval [-pi, pi].
     * @return The angle of rotation of this {@link UnmodifiableEntity}.
     */
    public double getAngle() {
        return this.innerEntity.getAngle();
    }

    /**
     * Gets the type of this {@link UnmodifiableEntity} as a value of {@link EntityType}.
     * @return The {@link EntityType} of this {@link UnmodifiableEntity}.
     */
    public EntityType getType() {
        return this.innerEntity.getType();
    }

    /**
     * Gets the state of this {@link UnmodifiableEntity} as a value of {@link EntityState}. 
     * @return The {@link EntityState} this {@link UnmodifiableEntity} is in.
     */
    public EntityState getState() {
        return this.innerEntity.getState();
    }

    /**
     * Gets a {@link Pair} of values which represents respectively the width and height of this {@link UnmodifiableEntity}.
     * @return The width and height of this {@link UnmodifiableEntity} as a {@link Pair}.
     */
    public Pair<Double, Double> getDimensions() {
        return this.innerEntity.getDimensions();
    }

    /**
     * Gets if the wrapped {@link Entity} is part of the subtype {@link DynamicEntity} or not.
     * @return True if the wrapped {@link Entity} is a {@link DynamicEntity}.
     */
    public boolean isDynamic() {
        return this.dynamic;
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
                               && Objects.equals(this.innerEntity, UnmodifiableEntity.class.cast(obj).innerEntity));
    }
}
