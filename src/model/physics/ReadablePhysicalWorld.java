package model.physics;

import java.io.Serializable;

import org.dyn4j.dynamics.Body;

import com.google.common.base.Optional;

import model.entities.EntityType;
import model.entities.PowerUpType;

/**
 * An interface used by {@link PhysicalWorld} for having getters for its physical properties, so as to allow its
 * subcomponents to access them and do physical calculations correctly.
 */
public interface ReadablePhysicalWorld extends Serializable {
    /**
     * Gets a {@link PhysicalBody} given the {@link Body} to which is associated.
     * @param body the {@link Body} from which getting its associated {@link PhysicalBody}
     * @return the {@link PhysicalBody} associated with the passed {@link Body}
     */
    PhysicalBody getPhysicalBodyFromBody(Body body);

    /**
     * Gets the {@link EntityType} of the {@link model.entities.Entity} which {@link PhysicalBody} is associated to the
     * given {@link Body}.
     * @param body the {@link Body} from which getting its associated {@link EntityType}
     * @return the {@link EntityType} associated with the passed {@link Body}
     */
    EntityType getEntityTypeFromBody(Body body);

    /**
     * Gets the {@link PhysicalBody} of a {@link model.entities.Ladder} in case the player is currently colliding with one
     * in this iteration step.
     * @return an {@link Optional} with the {@link PhysicalBody} of a {@link model.entities.Ladder} if the player is
     * colliding with it, an {@link Optional#absent()} otherwise
     */
    Optional<PhysicalBody> getCollidingLadder();

    /**
     * Gets the {@link PlayerPhysicalBody} of the current {@link model.entities.Player}, if it has been created.
     * @return an {@link Optional} with the {@link PlayerPhysicalBody} of the {@link model.entities.Player} if the player has
     * been created, an {@link Optional#absent()} otherwise
     */
    Optional<PlayerPhysicalBody> getPlayerPhysicalBody();

    /**
     * Gets the {@link PowerUpType} of the {@link model.entities.PowerUp} which {@link PhysicalBody} is associated to the
     * given {@link Body}.
     * @param body the {@link Body} from which getting its associated {@link PowerUpType}
     * @return the {@link PowerUpType} associated with the passed {@link Body}
     */
    PowerUpType getPowerUpTypeFromBody(Body body);
}