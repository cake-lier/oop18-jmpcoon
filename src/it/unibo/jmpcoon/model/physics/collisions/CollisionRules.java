package it.unibo.jmpcoon.model.physics.collisions;


import java.io.Serializable;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.Vector2;

import com.google.common.base.Optional;

import it.unibo.jmpcoon.model.entities.EntityState;
import it.unibo.jmpcoon.model.entities.EntityType;
import it.unibo.jmpcoon.model.entities.PowerUpType;
import it.unibo.jmpcoon.model.physics.PhysicalBody;
import it.unibo.jmpcoon.model.physics.PhysicsUtils;
import it.unibo.jmpcoon.model.physics.PlayerPhysicalBody;
import it.unibo.jmpcoon.model.physics.ReadablePhysicalWorld;
import it.unibo.jmpcoon.model.world.CollisionEvent;
import it.unibo.jmpcoon.model.world.NotifiableWorld;

/**
 * Class representing a listener for physical collisions between bodies required by the dyn4j library so as to rule them.
 */
public final class CollisionRules extends CollisionAdapter implements Serializable {
    private static final long serialVersionUID = 7929553481812283534L;
    private final NotifiableWorld outerWorld;
    private final ReadablePhysicalWorld physicalWorld;

    /**
     * Default constructor, accepts a reference to an object which is a {@link it.unibo.jmpcoon.model.physics.ReadablePhysicalWorld}, so it's a
     * {@link it.unibo.jmpcoon.model.physics.PhysicalWorld} but only with methods for getting informations about the current physical state of
     * the game. Moreover, it accepts a reference to an object which is a {@link it.unibo.jmpcoon.model.world.NotifiableWorld}, so it's a
     * {@link it.unibo.jmpcoon.model.world.World} but with only methods to notify it of a particular collision that happened between bodies.
     * This constructor is package protected because only the {@link PhysicsRulesFactory} should create an instance of this object.
     * @param physicalWorld the {@link it.unibo.jmpcoon.model.physics.ReadablePhysicalWorld} from which getting informations about the physical
     * state of the game
     * @param outerWorld the {@link it.unibo.jmpcoon.model.world.NotifiableWorld} to be notified of {@link CollisionEvent}s
     */
    CollisionRules(final ReadablePhysicalWorld physicalWorld, final NotifiableWorld outerWorld) {
        super();
        this.physicalWorld = physicalWorld;
        this.outerWorld = outerWorld;
    }

    /**
     * {@inheritDoc}
     * The only collisions that should be managed are the ones between the {@link it.unibo.jmpcoon.model.entities.Player} and the other
     * {@link it.unibo.jmpcoon.model.entities.Entity}s. If the {@link it.unibo.jmpcoon.model.entities.Player} collides with a {@link it.unibo.jmpcoon.model.entities.PowerUp},
     * this last one should be destroyed and the {@link it.unibo.jmpcoon.model.entities.Player} should acquire its power, unless the
     * {@link it.unibo.jmpcoon.model.entities.PowerUp} is the goal and as such the game should end. After this, the {@link NotifiableWorld}
     * should be notified that a {@link it.unibo.jmpcoon.model.entities.PowerUp} was acquired.
     * If the {@link it.unibo.jmpcoon.model.entities.Player} collides with an enemy, such as a {@link it.unibo.jmpcoon.model.entities.WalkingEnemy} or a
     * {@link it.unibo.jmpcoon.model.entities.RollingEnemy}, if it's invincible or it collides with it on the top of the enemy, the enemy
     * should die and the {@link NotifiableWorld} gets informed of it. If the player is invulnerable the collision simply
     * is ignored, as never happened in the first place, otherwise the {@link it.unibo.jmpcoon.model.entities.Player} gets hit and, if it
     * has no lives left, it should die, and the {@link NotifiableWorld} get notified of this.
     * If the {@link it.unibo.jmpcoon.model.entities.Player} collides with a {@link it.unibo.jmpcoon.model.entities.Platform} on its top and does this
     * while it's climbing up and is at the top of the {@link it.unibo.jmpcoon.model.entities.Ladder} or is climbing down and is at the
     * bottom of it, this means the {@link it.unibo.jmpcoon.model.entities.Player} reached an end of the {@link it.unibo.jmpcoon.model.entities.Ladder}
     * and for this its climb should stop by going back to an {@link EntityState#IDLE} state.
     */
    @Override
    public boolean collision(final ContactConstraint contactConstraint) {
        final Body firstBody = contactConstraint.getBody1();
        final Triple<Body, PhysicalBody, EntityType> firstTriple 
            = new ImmutableTriple<>(firstBody, this.physicalWorld.getPhysicalBodyFromBody(firstBody),
                                    this.physicalWorld.getEntityTypeFromBody(firstBody));
        final Body secondBody = contactConstraint.getBody2();
        final Triple<Body, PhysicalBody, EntityType> secondTriple 
            = new ImmutableTriple<>(secondBody, this.physicalWorld.getPhysicalBodyFromBody(secondBody),
                                    this.physicalWorld.getEntityTypeFromBody(secondBody));
        if (firstTriple.getRight() == EntityType.PLAYER || secondTriple.getRight() == EntityType.PLAYER) {
            final Optional<PlayerPhysicalBody> playerPhysicalBodyOpt = this.physicalWorld.getPlayerPhysicalBody();
            if (playerPhysicalBodyOpt.isPresent()) {
                final Triple<Body, PhysicalBody, EntityType> otherTriple
                    = firstTriple.getRight() != EntityType.PLAYER ? firstTriple : secondTriple;
                final PlayerPhysicalBody playerPhysicalBody = playerPhysicalBodyOpt.get();
                final Vector2 point = contactConstraint.getContacts().get(0).getPoint();
                final Pair<Double, Double> collisionPoint = new ImmutablePair<>(point.x, point.y);
                final EntityState playerState = playerPhysicalBody.getState();
                if (otherTriple.getRight() == EntityType.POWERUP) {
                    this.processPowerUp(playerPhysicalBody, otherTriple.getLeft());
                } else if (otherTriple.getRight() == EntityType.WALKING_ENEMY
                           || otherTriple.getRight() == EntityType.ROLLING_ENEMY) {
                    return this.processEnemyCollision(playerPhysicalBody, playerState, otherTriple, collisionPoint);
                } else if (otherTriple.getRight() == EntityType.PLATFORM) {
                    this.processPlatform(playerPhysicalBody, playerState, otherTriple.getMiddle(), collisionPoint);
                }
            }
        }
        return true;
    }

