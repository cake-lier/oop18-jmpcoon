package model;

import com.sun.webkit.ThemeClient;

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
     * creates {@link StaticPhysicalBody}.
     * @param position the upper left corner of the {@link StaticPhysicalBody} created
     * @param angle the angle in radians of the created {@link StaticPhysicalBody}
     * @param shape the {@link EntityShape} of {@link StaticPhysicalBody} created
     * @param width 
     * @param height
     * @param type the {@link EntityType} of the {@link Entity} that will use the created {@link StaticPhysicalBody}
     * @return a {@link StaticPhysicalBody} with the given characteristics
     */
    StaticPhysicalBody createStaticPhysicalBody(Pair<Double, Double> position, double angle, EntityShape shape, double width, double height, EntityType type);

    // TODO: add method to create DynamicPhysicalBody

}
