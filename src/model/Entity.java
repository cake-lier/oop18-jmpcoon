package model;

import utils.Pair;

/**
 * an interface representing an Entity inside a {@link World}.
 */
public interface Entity {

    /**
     * @return the position of the entity, as a {@link Pair} where the first element is the x coordinate
     * and the second element is the y one
     */
    Pair<Double, Double> getPosition();

    /**
     * @return the shape of this {@link Entity}
     */
    EntityShape getShape();

    // TODO: better phrase this comment
    /**
     * @return the angle in radians of this {@link Entity}
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

}
