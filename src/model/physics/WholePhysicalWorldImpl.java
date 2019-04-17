package model.physics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.Step;
import org.dyn4j.dynamics.StepAdapter;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactAdapter;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.geometry.Vector2;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import model.entities.EntityType;
import model.entities.EntityState;
import model.entities.PowerUpType;
import model.serializable.SerializableBody;
import model.serializable.SerializableWorld;
import model.world.CollisionEvent;
import model.world.NotifiableWorld;

/**
 * The class implementation of {@link PhysicalWorld}. It's package protected so the only class which can build it is the 
 * {@link PhysicalFactory}, the factory class for each one of the physical entities of this game.
 */
final class WholePhysicalWorldImpl implements WholePhysicalWorld {
    private static final long serialVersionUID = -8486558535164534658L;

    private static final int INVINCIBILITY_DURATION = 400;
    private static final int HIT_COOLDOWN = 60;

    private final SerializableWorld world;
    private final NotifiableWorld outerWorld;
    private final BiMap<PhysicalBody, SerializableBody> containers;
    private final Map<PhysicalBody, EntityType> types;
    private final Map<SerializableBody, PowerUpType> powerups;
    private Optional<PlayerPhysicalBody> player;
    private Optional<PhysicalBody> collidingLadder;

    private int stepCounterHit;
    private int stepCounterInvincibility;

    /**
     * Binds the current instance of {@link WholePhysicalWorldImpl} with the instance of {@link World} which will be wrapped and 
     * used.
     * @param world The {@link World} to wrap.
     */
    WholePhysicalWorldImpl(final NotifiableWorld outerWorld, final SerializableWorld world) {
        this.world = world;
        this.outerWorld = outerWorld;
        this.containers = HashBiMap.create();
        this.types = new LinkedHashMap<>();
        this.powerups = new LinkedHashMap<>();
        this.collidingLadder = Optional.absent();
        this.player = Optional.absent();
        this.stepCounterHit = 0;
        this.stepCounterInvincibility = 0;
        this.addCollisionRules();
    }


