package model.world;

/**
 * 
 *
 */
public interface NotifiableWorld {

    /**
     * 
     * @param collisionType
     */
    void notifyCollision(CollisionType collisionType);
}
