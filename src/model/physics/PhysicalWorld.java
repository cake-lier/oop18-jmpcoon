package model.physics;

/**
 * The facade for the dyn4j {@link org.dyn4j.dynamics.World} and all the collision
 * mechanics provided by the library.
 */
public interface PhysicalWorld {
    /**
     * Checks if two {@link PhysicalBody}s are currently colliding.
     * @param first The first {@link PhysicalBody} to check against.
     * @param second The second {@link PhysicalBody} to check against.
     * @return True if the two are colliding, false otherwise.
     */
    boolean arePhysicalBodiesInContact(PhysicalBody first, PhysicalBody second);
}
