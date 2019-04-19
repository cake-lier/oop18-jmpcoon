package model.physics;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

/**
 * An interface for the class providing physics management of the {@link PhysicalBody} to the {@link World}.
 */
public interface UpdatablePhysicalWorld extends Serializable {

    /**
     * Checks if two {@link PhysicalBody}s are currently colliding.
     * @param first the first {@link PhysicalBody} to check against
     * @param second the second {@link PhysicalBody} to check against
     * @return true if the two are colliding, false otherwise.
     */
    boolean areBodiesInContact(PhysicalBody first, PhysicalBody second);

    /**
     * Removes the {@link PhysicalBody} from this {@link PhysicalWorld}.
     * @param body the {@link PhysicalBody} to remove
     */
    void removeBody(PhysicalBody body);

    /**
     * Gets all bodies currently colliding with this {@link PhysicalBody} associated with the points in world coordinates in
     * which they are colliding with this body.
     * @param body the {@link PhysicalBody} from which to get all {@link PhysicalBody}s colliding with it
     * @return a {@link Set} made with {@link Pair}s of a colliding {@link PhysicalBody} and the point in which is colliding with
     * the passed {@link PhysicalBody}
     */
    Set<Pair<PhysicalBody, Pair<Double, Double>>> getCollidingBodies(PhysicalBody body);

    /**
     * Updates the current state of the {@link PhysicalWorld} by advancing to the next simulation step.
     */
    void update();
}
