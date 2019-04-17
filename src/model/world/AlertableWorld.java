package model.world;

/**
 * An interface containing methods for {@link World} to implements so as to receive informations from
 * {@link model.physics.PhysicalWorld} about collisions that happened and act accordingly. This interface was made so as to allow
 * the {@link model.physics.PhysicalWorld} to communicate without the ability of calling other methods such as for initializing
 * or getting informations about the {@link World}, which is a thing it shouldn't do. In fact, such last actions should be made
 * only by the {@link controller.game.GameController}, which in turn can't notify {@link World} of happened collisions, hence
 * again the separation of these last methods in another interface.
 */
public interface AlertableWorld {

    /**
     * Notifies the {@link AlertableWorld} about a collision that happened, which type is represented by a value of
     * {@link CollisionType}.
     * @param collisionType The {@link CollisionType} of a collision that has happened.
     */
    void notifyCollision(CollisionType collisionType);
}
