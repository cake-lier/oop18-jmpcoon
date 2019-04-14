package model.physics;

import model.entities.PowerUpManager;

/**
 * A convenience interface for manipulating objects with properties both of a {@link PhysicalWorld} and a
 * {@link ModifiablePhysicalWorld}.
 */
public interface WholePhysicalWorld extends PhysicalWorld, ModifiablePhysicalWorld {
    /**
     * Gives {@link WholePhysicalWorld} access to the {link PowerUpManager} created by the {@link World}.
     * @param powerUpManager The {@link PowerUpManager} to be set.
     */
    void setManager(PowerUpManager powerUpManager);
}
