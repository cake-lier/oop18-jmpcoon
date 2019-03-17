package model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

/**
 * 
 */
public interface ModifiablePhysicalWorld {
    /**
     * Registers an association between a {@link PhysicalBody} and the {@link Body}
     * contained within. It's done for safety and style reasons, a PhysicalBody
     * should not return the contained {@link Body} and if it did, the only object which
     * should be able to see it is a {@link ModifiablePhysicalWorld}.
     * @param container The {@link PhysicalBody} which contains the {@link Body}.
     * @param contained The {@link Body} which is contained in the {@link PhysicalBody}.
     */
    void addContainerAssociation(PhysicalBody container, Body contained);
    /**
     * Returns the {@link World} associated with this {@link ModifiablePhysicalWorld}.
     * @return The {@link World} this instance is associated with.
     */
    World getWorld();
}
