package model.entities;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.Pair;

import model.physics.BodyShape;

/**
 * Contains the properties of an {@link Entity} which has to be created, such as the {@link EntityType}, the {@link BodyShape},
 * the current position in the {@link model.world.World} and its angle with the x axis.
 */
public interface EntityProperties extends Serializable {

    /**
     * Getter for the {@link EntityType} property.
     * @return The {@link EntityType} property value.
     */
    EntityType getEntityType();

    /**
     * Getter for the {@link BodyShape} property.
     * @return The {@link BodyShape} property value.
     */
    BodyShape getEntityShape();

    /**
     * Getter for the position of the upper left corner of this {@link Entity} in world coordinates.
     * @return A {@link Pair} containing the values of x and y coordinates.
     */
    Pair<Double, Double> getPosition();

    /**
     * Getter for the width and the height of this {@link Entity}.
     * @return A {@link Pair} containing the width and the height.
     */
    Pair<Double, Double> getDimensions();

    /**
     * Getter for the angle property.
     * @return The angle value in radians.
     */
    double getAngle();
}
