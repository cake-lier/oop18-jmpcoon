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
     * @param position the position in which the {@link StaticPhysicalBody} is created
     * @param angle the angle in radians of the created {@link StaticPhysicalBody}
     * @return a {@link StaticPhysicalBody} with the given characteristics
     */
    StaticPhysicalBody createStaticPhysicalBody(Pair<Double, Double> position, double angle);

    // TODO: add method to create DynamicPhysicalBody

}
