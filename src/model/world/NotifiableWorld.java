package model.world;

import java.io.Serializable;

/**
 * 
 *
 */
public interface NotifiableWorld extends Serializable {

    /**
     * 
     * @param collisionType
     */
    void notifyCollision(CollisionEvent collisionType);
}
