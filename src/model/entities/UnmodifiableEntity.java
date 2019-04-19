package model.entities;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;

import model.physics.BodyShape;

/**
 * An interface for realizing a wrapper class for an {@link Entity} instance, used to expose only those methods which are
 * necessary from the outside (for example, to the view of this game) to get informations about this {@link Entity}. All useless
 * methods or methods that could change the {@link Entity} itself are then concealed.
 */
public interface UnmodifiableEntity {
    /**
     * Gets current position of this {@link UnmodifiableEntity}, as a {@link Pair} of coordinates which represents the center of
     * it.
     * @return the {@link Pair} of coordinates of the center of this {@link UnmodifiableEntity}
     */
    Pair<Double, Double> getPosition();

    /**
     * Gets the shape of this {@link UnmodifiableEntity} as a value of {@link BodyShape}.
     * @return the {@link BodyShape} of this {@link UnmodifiableEntity}
     */
    BodyShape getShape();

    /**
     * Gets the angle of rotation around the center of this {@link UnmodifiableEntity} from its position aligned with the 
     * coordinate system of the world calculated in radians counterclockwise in the interval [-pi, pi].
     * @return the angle of rotation of this {@link UnmodifiableEntity}
     */
    double getAngle();

    /**
     * Gets the type of this {@link UnmodifiableEntity} as a value of {@link EntityType}.
     * @return the {@link EntityType} of this {@link UnmodifiableEntity}
     */
    EntityType getType();

    /**
     * Gets the state of this {@link UnmodifiableEntity} as a value of {@link EntityState}. 
     * @return the {@link EntityState} this {@link UnmodifiableEntity} is in
     */
    EntityState getState();

    /**
     * Gets a {@link Pair} of values which represents respectively the width and height of this {@link UnmodifiableEntity}.
     * @return the width and height of this {@link UnmodifiableEntity} as a {@link Pair}
     */
    Pair<Double, Double> getDimensions();

    /**
     * Gets if the wrapped {@link Entity} is part of the subtype {@link DynamicEntity} or not.
     * @return true if the wrapped {@link Entity} is a {@link DynamicEntity}, false otherwise
     */
    boolean isDynamic();

    /**
     * Gets a {@link Pair} of values which represents the x and y value of the velocity of this {@link UnmodifiableEntity}.
     * @return the velocity of this {@link UnmodifiableEntity} as a {@link Pair}
     */
    Pair<Double, Double> getVelocity();

    /**
     * Gets the power-up type of the wrapped {@link Entity}, if it wraps a {@link PowerUp}, an absent value otherwise.
     * @return an {@link Optional} containing the the {@link PowerUpType} if this instance wraps a {@link PowerUp}, absent 
     * otherwise
     */
    Optional<PowerUpType> getPowerUpType();
}
