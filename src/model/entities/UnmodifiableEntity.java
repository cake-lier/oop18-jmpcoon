package model.entities;

import org.apache.commons.lang3.tuple.Pair;

import model.physics.BodyShape;

/**
 * An interface for realizing a wrapper class for an {@link Entity} instance, used to expose only those methods which are
 * necessary from the outside (for example, to the view of this game) to get informations about this {@link Entity}. All useless
 * methods or methods that could change the {@link Entity} itself are then concealed.
 */
public interface UnmodifiableEntity {
    /**
     * Gets current position of this {@link UnmodifiableEntity}, as a {@link Pair} of coordinates which represents the center of it.
     * @return The {@link Pair} of coordinates of the center of this {@link UnmodifiableEntity}.
     */
    Pair<Double, Double> getPosition();

    /**
     * Gets the shape of this {@link UnmodifiableEntity} as a value of {@link BodyShape}.
     * @return The {@link BodyShape} of this {@link UnmodifiableEntity}.
     */
    BodyShape getShape();

    /**
     * Gets the angle of rotation around the center of this {@link UnmodifiableEntity} from its position aligned with the coordinate
     * system of the world calculated in radians counterclockwise in the interval [-pi, pi].
     * @return The angle of rotation of this {@link UnmodifiableEntity}.
     */
    double getAngle();

    /**
     * Gets the type of this {@link UnmodifiableEntity} as a value of {@link EntityType}.
     * @return The {@link EntityType} of this {@link UnmodifiableEntity}.
     */
    EntityType getType();

    /**
     * Gets the state of this {@link UnmodifiableEntity} as a value of {@link EntityState}. 
     * @return The {@link EntityState} this {@link UnmodifiableEntity} is in.
     */
    EntityState getState();

    /**
     * Gets a {@link Pair} of values which represents respectively the width and height of this {@link UnmodifiableEntity}.
     * @return The width and height of this {@link UnmodifiableEntity} as a {@link Pair}.
     */
    Pair<Double, Double> getDimensions();

    /**
     * Gets if the wrapped {@link Entity} is part of the subtype {@link DynamicEntity} or not.
     * @return True if the wrapped {@link Entity} is a {@link DynamicEntity}.
     */
    boolean isDynamic();
}
