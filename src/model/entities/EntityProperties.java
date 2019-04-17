package model.entities;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.Pair;

import model.physics.BodyShape;

import com.google.common.base.Optional;

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

    /**
     * Getter for the {@link PowerUpType}, if the {@link EntityProperties} refers to a {@link PowerUp}.
     * @return an {@link Optional} containing the {@link PowerUpType} if this {@link EntityProperties} refers to a 
     * {@link PowerUp}, an empty {@link Optional} otherwise.
     */
    Optional<PowerUpType> getPowerUpType();

    /**
     * Getter for the range a {@link WalkingEnemy} should pace across, if the {@link EntityProperties} refers to a 
     * {@link WalkingEnemy}. 
     * @return an {@link Optional} containing the range a {@link WalkingEnemy} should pace across, if the 
     * {@link EntityProperties} refers to a {@link WalkingEnemy}, an empty {@link Optional} otherwise.
     */
    Optional<Double> getWalkingRange();
}
