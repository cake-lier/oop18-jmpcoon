package it.unibo.jmpcoon.model.physics.collisions;

import it.unibo.jmpcoon.model.physics.ReadablePhysicalWorld;
import it.unibo.jmpcoon.model.world.NotifiableWorld;

/**
 * Class implementation of {@link PhysicsRulesFactory}.
 */
public class PhysicsRulesFactoryImpl implements PhysicsRulesFactory {
    private static final String CONT_INIT_ERR = "A ContactRules instance has already been created";
    private static final String COLL_INIT_ERR = "A CollisionRules instance has already been created";
    private boolean contactRulesCreated;
    private boolean collisionRulesCreated;

    /**
     * {@inheritDoc}
     */
    @Override
    public ContactRules createContactRules(final ReadablePhysicalWorld world) throws IllegalStateException {
        this.checkStateIllegality(this.contactRulesCreated, CONT_INIT_ERR);
        this.contactRulesCreated = true;
        return new ContactRules(world);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CollisionRules createCollisionRules(final ReadablePhysicalWorld physicalWorld, final NotifiableWorld outerWorld)
        throws IllegalStateException {
        this.checkStateIllegality(this.collisionRulesCreated, COLL_INIT_ERR);
        this.collisionRulesCreated = true;
        return new CollisionRules(physicalWorld, outerWorld);
    }

    private void checkStateIllegality(final boolean illegalCondition, final String message) {
        if (illegalCondition) {
            throw new IllegalStateException(message);
        }
    }
}
