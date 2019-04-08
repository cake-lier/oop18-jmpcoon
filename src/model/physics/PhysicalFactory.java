package model.physics;

import model.entities.EntityShape;
import model.entities.EntityType;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.Pair;

/**
 * An interface representing the facade for the entire creation requested to the physical engine in order to create the
 * {@link PhysicalWorld} and populate the levels through {@link PhysicalBody}s. These last must be created after the 
 * {@link PhysicalWorld}. It creates other facades to maintain a degree of separation between the dyn4j library and the
 * implementation.
 */
public interface PhysicalFactory extends Serializable {

    /**
     * A {@link PhysicalWorld} that manages the physics simulation needed by the {@link model.world.World}.
     * It considers only positive coordinates.
     * @param width The width of the {@link PhysicalWorld}.
     * @param height The height of the {@link PhysicalWorld}.
     * @return The {@link PhysicalWorld} with the given dimensions.
     * @throws IllegalStateException if a {@link PhysicalWorld} has already been created.
     */
    PhysicalWorld createPhysicalWorld(double width, double height) throws IllegalStateException;

    /**
     * Creates a {@link StaticPhysicalBody} living inside the {@link PhysicalWorld} created by the same {@link PhysicalFactory}.
     * @param position The center of the {@link StaticPhysicalBody} created.
     * @param angle The angle in radians of the created {@link StaticPhysicalBody}.
     * @param shape The {@link EntityShape} of {@link StaticPhysicalBody} created.
     * @param width The width of the {@link PhysicalBody}.
     * @param height The height of the {@link PhysicalBody}.
     * @param type The {@link EntityType} of the {@link Entity} that will use the created {@link StaticPhysicalBody}.
     * @return A {@link StaticPhysicalBody} with the given characteristics.
     * @throws IllegalStateException If a {@link PhysicalWorld} has yet to be created.
     * @throws IllegalArgumentException If the given position is outside the {@link PhysicalWorld} bounds, or if the combination
     * of {@link EntityType} and {@link EntityShape} is illegal. 
     */
    StaticPhysicalBody createStaticPhysicalBody(Pair<Double, Double> position, double angle, EntityShape shape,
            double width, double height, EntityType type) throws IllegalStateException, IllegalArgumentException;

    /**
     * Creates {@link DynamicPhysicalBody} living inside the {@link PhysicalWorld} created by the same {@link PhysicalFactory}.
     * @param position The center of the {@link DynamicPhysicalBody} created.
     * @param angle The angle in radians of the created {@link DynamicPhysicalBody}.
     * @param shape The {@link EntityShape} of {@link DynamicPhysicalBody} created.
     *  @param width The width of the {@link PhysicalBody}.
     * @param height The height of the {@link PhysicalBody}.
     * @param type The {@link EntityType} of the {@link Entity} that will use the created {@link DynamicPhysicalBody}
     * @return A {@link DynamicPhysicalBody} with the given characteristics.
     * @throws IllegalStateException If a {@link PhysicalWorld} has yet to be created.
     * @throws IllegalArgumentException If the given position is outside the {@link PhysicalWorld} bounds, or if the combination
     * of {@link EntityType} and {@link EntityShape} is illegal. 
     */
    DynamicPhysicalBody createDynamicPhysicalBody(Pair<Double, Double> position, double angle, EntityShape shape,
            double width, double height, EntityType type)  throws IllegalStateException, IllegalArgumentException;

}
