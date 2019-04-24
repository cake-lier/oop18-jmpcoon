package it.unibo.jmpcoon.model.physics;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.tuple.Pair;

/**
 * An interface for providing physics management of {@link PhysicalBody}s to the {@link it.unibo.jmpcoon.model.world.World} and
 * management of the physical simulation in general.
 */
public interface UpdatablePhysicalWorld extends Serializable {
    /**
     * Checks if two {@link PhysicalBody}s are currently colliding.
     * @param first the first {@link PhysicalBody} to check against
     * @param second the second {@link PhysicalBody} to check against
     * @return true if the two are colliding, false otherwise
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
     * @return a {@link Collection} made with {@link Pair}s of a colliding {@link PhysicalBody} and the point in which is
     * colliding with the passed {@link PhysicalBody}
     */
    Collection<Pair<PhysicalBody, Pair<Double, Double>>> getCollidingBodies(PhysicalBody body);

    /**
     * Updates the current state of the {@link PhysicalWorld} by advancing to the next simulation step.
     */
    void update();
}
