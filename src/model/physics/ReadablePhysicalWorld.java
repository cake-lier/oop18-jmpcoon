package model.physics;

import java.io.Serializable;

import org.dyn4j.dynamics.Body;

import com.google.common.base.Optional;

import model.entities.EntityType;
import model.entities.PowerUpType;

/**
 * 
 */
public interface ReadablePhysicalWorld extends Serializable {
    /**
     * 
     * @param body
     * @return
     */
    PhysicalBody getPhysicalBodyFromBody(final Body body);

    /**
     * 
     * @param body
     * @return
     */
    EntityType getEntityTypeFromBody(final Body body);

    /**
     * 
     * @return
     */
    Optional<PhysicalBody> getCollidingLadder();

    /**
     * 
     * @return
     */
    Optional<PlayerPhysicalBody> getPlayerPhysicalBody();

    /**
     * 
     * @param body
     * @return
     */
    PowerUpType getPowerUpTypeFromBody(final Body body);
}
