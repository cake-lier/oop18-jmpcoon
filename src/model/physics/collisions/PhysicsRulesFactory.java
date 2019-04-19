package model.physics.collisions;

import model.physics.ReadablePhysicalWorld;
import model.world.NotifiableWorld;

/**
 * Interface for a factory of listeners which model the rules of physics of this game. The necessity of a factory is due
 * to the fact that a listener shouldn't be created twice, otherwise if used correctly the collisions will be resolved
 * twice. This interface could not rule out the fact that this factory itself could be created twice and because of this
 * create the rules twice, but this couldn't be done because the rules needs to be created once per game and not once
 * per application execution. That being said, it's left as a warning to never instantiate twice this factory, also
 * because it's a factory and should produce more elements if needed without another instantiation, differently from a
 * builder.
 */
public interface PhysicsRulesFactory {
    /**
     * Create a new listener which rules contacts between the {@link model.physics.PhysicalBody}s of this
     * {@link model.physics.PhysicalWorld}.
     * @param world the {@link model.physics.PhysicalWorld} from which getting the needed informations for physical
     * calculations
     * @return a new {@link ContactRules} object which will manage contacts between {@link model.physics.PhysicalBody}s
     * in this {@link model.physics.PhysicalWorld}
     * @throws IllegalStateException if the {@link ContactRules} have already been created
     */
    ContactRules createContactRules(ReadablePhysicalWorld world) throws IllegalStateException;

    /**
     * Create a new listener which rules collisions between the {@link model.physics.PhysicalBody}s of this
     * {@link model.physics.PhysicalWorld}.
     * @param physicalWorld the {@link model.physics.PhysicalWorld} from which getting the needed informations for physical
     * calculations
     * @param outerWorld the {@link model.world.World} to notify when a collision has happened
     * @return a new {@link ContactRules} object which will manage contacts between {@link model.physics.PhysicalBody}s
     * in this {@link model.physics.PhysicalWorld}
     * @throws IllegalStateException if the {@link ContactRules} have already been created
     */
    CollisionRules createCollisionRules(ReadablePhysicalWorld physicalWorld, NotifiableWorld outerWorld) 
        throws IllegalStateException;
}
