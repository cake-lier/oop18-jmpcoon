package model;

import utils.Pair;

/**
 * a interface for a class used to produce {@link PhysicalWorld} and {@link PhysicalBody}.
 */
public interface PhysicalFactory {

    /**
     * @return the {@link PhysicalWorld} in which all the {@link PhysicalBody} lives
     */
    PhysicalWorld createPhysicalWorld();

    /**
     * creates a rectangular {@link StaticPhysicalBody}.
     * @param position the upper left corner of the {@link StaticPhysicalBody} created
     * @param angle the angle in radians of the created {@link StaticPhysicalBody}
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param compenetrable whether the {@link StaticPhysicalBody} created can be crossed by other bodies
     * @return a {@link StaticPhysicalBody} with the given characteristics
     */
    StaticPhysicalBody createRectangleStaticPhysicalBody(Pair<Double, Double> position, double angle, double width, double height, boolean compenetrable);

    // TODO: add method to create DynamicPhysicalBody

}
