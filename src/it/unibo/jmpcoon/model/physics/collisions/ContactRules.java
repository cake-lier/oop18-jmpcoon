package it.unibo.jmpcoon.model.physics.collisions;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.contact.ContactAdapter;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.geometry.Vector2;

import com.google.common.base.Optional;

import it.unibo.jmpcoon.model.entities.EntityState;
import it.unibo.jmpcoon.model.entities.EntityType;
import it.unibo.jmpcoon.model.physics.PhysicalBody;
import it.unibo.jmpcoon.model.physics.PhysicsUtils;
import it.unibo.jmpcoon.model.physics.ReadablePhysicalWorld;

/**
 * Class representing a listener for physical contacts between bodies required the dyn4j library so as to rule them.
 */
public class ContactRules extends ContactAdapter implements Serializable {
    private static final long serialVersionUID = -5814150230389633139L;
    private final ReadablePhysicalWorld world;

    /**
     * Default constructor, accepts a reference to an object which is a {@link it.unibo.jmpcoon.model.physics.ReadablePhysicalWorld},
     * so it's a {@link it.unibo.jmpcoon.model.physics.PhysicalWorld} but only with methods for getting informations about the
     * current physical state of the game. This constructor is package protected because only the {@link PhysicsRulesFactory} should
     * create an instance of this object.
     * @param world the {@link it.unibo.jmpcoon.model.physics.ReadablePhysicalWorld} from which getting informations about the physical state
     * of the game
     */
    ContactRules(final ReadablePhysicalWorld world) {
        super();
        this.world = world;
    }

    /**
     * {@inheritDoc}
     * The only contacts that matters are the ones between {@link it.unibo.jmpcoon.model.entities.Player} and
     * {@link it.unibo.jmpcoon.model.entities.Platform}. If the {@link it.unibo.jmpcoon.model.entities.Player} is in contact
     * (the prior collision already happened) with a {@link it.unibo.jmpcoon.model.entities.Ladder} and it was climbing it up
     * or down and is not at the top part of the {@link it.unibo.jmpcoon.model.entities.Ladder} where the climb should end, this
     * means that the contact with the {@link it.unibo.jmpcoon.model.entities.Ladder} should be ignored because it represents
     * a traversal of a {@link it.unibo.jmpcoon.model.entities.Platform} while the {@link it.unibo.jmpcoon.model.entities.Player}
     * is climbing and it shouldn't generate any physical response. The same goes if the {@link it.unibo.jmpcoon.model.entities.Player}
     * is climbing up the {@link it.unibo.jmpcoon.model.entities.Ladder} and is at its bottom or if it's climbing down it and
     * is at it's top, in those cases the {@link it.unibo.jmpcoon.model.entities.Ladder} is just moving on the
     * {@link it.unibo.jmpcoon.model.entities.Ladder} and as such the contact has shouldn't have any physical response.
     */
    @Override
    public boolean preSolve(final ContactPoint point) {
        final Body firstBody = point.getBody1();
        final EntityType firstType = this.world.getEntityTypeFromBody(firstBody);
        final Body secondBody = point.getBody2();
        final EntityType secondType = this.world.getEntityTypeFromBody(secondBody);
        if ((firstType == EntityType.PLAYER && secondType == EntityType.PLATFORM) 
            || (firstType == EntityType.PLATFORM && secondType == EntityType.PLAYER)) {
            final PhysicalBody playerBody = firstType == EntityType.PLAYER 
                                            ? this.world.getPhysicalBodyFromBody(firstBody)
                                            : this.world.getPhysicalBodyFromBody(secondBody);
            final EntityState playerState = playerBody.getState();
            final PhysicalBody platformBody = firstType == EntityType.PLATFORM 
                                              ? this.world.getPhysicalBodyFromBody(firstBody)
                                              : this.world.getPhysicalBodyFromBody(secondBody);
            final Optional<PhysicalBody> collidingLadder = this.world.getCollidingLadder();
            if ((playerState == EntityState.CLIMBING_DOWN || playerState == EntityState.CLIMBING_UP)
                && collidingLadder.isPresent()) {
                final Vector2 coordinates = point.getPoint();
                final PhysicalBody actualLadder = collidingLadder.get();
                if (!PhysicsUtils.isBodyOnTop(playerBody, platformBody, new ImmutablePair<>(coordinates.x, coordinates.y))
                    || ((playerState == EntityState.CLIMBING_DOWN 
                         && !PhysicsUtils.isBodyAtBottomHalf(playerBody, actualLadder))
                        || (playerState == EntityState.CLIMBING_UP 
                            && PhysicsUtils.isBodyAtBottomHalf(playerBody, actualLadder)))) {
                    return false;
                }
            }
        }
        return true;
    }
}
