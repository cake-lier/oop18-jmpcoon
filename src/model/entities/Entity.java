package model.entities;

import model.physics.BodyShape;
import model.physics.PhysicalBody;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Represents an entity, a physical object with an extension, a position, mass and a particular behavior which
 * depends on its {@link EntityType} and {@link EntityState}, inside the {@link model.world.World}.
 */
public interface Entity extends Serializable {
    /**
     * Returns the position of this {@link model.entities.Entity}.
     * @return the position of the entity, as a {@link Pair} where the first element is the x coordinate
     * and the second element is the y one of center of the {@link model.entities.Entity}
     */
    Pair<Double, Double> getPosition();

    /**
     * Returns the {@link BodyShape} of this {@link model.entities.Entity}.
     * @return the shape of this {@link model.entities.Entity}
     */
    BodyShape getShape();

    /**
     * Returns the angle of this {@link model.entities.Entity}.
     * @return the angle of rotation around the center of the {@link model.entities.Entity} from its position aligned
     * with the coordinate system of the world calculated in radians counterclockwise
     */
    double getAngle();

    /**
     * Returns the {@link EntityType} of this {@link model.entities.Entity}.
     * @return the type of this {@link model.entities.Entity}
     */
    EntityType getType();

    /**
     * Returns the {@link model.entities.EntityState} of this {@link model.entities.Entity}.
     * @return the state this {@link model.entities.Entity} is in
     */
    EntityState getState();

    /**
     * Returns if this {@link model.entities.Entity} is alive or not.
     * @return whether the {@link model.entities.Entity} is alive or not
     */
    boolean isAlive();

    /**
     * Returns the dimensions of this {@link model.entities.Entity}.
     * @return the dimensions (width and height) of this {@link model.entities.Entity}
     */
    Pair<Double, Double> getDimensions();

    /**
     * Returns the velocity of this {@link model.entities.Entity}.
     * @return the velocity of this {@link model.entities.Entity}
     */
    Pair<Double, Double> getVelocity();

    /**
     * Returns the {@link PhysicalBody} of this {@link model.entities.Entity}.
     * @return the internal {@link PhysicalBody} inside this {@link model.entities.Entity}
     */
    PhysicalBody getPhysicalBody();
}
