package model;

import org.dyn4j.dynamics.World;

/**
 * The class implementation of {@link PhysicalWorld}.
 */
public class PhysicalWorldImpl implements PhysicalWorld {
    private final World world;
    /**
     * Binds the current instance of {@link PhysicalWorld} with the instance of
     * {@link org.dyn4j.dynamics.World} which will be wrapped and used.
     * @param world The library world to wrap.
     */
    public PhysicalWorldImpl(final World world) {
        this.world = world;
    }
}
