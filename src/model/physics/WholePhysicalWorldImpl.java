package model.physics;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * The class implementation of {@link PhysicalWorld}. It's package protected so the only class which can build it is the 
 * {@link PhysicsFactory}, the factory class for each one of the physical entities of this game.
 */
final class WholePhysicalWorldImpl implements WholePhysicalWorld {
    private final World world;
    private final BiMap<PhysicalBody, Body> associations;
    /**
     * Binds the current instance of {@link WholePhysicalWorldImpl} with the instance of {@link World} which will be wrapped and 
     * used.
     * @param world The {@link World} to wrap.
     */
    WholePhysicalWorldImpl(final World world) {
        this.world = world;
        this.associations = HashBiMap.create();
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
    /**
     * {@inheritDoc}
     */
    @Override
    public void removePhysicalBody(final PhysicalBody body) {
        this.world.removeBody(this.associations.get(body));
        this.associations.remove(body);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Pair<PhysicalBody, Pair<Double, Double>>> collidingPhysicalBodies(final PhysicalBody body) {
        final Body innerBody = this.associations.get(body);
        return innerBody.getContacts(false).parallelStream()
                                           .<Pair<PhysicalBody, Pair<Double, Double>>>map(contact -> 
                                                                  new ImmutablePair<>(this.associations.inverse().get(
                                                                                          contact.getBody1().equals(innerBody)
                                                                                          ? contact.getBody2()
                                                                                          : contact.getBody1()),
                                                                                      new ImmutablePair<>(contact.getPoint().x,
                                                                                                          contact.getPoint().y)))
                                          .collect(Collectors.toSet());
    }
}
