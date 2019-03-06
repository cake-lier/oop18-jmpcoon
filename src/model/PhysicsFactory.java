package model;

/**
 * Represents the facade for the entire creation requested to the physical engine in order
 * to populate the levels. It creates other facades to maintain a degree of separation
 * between library and implementation.
 */
public interface PhysicsFactory {
    /**
     * Creates a facade for the world as created by the chosen library so as to be used by
     * our {@link World} without the hassle of dealing with the library itself.
     * @param width The width of the inner library world.
     * @param height The height of the inner library world.
     * @return The facade for the world implementation of the chosen library.
     */
    PhysicalWorld createWorld(double width, double height);
}
