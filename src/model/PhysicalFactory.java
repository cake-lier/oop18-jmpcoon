package model;

import utils.Pair;

/**
 * a interface for a class used to produce {@link PhysicalWorld} and
 * {@link PhysicalBody}. The {@link PhysicalBody} must be created after the
 * {@link PhysicalWorld}.
 */
public interface PhysicalFactory {

    /**
     * @param width
     *            the width of the physical world
     * @param height
     *            the height of the physical world
     * @return the {@link PhysicalWorld} in which all the {@link PhysicalBody} lives
     */
    PhysicalWorld createPhysicalWorld(double width, double height);

    /**
     * creates {@link StaticPhysicalBody}.
     * 
     * @param position
     *            the upper left corner of the {@link StaticPhysicalBody} created
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
     *            the upper left corner of the {@link DynamicPhysicalBody} created
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
    DynamicPhysicalBody createDynamicPhysicaBody(Pair<Double, Double> position, double angle, EntityShape shape,
            double width, double height, EntityType type);

}
