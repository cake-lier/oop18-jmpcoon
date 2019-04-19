package model.physics;

import model.entities.EntityState;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.Pair;

/**
 * An interface representing the physical body of an {@link model.entities.Entity}.
 */
public interface PhysicalBody extends Serializable {

    /**
     * @return the center of the {@link PhysicalBody}, as a {@link Pair} where the first element is the x coordinate and the 
     * second element is the y one
     */
    Pair<Double, Double> getPosition();

    /**
     * @return the angle of rotation of this {@link PhysicalBody} around its center, from its position aligned
     * with the coordinate system of the world calculated in radians counterclockwise
     */
    double getAngle();

    /**
     * @return the {@link EntityState} this {@link PhysicalBody} is in
     */
    EntityState getState();

    /**
     * @return whether this {@link PhysicalBody} exists or not
     */
    boolean exists();


    /**
     * @return the shape of this {@link PhysicalBody}
     */
    BodyShape getShape();

    /**
     * @return the dimensions (width and height) of this {@link PhysicalBody}
     */
    Pair<Double, Double> getDimensions();

    /**
     * @return the velocity of this {@link PhysicalBody}, divided in its x and y components
     */
    Pair<Double, Double> getVelocity();

}
