package model.entities;

import model.physics.PhysicalBody;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Represents an entity, a physical object with an extension, a position, mass and a particular behavior which
 * depends on its {@link EntityType} and {@link State}, inside the {@link World}.
 */
public interface Entity extends Serializable {

    /**
     * @return the position of the entity, as a {@link Pair} where the first element is the x coordinate
     * and the second element is the y one of center of the {@link Entity}
     */
    Pair<Double, Double> getPosition();

    /**
     * @return the shape of this {@link Entity}
     */
    EntityShape getShape();

    /**
     * @return the angle of rotation around the center of the {@link Entity} from its position aligned
     * with the coordinate system of the world calculated in radians counterclockwise
     */
    double getAngle();

    /**
     * @return the type of this {@link Entity}
     */
    EntityType getType();

    /**
     * @return the state this {@link Entity} is in
     */
    State getState();

    /**
     * @return whether the {@link Entity} is alive or not
     */
    boolean isAlive();

    /**
     * @return the dimensions (width and height) of this {@link Entity}
     */
    Pair<Double, Double> getDimensions();

    /**
     * 
     * @return the internal {@link PhysicalBody} inside this {@link Entity}
     */
    PhysicalBody getPhysicalBody();
}
