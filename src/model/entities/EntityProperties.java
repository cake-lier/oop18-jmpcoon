package model.entities;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Contains the properties of an {@link Number} which has to be created, such as the {@link EntityType}, the {@link EntityShape},
 * the current position in the {@link World} and its angle with the x axis.
 */
public interface EntityProperties extends Serializable {

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
     * Getter for the position of the upper left corner of this {@link Number} in world coordinates.
     * @return A {@link Pair} containing the values of x and y coordinates.
     */
    Pair<Double, Double> getPosition();

    /**
     * Getter for the width and the height of this {@link Number}.
     * @return A {@link Pair} containing the width and the height.
     */
    Pair<Double, Double> getDimensions();

    /**
     * Getter for the angle property.
     * @return The angle value in radians.
     */
    double getAngle();
}
