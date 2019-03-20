package model.physics;

/**
 * Represents the facade for the entire creation requested to the physical engine in order to populate the levels. It creates
 * other facades to maintain a degree of separation between the dyn4j library and the implementation.
 */
public interface PhysicsFactory {

    /**
     * Creates a facade for the {@link org.dyn4j.dynamics.World} as created by the dyn4j library so as to be used by the game
     * {@link model.World} without the hassle of dealing with the library itself.
     * @param width The width of the inner {@link org.dyn4j.dynamics.World}.
     * @param height The height of the inner {@link org.dyn4j.dynamics.World}.
     * @return The facade for the inner {@link org.dyn4j.dynamics.World}.
     */
    PhysicalWorld createWorld(double width, double height);
}
