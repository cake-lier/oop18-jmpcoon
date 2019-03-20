package model.physics;

import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

/**
 * The facade for the dyn4j {@link org.dyn4j.dynamics.World} and all the collision mechanics provided by the library.
 */
public interface PhysicalWorld {

    /**
     * Checks if two {@link PhysicalBody}s are currently colliding.
     * @param first The first {@link PhysicalBody} to check against.
     * @param second The second {@link PhysicalBody} to check against.
     * @return True if the two are colliding, false otherwise.
     */
    boolean arePhysicalBodiesInContact(PhysicalBody first, PhysicalBody second);

    /**
     * Remove the {@link PhysicalBody} from this {@link PhysicalWorld}.
     * @param body The {@link PhysicalBody} to remove.
     */
    void removePhysicalBody(PhysicalBody body);

    /**
     * Gets all bodies currently colliding with this {@link PhysicalBody} associated with the points in world coordinates in
     * which they are colliding with this body.
     * @param body The {@link PhysicalBody} from which to get all {@link PhysicalBody}s colliding with it.
     * @return A {@link Set} made with {@link Pair}s of a colliding {@link PhysicalBody} and the point in which is colliding with
     * the passed {@link PhysicalBody}.
     */
    Set<Pair<PhysicalBody, Pair<Double, Double>>> collidingPhysicalBodies(PhysicalBody body);
}
