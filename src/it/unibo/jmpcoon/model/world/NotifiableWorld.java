package it.unibo.jmpcoon.model.world;

import java.io.Serializable;

/**
 * An interface for {@link World} so it can be notified of events that happened. This will be used to pass an instance
 * of {@link World} to the physics components of this game so as to alert it that a physical event, such as
 * a {@link CollisionEvent}, has happened without using other methods which pertains to other areas of interest and
 * interfere with other operations.
 */
public interface NotifiableWorld extends Serializable {
    /**
     * Notifies this world that a collision has happened by specifying the type of the collision as a value of type
     * {@link CollisionEvent}.
     * @param collisionType the type of the collision that happened
     */
    void notifyCollision(CollisionEvent collisionType);
}
