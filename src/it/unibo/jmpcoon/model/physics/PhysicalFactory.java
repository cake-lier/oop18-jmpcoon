package it.unibo.jmpcoon.model.physics;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;

import it.unibo.jmpcoon.model.entities.EntityType;
import it.unibo.jmpcoon.model.entities.PowerUpType;
import it.unibo.jmpcoon.model.world.NotifiableWorld;

/**
 * An interface representing the facade for the entire creation requested to the physical engine in order to create the
 * {@link PhysicalWorld} and populate the levels through {@link PhysicalBody}s. These last must be created after the 
 * {@link PhysicalWorld}.
 */
public interface PhysicalFactory extends Serializable {
    /**
     * A {@link PhysicalWorld} that manages the physics simulation needed by the {@link it.unibo.jmpcoon.model.world.World}.
     * Notifies the {@link it.unibo.jmpcoon.model.world.World} of occurred events through the methods contained in the interface
     * {@link NotifiableWorld}. The {@link PhysicalWorld} considers only positive coordinates.
     * @param outerWorld the reference to the {@link it.unibo.jmpcoon.model.world.World} which contains only methods for
     * notifying it of occurred physical events, such as
     * {@link it.unibo.jmpcoon.model.world.NotifiableWorld#notifyCollision(it.unibo.jmpcoon.model.world.CollisionEvent)}
     * @param width the width of the {@link PhysicalWorld}
     * @param height the height of the {@link PhysicalWorld}
     * @return the {@link PhysicalWorld} with the given dimensions
     * @throws IllegalStateException if a {@link PhysicalWorld} has already been created
     */
    UpdatablePhysicalWorld createPhysicalWorld(NotifiableWorld outerWorld, double width, double height)
                                               throws IllegalStateException;

    /**
     * Creates a {@link StaticPhysicalBody} living inside the {@link PhysicalWorld} created by the same {@link PhysicalFactory}.
     * @param position the center of the {@link StaticPhysicalBody} created
     * @param angle the angle in radians of the created {@link StaticPhysicalBody}
     * @param shape the {@link BodyShape} of {@link StaticPhysicalBody} created
     * @param width the width of the {@link PhysicalBody} (or the diameter, if the {@link PhysicalBody} will have a circular 
     * shape)
     * @param height the height of the {@link PhysicalBody} (or the diameter, if the {@link PhysicalBody} will have a circular 
     * shape)
     * @param type the {@link EntityType} of the {@link it.unibo.jmpcoon.model.entities.Entity} that will use the created 
     * {@link StaticPhysicalBody}
     * @param powerUpType the {@link PowerUpType} if the type is a {@link it.unibo.jmpcoon.model.entities.PowerUp}
     * @return a {@link StaticPhysicalBody} with the given characteristics
     * @throws IllegalStateException if a {@link PhysicalWorld} has yet to be created
     * @throws IllegalArgumentException if the given position is outside the {@link PhysicalWorld} bounds, or if the combination
     * of {@link EntityType}, {@link BodyShape} and dimensions is illegal
     */
    StaticPhysicalBody createStaticPhysicalBody(Pair<Double, Double> position, double angle, BodyShape shape, double width, 
                                                double height, EntityType type, Optional<PowerUpType> powerUpType)
                                                throws IllegalStateException, IllegalArgumentException;

    /**
     * Creates a {@link DynamicPhysicalBody} living inside the {@link PhysicalWorld} created by the same {@link PhysicalFactory}.
     * @param position the center of the {@link DynamicPhysicalBody} created
     * @param angle the angle in radians of the {@link DynamicPhysicalBody} created
     * @param shape the {@link BodyShape} of {@link DynamicPhysicalBody} created
     * @param width the width of the {@link PhysicalBody} (or the diameter, if the {@link PhysicalBody} will have a circular 
     * shape)
     * @param height the height of the {@link PhysicalBody} (or the diameter, if the {@link PhysicalBody} will have a circular
     * shape)
     * @param type the {@link EntityType} of the {@link it.unibo.jmpcoon.model.entities.Entity} that will use the created 
     * {@link DynamicPhysicalBody}
     * @return a {@link DynamicPhysicalBody} with the given characteristics
     * @throws IllegalStateException if a {@link PhysicalWorld} has yet to be created
     * @throws IllegalArgumentException if the given position is outside the {@link PhysicalWorld} bounds, or if the combination
     * of {@link EntityType}, {@link BodyShape} and dimensions is illegal
     */
    DynamicPhysicalBody createDynamicPhysicalBody(Pair<Double, Double> position, double angle, BodyShape shape,
                                                  double width, double height, EntityType type)
                                                  throws IllegalStateException, IllegalArgumentException;

    /**
     * Creates {@link PlayerPhysicalBody} living inside the {@link PhysicalWorld} created by the same {@link PhysicalFactory}.
     * @param position the center of the {@link PlayerPhysicalBody} created
     * @param angle the angle in radians of the {@link PlayerPhysicalBody} created
     * @param shape the {@link BodyShape} of the {@link PlayerPhysicalBody} created
     * @param width the width of the {@link PhysicalBody} (or the diameter, if the {@link PhysicalBody} will have a circular
     * shape)
     * @param height the height of the {@link PhysicalBody} (or the diameter, if the {@link PhysicalBody} will have a circular
     * shape)
     * @return a {@link PlayerPhysicalBody} with the given characteristics
     * @throws IllegalStateException if a {@link PhysicalWorld} has yet to be created
     * @throws IllegalArgumentException if the given position is outside the {@link PhysicalWorld} bounds, or if the combination
     * of {@link EntityType}, {@link BodyShape} and dimensions is illegal
     */
    PlayerPhysicalBody createPlayerPhysicalBody(Pair<Double, Double> position, double angle, BodyShape shape,
                                                double width, double height) throws IllegalStateException;
}
