package model.physics;

import model.entities.EntityType;
import model.world.NotifiableWorld;
import model.entities.PowerUpType;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;


/**
 * An interface representing the facade for the entire creation requested to the physical engine in order to create the
 * {@link PhysicalWorld} and populate the levels through {@link PhysicalBody}s. These last must be created after the 
 * {@link PhysicalWorld}. It creates other facades to maintain a degree of separation between the dyn4j library and the
 * implementation.
 */
public interface PhysicalFactory extends Serializable {

    /**
     * A {@link PhysicalWorld} that manages the physics simulation needed by the {@link model.world.World}. Notifies the
     * {@link model.world.World} of occurred events through the methods contained in the interface {@link NotifiableWorld}. The
     * {@link PhysicalWorld} considers only positive coordinates.
     * @param outerWorld The reference to the {@link model.world.World} which contains only methods for notifying it of occurred
     * physical events, such as {@link NotifiableWorld#notifyCollision(model.world.CollisionType)}.
     * @param width The width of the {@link PhysicalWorld}.
     * @param height The height of the {@link PhysicalWorld}.
     * @return The {@link PhysicalWorld} with the given dimensions.
     * @throws IllegalStateException if a {@link PhysicalWorld} has already been created.
     */
    PhysicalWorld createPhysicalWorld(NotifiableWorld outerWorld, double width, double height) throws IllegalStateException;

    /**
     * Creates a {@link StaticPhysicalBody} living inside the {@link PhysicalWorld} created by the same {@link PhysicalFactory}.
     * @param position The center of the {@link StaticPhysicalBody} created.
     * @param angle The angle in radians of the created {@link StaticPhysicalBody}.
     * @param shape The {@link BodyShape} of {@link StaticPhysicalBody} created.
     * @param width The width of the {@link PhysicalBody} (or the diameter, if the {@link PhysicalBody} will have a circular shape).
     * @param height The height of the {@link PhysicalBody} (or the diameter, if the {@link PhysicalBody} will have a circular shape).
     * @param type The {@link EntityType} of the {@link model.entities.Entity} that will use the created {@link StaticPhysicalBody}.
     * @param powerUpType the {@link PowerUpType} if the type is a {@link PowerUp}.
     * @return A {@link StaticPhysicalBody} with the given characteristics.
     * @throws IllegalStateException If a {@link PhysicalWorld} has yet to be created.
     * @throws IllegalArgumentException If the given position is outside the {@link PhysicalWorld} bounds, or if the combination
     * of {@link EntityType}, {@link BodyShape} and dimensions is illegal. 
     */
    StaticPhysicalBody createStaticPhysicalBody(Pair<Double, Double> position, double angle, BodyShape shape,
                                                    double width, double height, EntityType type, Optional<PowerUpType> powerUpType)
                                                            throws IllegalStateException, IllegalArgumentException;

    /**
     * Creates {@link DynamicPhysicalBody} living inside the {@link PhysicalWorld} created by the same {@link PhysicalFactory}.
     * @param position The center of the {@link DynamicPhysicalBody} created.
     * @param angle The angle in radians of the created {@link DynamicPhysicalBody}.
     * @param shape The {@link BodyShape} of {@link DynamicPhysicalBody} created.
     *  @param width The width of the {@link PhysicalBody} (or the diameter, if the {@link PhysicalBody} will have a circular shape).
     * @param height The height of the {@link PhysicalBody} (or the diameter, if the {@link PhysicalBody} will have a circular shape).
     * @param type The {@link EntityType} of the {@link model.entities.Entity} that will use the created {@link DynamicPhysicalBody}
     * @return A {@link DynamicPhysicalBody} with the given characteristics.
     * @throws IllegalStateException If a {@link PhysicalWorld} has yet to be created.
     * @throws IllegalArgumentException If the given position is outside the {@link PhysicalWorld} bounds, or if the combination
     * of {@link EntityType}, {@link BodyShape} and dimensions is illegal. 
     */
    DynamicPhysicalBody createDynamicPhysicalBody(Pair<Double, Double> position, double angle, BodyShape shape,
                                                      double width, double height, EntityType type)
                                                              throws IllegalStateException, IllegalArgumentException;
}
