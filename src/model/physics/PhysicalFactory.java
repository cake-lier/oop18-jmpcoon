package model.physics;

import model.entities.EntityShape;
import model.entities.EntityType;
import org.apache.commons.lang3.tuple.Pair;

/**
 * An interface representing the facade for the entire creation requested to the physical engine in order to create the
 * {@link PhysicalWorld} and populate the levels through {@link PhysicalBody}s. These last must be created after the 
 * {@link PhysicalWorld}. It creates other facades to maintain a degree of separation between the dyn4j library and the
 * implementation.
 */
public interface PhysicalFactory {

    /**
     * Creates a facade for the {@link org.dyn4j.dynamics.World} as created by the dyn4j library so as to be used by the game
     * {@link model.world.World} without the hassle of dealing with the library itself.
     * @param width The width of the inner {@link org.dyn4j.dynamics.World}.
     * @param height The height of the inner {@link org.dyn4j.dynamics.World}.
     * @return The facade for the inner {@link org.dyn4j.dynamics.World}.
     */
    PhysicalWorld createPhysicalWorld(double width, double height);

    /**
     * creates {@link StaticPhysicalBody}.
     * 
     * @param position
     *            the center of the {@link StaticPhysicalBody} created
     * @param angle
     *            the angle in radians of the created {@link StaticPhysicalBody}
     * @param shape
     *            the {@link EntityShape} of {@link StaticPhysicalBody} created
     * @param width
     *            the width of the physical body
     * @param height
     *            the height of the physical body
     * @param type
     *            the {@link EntityType} of the {@link Entity} that will use the
     *            created {@link StaticPhysicalBody}
     * @return a {@link StaticPhysicalBody} with the given characteristics
     */
    StaticPhysicalBody createStaticPhysicalBody(Pair<Double, Double> position, double angle, EntityShape shape,
            double width, double height, EntityType type);

    /**
     * creates {@link DynamicPhysicalBody}.
     * 
     * @param position
     *            the center of the {@link DynamicPhysicalBody} created
     * @param angle
     *            the angle in radians of the created {@link DynamicPhysicalBody}
     * @param shape
     *            the {@link EntityShape} of {@link DynamicPhysicalBody} created
     * @param width
     *            the width of the physical body
     * @param height
     *            the height of the physical body
     * @param type
     *            the {@link EntityType} of the {@link Entity} that will use the
     *            created {@link DynamicPhysicalBody}
     * @return a {@link DynamicPhysicalBody} with the given characteristics
     */
    DynamicPhysicalBody createDynamicPhysicalBody(Pair<Double, Double> position, double angle, EntityShape shape,
            double width, double height, EntityType type);

}