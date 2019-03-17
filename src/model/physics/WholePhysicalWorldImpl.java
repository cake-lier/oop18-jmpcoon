package model.physics;

import java.util.LinkedHashMap;
import java.util.Map;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

/**
 * The class implementation of {@link PhysicalWorld}. It's package protected so the only
 * class which can build it is the {@link PhysicsFactory}, the factory class for each one
 * of the physical entities of this game.
 */
final class WholePhysicalWorldImpl implements WholePhysicalWorld {
    private final World world;
    private final Map<PhysicalBody, Body> associations;
    /**
     * Binds the current instance of {@link WholePhysicalWorldImpl} with the instance of
     * {@link World} which will be wrapped and used.
     * @param world The {@link World} to wrap.
     */
    WholePhysicalWorldImpl(final World world) {
        this.world = world;
        this.associations = new LinkedHashMap<>();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void addContainerAssociation(final PhysicalBody container,
                                        final Body contained) {
        this.associations.putIfAbsent(container, contained);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public World getWorld() {
        return this.world;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean arePhysicalBodiesInContact(final PhysicalBody first,
                                              final PhysicalBody second) {
        return this.associations.get(first).isInContact(this.associations.get(second));
    }
}
