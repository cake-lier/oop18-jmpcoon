package model.physics;

import model.entities.State;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.Pair;

/**
 * an interface representing the physical body of an {@link model.entities.Entity}.
 */
public interface PhysicalBody extends Serializable {

    /**
     * @return the center of the {@link PhysicalBody}, as a {@link Pair} where the first element is the x coordinate and the second element is the y one
     */
    Pair<Double, Double> getPosition();

    /**
     * @return the angle of this {@link PhysicalBody}
     */
    double getAngle();

    /**
     * @return the {@link State} this {@link PhysicalBody} is in
     */
    State getState();

    /**
     * @return whether this {@link PhysicalBody} exists or not
     */
    boolean exist();

    /**
     * @return the dimensions (width and height) of this {@link PhysicalBody}
     */
    Pair<Double, Double> getDimensions();

    /**
     * @return the velocity of this {@link PhysicalBody}, divided in its x and y components.
     */
    Pair<Double, Double> getVelocity();

}