    /*
     * Method for elaborating collision rules in a collision between the player and a power up.
     */
    private void processPowerUp(final PlayerPhysicalBody playerPhysicalBody, final Body powerUpBody) {
        powerUpBody.setActive(false);
        final Optional<PowerUpType> optionalType = this.physicalWorld.getPowerUpTypeFromBody(powerUpBody);
        if (optionalType.isPresent()) {
            final PowerUpType type = optionalType.get();
            if (type != PowerUpType.GOAL) {
                playerPhysicalBody.givePowerUp(type);
            }
            this.outerWorld.notifyCollision(type.getAssociatedEvent());
        }
    }

    /*
     * Method for elaborating collision rules in a collision between the player and an enemy.
     */
    private boolean processEnemyCollision(final PlayerPhysicalBody playerPhysicalBody, final EntityState playerState, 
                                          final Triple<Body, PhysicalBody, EntityType> enemyTriple,
                                          final Pair<Double, Double> collisionPoint) {
        if (playerState == EntityState.CLIMBING_UP || playerState == EntityState.CLIMBING_DOWN) {
            return false;
        }
        if (playerPhysicalBody.isInvincible()
            || (enemyTriple.getRight() == EntityType.WALKING_ENEMY 
                && PhysicsUtils.isBodyOnTop(playerPhysicalBody, enemyTriple.getMiddle(), collisionPoint))
            || (enemyTriple.getRight() == EntityType.ROLLING_ENEMY
                && PhysicsUtils.isBodyAbove(playerPhysicalBody, enemyTriple.getMiddle(), collisionPoint))) {
            enemyTriple.getLeft().setActive(false);
            this.outerWorld.notifyCollision(enemyTriple.getRight() == EntityType.WALKING_ENEMY
                                            ? CollisionEvent.WALKING_ENEMY_KILLED
                                            : CollisionEvent.ROLLING_ENEMY_KILLED);
            return true;
        } else if (playerPhysicalBody.isInvulnerable()) {
            return false;
        }
        playerPhysicalBody.hit();
        if (!playerPhysicalBody.exists()) {
            this.outerWorld.notifyCollision(CollisionEvent.PLAYER_KILLED);
        }
        return true;
    }

    /*
     * Method for elaborating collision rules in a collision between the player and a platform.
     */
    private void processPlatform(final PlayerPhysicalBody playerPhysicalBody, final EntityState playerState,
                                 final PhysicalBody platformPhysicalBody, final Pair<Double, Double> collisionPoint) {
        final Optional<PhysicalBody> collidingLadder = this.physicalWorld.getCollidingLadder();
        if ((playerState == EntityState.CLIMBING_DOWN || playerState == EntityState.CLIMBING_UP)
            && collidingLadder.isPresent()) {
            final PhysicalBody actualLadder = collidingLadder.get();
            final boolean isPlayerAtBottomLadder = PhysicsUtils.isBodyAtBottomHalf(playerPhysicalBody, actualLadder);
            if (PhysicsUtils.isBodyOnTop(playerPhysicalBody, platformPhysicalBody, collisionPoint)
                && ((playerState == EntityState.CLIMBING_DOWN && isPlayerAtBottomLadder)
                    || (playerState == EntityState.CLIMBING_UP && !isPlayerAtBottomLadder))) {
                playerPhysicalBody.setIdle();
            }
        }
    }
}
