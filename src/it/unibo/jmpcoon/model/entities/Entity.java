package it.unibo.jmpcoon.model.entities;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.Pair;

import it.unibo.jmpcoon.model.physics.BodyShape;
import it.unibo.jmpcoon.model.physics.PhysicalBody;

/**
 * Represents an entity, a physical object with an extension, a position, mass and a particular behavior which
 * depends on its {@link EntityType} and {@link EntityState}, inside the {@link it.unibo.jmpcoon.model.world.World}.
 */
public interface Entity extends Serializable {
    /**
     * Returns the position of this {@link it.unibo.jmpcoon.model.entities.Entity}.
     * @return the position of the entity, as a {@link Pair} where the first element is the x coordinate
     * and the second element is the y one of center of the {@link it.unibo.jmpcoon.model.entities.Entity}
     */
    Pair<Double, Double> getPosition();

    /**
     * Returns the {@link BodyShape} of this {@link it.unibo.jmpcoon.model.entities.Entity}.
     * @return the shape of this {@link it.unibo.jmpcoon.model.entities.Entity}
     */
    BodyShape getShape();

    /**
     * Returns the angle of this {@link it.unibo.jmpcoon.model.entities.Entity}.
     * @return the angle of rotation around the center of the {@link it.unibo.jmpcoon.model.entities.Entity} from its position aligned
     * with the coordinate system of the world calculated in radians counterclockwise
     */
    double getAngle();

    /**
     * Returns the {@link EntityType} of this {@link it.unibo.jmpcoon.model.entities.Entity}.
     * @return the type of this {@link it.unibo.jmpcoon.model.entities.Entity}
     */
    EntityType getType();

    /**
     * Returns the {@link it.unibo.jmpcoon.model.entities.EntityState} of this {@link it.unibo.jmpcoon.model.entities.Entity}.
     * @return the state this {@link it.unibo.jmpcoon.model.entities.Entity} is in
     */
    EntityState getState();

    /**
     * Returns if this {@link it.unibo.jmpcoon.model.entities.Entity} is alive or not.
     * @return whether the {@link it.unibo.jmpcoon.model.entities.Entity} is alive or not
     */
    boolean isAlive();

    /**
     * Returns the dimensions of this {@link it.unibo.jmpcoon.model.entities.Entity}.
     * @return the dimensions (width and height) of this {@link it.unibo.jmpcoon.model.entities.Entity}
     */
    Pair<Double, Double> getDimensions();

    /**
     * Returns the velocity of this {@link it.unibo.jmpcoon.model.entities.Entity}.
     * @return the velocity of this {@link it.unibo.jmpcoon.model.entities.Entity}
     */
    Pair<Double, Double> getVelocity();

    /**
     * Returns the {@link PhysicalBody} of this {@link it.unibo.jmpcoon.model.entities.Entity}.
     * @return the internal {@link PhysicalBody} inside this {@link it.unibo.jmpcoon.model.entities.Entity}
     */
    PhysicalBody getPhysicalBody();
}
