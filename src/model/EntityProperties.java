package model;

import utils.Pair;

/**
 * Contains the properties of an {@link Entity} which has to be created, such as the
 * {@link EntityType}, the {@link EntityShape}, the current position in the {@link World}
 * and its angle with the x axis.
 */
public interface EntityProperties {
    /**
     * Getter for the {@link EntityType} property.
     * @return The {@link EntityType} property value.
     */
    EntityType getEntityType();
    /**
     * Getter for the {@link EntityShape} property.
     * @return The {@link EntityShape} property value.
     */
    EntityShape getEntityShape();
    /**
     * Getter for the position of this {@link Entity} in {@link World} coordinates.
     * @return A {@link Pair} containing the values of x and y coordinates.
     */
    Pair<Double, Double> getPosition();
    /**
     * Getter for the width of this {@link Entity}.
     * @return the width
     */
    double getWidth();
    /**
     * Getter for the height of this {@link Entity}.
     * @return the height
     */
    double getHeight();
    /**
     * Getter for the angle property.
     * @return The angle value in radians.
     */
    double getAngle();
}
