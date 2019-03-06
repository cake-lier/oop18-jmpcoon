package model;

import java.util.Optional;

import org.dyn4j.collision.AxisAlignedBounds;
import org.dyn4j.dynamics.World;

/**
 * The class implementation of {@link PhysicsFactory}.
 */
public final class PhysicsFactoryImpl implements PhysicsFactory {
    private static final String NO_TWO_WORLDS_MSG 
                                = "You can't create two worlds for this game";

    private Optional<World> world = Optional.empty();
    /**
     * {@inheritDoc}
     */
    @Override
    public PhysicalWorld createWorld(final double width, final double height) {
        if (this.world.isPresent()) {
            throw new IllegalStateException(NO_TWO_WORLDS_MSG);
        }
        this.world = Optional.of(new World(new AxisAlignedBounds(width, height)));
        return new PhysicalWorldImpl(this.world.get());
    }
}
