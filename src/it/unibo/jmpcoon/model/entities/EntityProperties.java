package it.unibo.jmpcoon.model.entities;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;

import it.unibo.jmpcoon.model.physics.BodyShape;

/**
 * Contains the properties of an {@link it.unibo.jmpcoon.model.entities.Entity} which has to be created, such as the {@link EntityType}, 
 * the {@link BodyShape}, the current position in the {@link it.unibo.jmpcoon.model.world.World} and its angle with the x axis.
 */
public interface EntityProperties extends Serializable {
    /**
     * Returns the {@link EntityType} property.
     * @return the {@link EntityType} property value
     */
    EntityType getEntityType();

    /**
     * Returns the {@link BodyShape} property.
     * @return the {@link BodyShape} property value
     */
    BodyShape getEntityShape();

    /**
     * Returns the position of the upper left corner of this {@link Entity} in world coordinates.
     * @return a {@link Pair} containing the values of x and y coordinates
     */
    Pair<Double, Double> getPosition();

    /**
     * Returns the width and the height of this {@link Entity}.
     * @return a {@link Pair} containing the width and the height
     */
    Pair<Double, Double> getDimensions();

    /**
     * Returns the angle property.
     * @return the angle value in radians
     */
    double getAngle();

    /**
     * Returns the {@link PowerUpType}, if the {@link EntityProperties} refers to a {@link PowerUp}.
     * @return an {@link Optional} containing the {@link PowerUpType} if this {@link EntityProperties} refers to a 
     * {@link PowerUp}, an empty {@link Optional} otherwise
     */
    Optional<PowerUpType> getPowerUpType();

    /**
     * Returns the range a {@link WalkingEnemy} should pace across, if the {@link EntityProperties} refers to a 
     * {@link WalkingEnemy}. 
     * @return an {@link Optional} containing the range a {@link WalkingEnemy} should pace across, if the 
     * {@link EntityProperties} refers to a {@link WalkingEnemy}, an empty {@link Optional} otherwise
     */
    Optional<Double> getWalkingRange();
}
