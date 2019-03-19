package model.physics;

import model.entities.EntityShape;
import org.apache.commons.lang3.tuple.Pair;

/**
 * an interface representing the physical body of an {@link Entity}.
 */
public interface PhysicalBody {

    /**
     * @return the position of the {@link PhysicalBody}, as a {@link Pair} where the first element is the x coordinate and the second element is the y one
     */
    Pair<Double, Double> getPosition();

    /**
     * @return the shape of this {@link PhysicalBody}
     */
    EntityShape getShape();

    /**
     * @return whether this {@link PhysicalBody} exists or not
     */
    boolean exist();

    /**
     * 
     * @return a.
     */
    Pair<Double, Double> getDimensions();
}