    /*
     * Sets the dyn4j World to use these rules when collisions happens. This is made by registering a CollisionAdapter which
     * specifies what to do on a collision event. What happens is that, if one of the entities involved is the player and the
     * other an enemy, the hit enemy is destroyed if the collision happen on the "head" of it or, if the collision doesn't happen
     * on the "head" of the enemy, the player is destroyed.
     */
    private void addCollisionRules() {
        this.world.addListener(new StepAdapter() {
            @Override
            public void begin(final Step step, final World world) {
                WholePhysicalWorldImpl.this.collidingLadder 
                    = Optional.fromJavaUtil(
                          WholePhysicalWorldImpl.this.types.entrySet()
                                                           .parallelStream()
                                                           .filter(e -> e.getValue() == EntityType.LADDER)
                                                           .filter(l -> WholePhysicalWorldImpl.this.areBodiesInContact(
                                                                            WholePhysicalWorldImpl.this.player.get(), l.getKey()))
                                                           .findFirst()
                                                           .map(cl -> cl.getKey()));
            }
        });
        this.world.addListener(new ContactAdapter() {
            @Override
            public boolean preSolve(final ContactPoint point) {
                final Body firstBody = point.getBody1();
                final PhysicalBody firstPhysicalBody = WholePhysicalWorldImpl.this.containers.inverse().get(firstBody);
                final EntityType firstType = WholePhysicalWorldImpl.this.types.get(firstPhysicalBody);
                final Body secondBody = point.getBody2();
                final PhysicalBody secondPhysicalBody = WholePhysicalWorldImpl.this.containers.inverse().get(secondBody);
                final EntityType secondType = WholePhysicalWorldImpl.this.types.get(secondPhysicalBody);
                if (firstType == EntityType.PLAYER || secondType == EntityType.PLAYER) {
                    final EntityType otherType = firstType != EntityType.PLAYER ? firstType : secondType;
                    final PhysicalBody otherBody = firstType != EntityType.PLAYER ? firstPhysicalBody : secondPhysicalBody;
                    final DynamicPhysicalBody playerBody = WholePhysicalWorldImpl.this.player.get();
                    final Vector2 coordinates = point.getPoint();
                    final EntityState playerState = playerBody.getState();
                    if (otherType == EntityType.PLATFORM && (playerState == EntityState.CLIMBING_DOWN || playerState == EntityState.CLIMBING_UP)
                        && WholePhysicalWorldImpl.this.collidingLadder.isPresent()) {
                        final PhysicalBody collidingLadder = WholePhysicalWorldImpl.this.collidingLadder.get();
                        if (!PhysicsUtils.isBodyOnTop(playerBody, otherBody, new ImmutablePair<>(coordinates.x, coordinates.y))
                            || ((playerState == EntityState.CLIMBING_DOWN 
                                 && !PhysicsUtils.isBodyAtBottomHalf(playerBody, collidingLadder))
                                || (playerState == EntityState.CLIMBING_UP 
                                    && PhysicsUtils.isBodyAtBottomHalf(playerBody, collidingLadder)))) {
                            return false;
                        }
                    }
                }
                return true;
            }
        });
        this.world.addListener(new CollisionAdapter() {
            @Override
            public boolean collision(final ContactConstraint contactConstraint) {
                final Body firstBody = contactConstraint.getBody1();
                final PhysicalBody firstPhysicalBody = WholePhysicalWorldImpl.this.containers.inverse().get(firstBody);
                final Triple<Body, PhysicalBody, EntityType> firstTriple = new ImmutableTriple<>(firstBody,
                        firstPhysicalBody, WholePhysicalWorldImpl.this.types.get(firstPhysicalBody));
                final Body secondBody = contactConstraint.getBody2();
                final PhysicalBody secondPhysicalBody = WholePhysicalWorldImpl.this.containers.inverse().get(secondBody);
                final Triple<Body, PhysicalBody, EntityType> secondTriple = new ImmutableTriple<>(secondBody,
                        secondPhysicalBody, WholePhysicalWorldImpl.this.types.get(secondPhysicalBody));
                if (firstTriple.getRight() == EntityType.PLAYER || secondTriple.getRight() == EntityType.PLAYER) {
                    final Triple<Body, PhysicalBody, EntityType> playerTriple = firstTriple.getRight() == EntityType.PLAYER
                                                                                ? firstTriple : secondTriple;
                    final Triple<Body, PhysicalBody, EntityType> otherTriple = firstTriple.getRight() != EntityType.PLAYER
                                                                               ? firstTriple : secondTriple;
                    final Vector2 point = contactConstraint.getContacts().get(0).getPoint();
                    final Pair<Double, Double> collisionPoint = new ImmutablePair<>(point.x, point.y);
                    final EntityState playerState = playerTriple.getMiddle().getState();
                    final NotifiableWorld world = WholePhysicalWorldImpl.this.outerWorld;
                    if (otherTriple.getRight() == EntityType.POWERUP) {
                        otherTriple.getLeft().setActive(false);
                        final PowerUpType type = WholePhysicalWorldImpl.this.powerups.get(otherTriple.getLeft());
                        if (type == PowerUpType.SUPER_STAR) {
                            world.notifyCollision(CollisionEvent.INVINCIBILITY_HIT);
                        } else {
                            world.notifyCollision(CollisionEvent.POWER_UP_HIT);
                        }
                        WholePhysicalWorldImpl.this.processPowerUp(type);
                    } else if (otherTriple.getRight() == EntityType.WALKING_ENEMY
                               || otherTriple.getRight() == EntityType.ROLLING_ENEMY) {
                        if (playerState == EntityState.CLIMBING_UP || playerState == EntityState.CLIMBING_DOWN) {
                            return false;
                        }
                        final PlayerPhysicalBody player = WholePhysicalWorldImpl.this.player.get();
                        if (player.isInvincible()) {
                            otherTriple.getLeft().setActive(false);
                            return true;
                        } else if (player.isInvulnerable()) {
                            return false;
                        }
                        if ((otherTriple.getRight() == EntityType.WALKING_ENEMY 
                                && PhysicsUtils.isBodyOnTop(playerTriple.getMiddle(), otherTriple.getMiddle(), collisionPoint))
                             || (otherTriple.getRight() == EntityType.ROLLING_ENEMY
                                 && PhysicsUtils.isBodyAbove(playerTriple.getMiddle(), otherTriple.getMiddle(), 
                                                             collisionPoint.getRight()))) {
                             otherTriple.getLeft().setActive(false);
                             world.notifyCollision(otherTriple.getRight() == EntityType.WALKING_ENEMY
                                                                             ? CollisionEvent.WALKING_ENEMY_KILLED
                                                                             : CollisionEvent.ROLLING_ENEMY_KILLED);
                             return true;
                        }
                        player.hit();
                        if (!player.exist()) {
                            world.notifyCollision(CollisionEvent.PLAYER_KILLED);
                        }
                    } else if (otherTriple.getRight() == EntityType.PLATFORM
                               && (playerState == EntityState.CLIMBING_DOWN || playerState == EntityState.CLIMBING_UP)
                               && WholePhysicalWorldImpl.this.collidingLadder.isPresent()) {
                        final PhysicalBody collidingLadder = WholePhysicalWorldImpl.this.collidingLadder.get();
                        if (PhysicsUtils.isBodyOnTop(playerTriple.getMiddle(), otherTriple.getMiddle(), collisionPoint)
                            && ((playerState == EntityState.CLIMBING_DOWN && PhysicsUtils.isBodyAtBottomHalf(playerTriple.getMiddle(),
                                                                                                             collidingLadder))
                                || (playerState == EntityState.CLIMBING_UP && !PhysicsUtils.isBodyAtBottomHalf(playerTriple.getMiddle(),
                                                                                                               collidingLadder)))) {
                            if (WholePhysicalWorldImpl.this.player.isPresent()) {
                                WholePhysicalWorldImpl.this.player.get().setIdle();
                            }
                        }
                    }
                }
                return true;
            }
        });
    }

