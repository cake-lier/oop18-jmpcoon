package model.physics;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.Vector2;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import model.entities.EntityType;
import model.entities.State;

/**
 * The class implementation of {@link PhysicalWorld}. It's package protected so the only class which can build it is the 
 * {@link PhysicsFactory}, the factory class for each one of the physical entities of this game.
 */
final class WholePhysicalWorldImpl implements WholePhysicalWorld {
    private static final double PRECISION = 0.001;

    private final World world;
    private final BiMap<PhysicalBody, Body> containers;
    private final Map<Body, EntityType> types;

    /**
     * Binds the current instance of {@link WholePhysicalWorldImpl} with the instance of {@link World} which will be wrapped and 
     * used.
     * @param world The {@link World} to wrap.
     */
    WholePhysicalWorldImpl(final World world) {
        this.world = world;
        this.containers = HashBiMap.create();
        this.types = new LinkedHashMap<>();
        this.addCollisionRules();
    }

    /*
     * Sets the dyn4j World to use these rules when collisions happens. This is made by registering a CollisionAdapter which
     * specifies what to do on a collision event. What happens is that, if one of the entities involved is the player and the
     * other an enemy, the hit enemy is destroyed if the collision happen on the "head" of it or, if the collision doesn't happen
     * on the "head" of the enemy, the player is destroyed.
     */
    private void addCollisionRules() {
        this.world.addListener(new CollisionAdapter() {
            @Override
            public boolean collision(final ContactConstraint contactConstraint) {
                final Body firstBody = contactConstraint.getBody1();
                final EntityType firstBodyType = WholePhysicalWorldImpl.this.types.get(firstBody);
                final Body secondBody = contactConstraint.getBody2();
                final EntityType secondBodyType = WholePhysicalWorldImpl.this.types.get(secondBody);
                if (firstBodyType == EntityType.PLAYER || secondBodyType == EntityType.PLAYER) {
                    final Body playerBody = firstBodyType == EntityType.PLAYER ? firstBody : secondBody;
                    final PhysicalBody playerPhysicalBody = WholePhysicalWorldImpl.this.containers.inverse().get(playerBody);
                    final EntityType otherType = firstBodyType != EntityType.PLAYER ? firstBodyType : secondBodyType;
                    final Body otherBody = firstBodyType != EntityType.PLAYER ? firstBody : secondBody;
                    final PhysicalBody otherPhysicalBody = WholePhysicalWorldImpl.this.containers.inverse().get(otherBody);
                    final Vector2 collisionPoint = contactConstraint.getContacts().get(0).getPoint();
                    final State playerState = playerPhysicalBody.getState();
                    if ((otherType == EntityType.WALKING_ENEMY
                         && (playerPhysicalBody.getPosition().getRight() - playerPhysicalBody.getDimensions().getRight() / 2)
                             - collisionPoint.y < PRECISION
                         && collisionPoint.y - (otherPhysicalBody.getPosition().getRight() 
                                                + otherPhysicalBody.getDimensions().getRight() / 2) < PRECISION) 
                        || (otherType == EntityType.ROLLING_ENEMY
                            && (playerPhysicalBody.getPosition().getRight() - playerPhysicalBody.getDimensions().getRight() / 2)
                                - collisionPoint.y < PRECISION
                            && collisionPoint.y > otherPhysicalBody.getPosition().getRight())) {
                        otherBody.setActive(false);
                    } else if (otherType == EntityType.WALKING_ENEMY || otherType == EntityType.ROLLING_ENEMY) {
                        playerBody.setActive(false);
                    } else if (otherType == EntityType.PLATFORM
                               && (playerState == State.CLIMBING_DOWN || playerState == State.CLIMBING_UP)) {
                        if ((playerPhysicalBody.getPosition().getRight() - playerPhysicalBody.getDimensions().getRight() / 2)
                            - collisionPoint.y < PRECISION
                            && collisionPoint.y - (otherPhysicalBody.getPosition().getRight() 
                                                   + otherPhysicalBody.getDimensions().getRight() / 2) < PRECISION) {
                            //playerPhysicalBody.unClimb();
                        } else {
                            return false;
                        }
                    }
                }
                return true;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContainerAssociation(final PhysicalBody container, final Body contained, final EntityType type) {
        this.containers.putIfAbsent(container, contained);
        this.types.putIfAbsent(contained, type);
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
    public boolean areBodiesInContact(final PhysicalBody first, final PhysicalBody second) {
        return this.containers.get(first).isInContact(this.containers.get(second));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeBody(final PhysicalBody body) {
        this.world.removeBody(this.containers.get(body));
        this.containers.remove(body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Pair<PhysicalBody, Pair<Double, Double>>> getCollidingBodies(final PhysicalBody body) {
        final Body innerBody = this.containers.get(body);
        return innerBody.getContacts(false).parallelStream()
                                           .<Pair<PhysicalBody, Pair<Double, Double>>>map(contact -> 
                                                                  new ImmutablePair<>(this.containers.inverse().get(
                                                                                          contact.getBody1().equals(innerBody)
                                                                                          ? contact.getBody2()
                                                                                          : contact.getBody1()),
                                                                                      new ImmutablePair<>(contact.getPoint().x,
                                                                                                          contact.getPoint().y)))
                                          .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        this.world.step(1);
    }
}
