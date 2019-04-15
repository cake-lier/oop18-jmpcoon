package model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

import model.entities.EntityType;
import model.entities.PowerUpType;

/**
 * 
 */
public interface ModifiablePhysicalWorld {
    /**
     * Registers an association between a {@link PhysicalBody} and the {@link Body} contained within. It's done for safety and 
     * style reasons, a {@link PhysicalBody} should not return the contained {@link Body} and if it did, the only object which
     * should be able to see it is a {@link ModifiablePhysicalWorld}. The {@link EntityType} of the {@link model.entities.Entity}
     * which contains the passed {@link PhysicalBody} is also registered for future cross-checks.
     * @param container The {@link PhysicalBody} which contains the {@link Body}.
     * @param contained The {@link Body} which is contained in the {@link PhysicalBody}.
     * @param type The {@link EntityType} of the {@link model.entities.Entity} containing the {@link PhysicalBody}.
     */
    void addContainerAssociation(PhysicalBody container, Body contained, EntityType type);

    /**
     * Returns the {@link World} associated with this {@link ModifiablePhysicalWorld}.
     * @return The {@link World} this instance is associated with.
     */
    World getWorld();
    
    void addPowerUpTypeAssociation(Body body, PowerUpType powerUpType);
}