    private void processPowerUp(final PowerUpType type) {
        if (type == PowerUpType.GOAL) {
            this.outerWorld.notifyCollision(CollisionEvent.GOAL_HIT); 
        } else {
            this.player.get().givePowerUp(type);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContainerAssociation(final PhysicalBody container, final SerializableBody contained, final EntityType type) {
        this.containers.putIfAbsent(container, contained);
        this.types.putIfAbsent(container, type);
        if (type == EntityType.PLAYER) {
            this.player = Optional.of(PlayerPhysicalBody.class.cast(container));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPowerUpTypeAssociation(final SerializableBody contained, final PowerUpType type) {
        this.powerups.putIfAbsent(contained, type);
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
        if (this.player.get().isInvincible() && this.invincibilityEnded()) {
            this.player.get().endInvincibility();
        }
        if (this.player.get().isInvulnerable() && this.hitEnded()) {
            this.player.get().endInvulnerability();
        }
        this.world.step(1);
    }

    private boolean hitEnded() {
        this.stepCounterHit = this.updateCounter(this.stepCounterHit, HIT_COOLDOWN);
        return this.counterEnded(this.stepCounterHit);
    }

    private boolean invincibilityEnded() {
        this.stepCounterInvincibility = this.updateCounter(this.stepCounterInvincibility, INVINCIBILITY_DURATION);
        return this.counterEnded(this.stepCounterInvincibility);
    }

    private int updateCounter(final int currentValue, final int maxValue) {
        return (currentValue + 1) % maxValue;
    }

    private boolean counterEnded(final int currentValue) {
        return currentValue == 0;
    }

    private void readObject(final ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        this.addCollisionRules();
    }
}
