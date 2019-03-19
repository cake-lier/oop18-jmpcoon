package model.entities;

import model.State;
import model.World;
import model.physics.PhysicalBody;
import utils.Pair;

/**
 * an interface representing an Entity inside a {@link World}.
 */
public interface Entity {

    // TODO: better format the comment (line too long)
    /**
     * @return the position of the entity, as a {@link Pair} where the first element is the x coordinate and the second element is the y one
     */
    Pair<Double, Double> getPosition();

    /**
     * @return the shape of this {@link Entity}
     */
    EntityShape getShape();

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
     * 
     */
    void remove();

    /**
     * 
     * @return a.
     */
    Pair<Double, Double> getDimensions();

    /**
     * 
     * @return a.
     */
    PhysicalBody getInternalPhysicalBody();
}
