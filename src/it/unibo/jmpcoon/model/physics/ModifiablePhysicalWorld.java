package it.unibo.jmpcoon.model.physics;

import org.dyn4j.dynamics.World;

import it.unibo.jmpcoon.model.entities.EntityType;
import it.unibo.jmpcoon.model.entities.PowerUpType;
import it.unibo.jmpcoon.model.serializable.SerializableBody;

/**
 * An interface for letting a {@link PhysicalWorld} to be modified by the components responsible of creating new 
 * {@link PhysicalBody}s, such as a {@link PhysicalFactory}, by adding this newly created instances to this {@link PhysicalWorld}
 * and manage their physics along with the others already created.
 */
public interface ModifiablePhysicalWorld {
    /**
     * Registers an association between a {@link PhysicalBody} and the {@link SerializableBody} contained within. It's done for 
     * safety and style reasons, a {@link PhysicalBody} should not return the contained {@link SerializableBody} and if it did,
     * the only object which should be able to see it is a {@link ModifiablePhysicalWorld}. The {@link EntityType} of the
     * {@link it.unibo.jmpcoon.model.entities.Entity} which contains the passed {@link PhysicalBody} is also registered for
     * future cross-checks.
     * @param container the {@link PhysicalBody} which contains the {@link SerializableBody}
     * @param contained the {@link SerializableBody} which is contained in the {@link PhysicalBody}
     * @param type the {@link EntityType} of the {@link it.unibo.jmpcoon.model.entities.Entity} containing the {@link PhysicalBody}
     */
    void addContainerAssociation(PhysicalBody container, SerializableBody contained, EntityType type);

    /**
     * Registers an association between the {@link PlayerPhysicalBody} of a {@link it.unibo.jmpcoon.model.entities.Player} and the
     * {@link it.unibo.jmpcoon.model.entities.Player}'s {@link SerializableBody}. This method is needed for the same reasons
     * as the previous ones, but exclusively for the {@link it.unibo.jmpcoon.model.entities.Player} because it possesses a different
     * {@link PhysicalBody} with more peculiar methods, specific for it.
     * @param container the {@link PlayerPhysicalBody} of a {@link it.unibo.jmpcoon.model.entities.Player}
     * @param contained the {@link SerializableBody} contained inside the {@link PlayerPhysicalBody} passed
     */
    void addPlayerAssociation(PlayerPhysicalBody container, SerializableBody contained);

    /**
     * Registers an association between the {@link PowerUpType} of a {@link it.unibo.jmpcoon.model.entities.PowerUp} and the power-up's
     * {@link SerializableBody}. As for the previous method, it's done for safety and style reasons, a {@link SerializableBody}
     * should not know anything about its {@link PhysicalBody} and this last should not know anything about the associated
     * {@link it.unibo.jmpcoon.model.entities.Entity}. Because the physics of a {@link it.unibo.jmpcoon.model.entities.PowerUp}
     * needs to know to which {@link PowerUpType} is associated a specific {@link SerializableBody}, this method provide a way
     * to register this association in advance, so as to recall it later on.
     * @param body the {@link SerializableBody} of a {@link it.unibo.jmpcoon.model.entities.PowerUp}
     * @param powerUpType the {@link PowerUpType} of the {@link it.unibo.jmpcoon.model.entities.PowerUp} which
     * {@link SerializableBody} is the one passed
     */
    void addPowerUpTypeAssociation(SerializableBody body, PowerUpType powerUpType);

    /**
     * Returns the {@link World} associated with this {@link ModifiablePhysicalWorld}.
     * @return The {@link World} this instance is associated with.
     */
    World getWorld();
}
