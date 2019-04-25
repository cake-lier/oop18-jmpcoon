package it.unibo.jmpcoon.model.physics;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import it.unibo.jmpcoon.model.entities.EntityType;
import it.unibo.jmpcoon.model.entities.PowerUpType;
import it.unibo.jmpcoon.model.physics.collisions.PhysicsRulesFactory;
import it.unibo.jmpcoon.model.physics.collisions.PhysicsRulesFactoryImpl;
import it.unibo.jmpcoon.model.serializable.SerializableBody;
import it.unibo.jmpcoon.model.serializable.SerializableWorld;
import it.unibo.jmpcoon.model.world.NotifiableWorld;

/**
 * The class implementation of {@link PhysicalWorld}. It's package protected so the only class which can build it is the 
 * {@link PhysicalFactory}, the factory class for each one of the physical entities of this game.
 */
class PhysicalWorldImpl implements PhysicalWorld {
    private static final long serialVersionUID = -8486558535164534658L;
    private static final int INVINCIBILITY_DURATION = 400;
    private static final int HIT_COOLDOWN = 60;

    private final SerializableWorld world;
    private final BiMap<PhysicalBody, SerializableBody> physicalToBodyAssociations;
    private final Map<PhysicalBody, EntityType> bodyToEntityTypeAssociations;
    private final Map<SerializableBody, PowerUpType> bodyToPowerUpTypeAssociations;
    private Optional<PlayerPhysicalBody> playerPhysicalBody;
    private Optional<PhysicalBody> collidingLadder;
    private int stepCounterHit;
    private int stepCounterInvincibility;

    /**
     * Binds the current instance of {@link PhysicalWorldImpl} with the instance of {@link SerializableWorld} which will be
     * wrapped and used. This constructor is package protected because this class should be created only by the
     * {@link PhysicalFactory} contained in this package and no one else.
     * @param outerWorld the {@link it.unibo.jmpcoon.model.world.World} that wraps this {@link PhysicalWorld} kept as a
     * {@link it.unibo.jmpcoon.model.world.NotifiableWorld} so as to just notify it of
     * {@link it.unibo.jmpcoon.model.world.CollisionEvent}s happened
     * @param world the {@link SerializableWorld} to wrap
     */
    PhysicalWorldImpl(final NotifiableWorld outerWorld, final SerializableWorld world) {
        this.world = world;
        final PhysicsRulesFactory physics = new PhysicsRulesFactoryImpl();
        this.world.addListener(physics.createContactRules(this));
        this.world.addListener(physics.createCollisionRules(this, outerWorld));
        this.physicalToBodyAssociations = HashBiMap.create();
        this.bodyToEntityTypeAssociations = new LinkedHashMap<>();
        this.bodyToPowerUpTypeAssociations = new LinkedHashMap<>();
        this.collidingLadder = Optional.absent();
        this.playerPhysicalBody = Optional.absent();
        this.stepCounterHit = 0;
        this.stepCounterInvincibility = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhysicalBody getPhysicalBodyFromBody(final Body body) {
        return this.physicalToBodyAssociations.inverse().get(body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getEntityTypeFromBody(final Body body) {
        return this.bodyToEntityTypeAssociations.get(this.getPhysicalBodyFromBody(body));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PhysicalBody> getCollidingLadder() {
        return this.collidingLadder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PlayerPhysicalBody> getPlayerPhysicalBody() {
        return this.playerPhysicalBody;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PowerUpType> getPowerUpTypeFromBody(final Body body) {
        return Optional.fromNullable(this.bodyToPowerUpTypeAssociations.get(body));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContainerAssociation(final PhysicalBody container, final SerializableBody contained, final EntityType type) {
        this.putAssociation(container, contained, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPlayerAssociation(final PlayerPhysicalBody container, final SerializableBody contained) {
        this.playerPhysicalBody = Optional.of(container);
        this.putAssociation(container, contained, EntityType.PLAYER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPowerUpTypeAssociation(final SerializableBody contained, final PowerUpType type) {
        this.bodyToPowerUpTypeAssociations.putIfAbsent(contained, type);
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
        return this.physicalToBodyAssociations.get(first).isInContact(this.physicalToBodyAssociations.get(second));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeBody(final PhysicalBody body) {
        this.world.removeBody(this.physicalToBodyAssociations.get(body));
        this.physicalToBodyAssociations.remove(body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Pair<PhysicalBody, Pair<Double, Double>>> getCollidingBodies(final PhysicalBody body) {
        final Body innerBody = this.physicalToBodyAssociations.get(body);
        return innerBody.getContacts(false)
                        .parallelStream()
                        .<Pair<PhysicalBody, Pair<Double, Double>>>map(contact ->
                            new ImmutablePair<>(this.physicalToBodyAssociations.inverse()
                                                                               .get(contact.getBody1().equals(innerBody) 
                                                                                    ? contact.getBody2() 
                                                                                    : contact.getBody1()),
                                                new ImmutablePair<>(contact.getPoint().x, contact.getPoint().y)))
                        .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        if (this.playerPhysicalBody.isPresent()) {
            final PlayerPhysicalBody player = this.playerPhysicalBody.get();
            this.collidingLadder
                = Optional.fromJavaUtil(this.bodyToEntityTypeAssociations
                                            .entrySet()
                                            .parallelStream()
                                            .filter(e -> e.getValue() == EntityType.LADDER)
                                            .filter(l -> this.areBodiesInContact(player, l.getKey()))
                                            .findFirst()
                                            .map(cl -> cl.getKey()));
            if (player.isInvincible() && this.invincibilityEnded()) {
                player.endInvincibility();
            }
            if (player.isInvulnerable() && this.hitEnded()) {
                player.endInvulnerability();
            }
            if ((player.getPosition().getLeft() + player.getDimensions().getLeft() / 2) < 0
                || (player.getPosition().getRight() + player.getDimensions().getRight() / 2) < 0) {
                    player.kill();
            }
        }
        this.world.step(1);
    }

    /*
     * Puts a generic association inside this PhysicalWorld between a PhysicalBody, its Body and its EntityType.
     */
    private void putAssociation(final PhysicalBody container, final SerializableBody contained, final EntityType type) {
        this.physicalToBodyAssociations.putIfAbsent(container, contained);
        this.bodyToEntityTypeAssociations.putIfAbsent(container, type);
    }

    /*
     * Calculates if the hit cool-down has terminated or not.
     */
    private boolean hitEnded() {
        this.stepCounterHit = this.updateCounter(this.stepCounterHit, HIT_COOLDOWN);
        return this.counterEnded(this.stepCounterHit);
    }

    /*
     * Calculates if the power up that allows to be invincible has terminated its effects or not.
     */
    private boolean invincibilityEnded() {
        this.stepCounterInvincibility = this.updateCounter(this.stepCounterInvincibility, INVINCIBILITY_DURATION);
        return this.counterEnded(this.stepCounterInvincibility);
    }

    /*
     * Calculates the value of a counter given the value after which has to overflow.
     */
    private int updateCounter(final int currentValue, final int maxValue) {
        return (currentValue + 1) % maxValue;
    }

    /*
     * Calculates if a counter has overflowed.
     */
    private boolean counterEnded(final int currentValue) {
        return currentValue == 0;
    }
}
