package model.entities;

import model.physics.PhysicsFactory;

/**
 * 
 */
public final class EntityFactory {
    private final PhysicsFactory physicsFactory;
    /**
     * 
     * @param physicsFactory a
     */
    public EntityFactory(final PhysicsFactory physicsFactory) {
        this.physicsFactory = physicsFactory;
    }
    /**
     * 
     * @return a
     */
    public Player createPlayer() {
        this.physicsFactory.getClass();
        return null;
    }
    /**
     * 
     * @return a
     */
    public Platform createPlatform() {
        return null;
    }
    /**
     * 
     * @return a
     */
    public Ladder createLadder() {
        return null;
    }
    /**
     * 
     * @return a
     */
    public WalkingEnemy createWalkingEnemy() {
        return null;
    }
    /**
     * 
     * @return a
     */
    public RollingEnemy createRollingEnemy() {
        return null;
    }
    /**
     * 
     * @return a
     */
    public PowerUp createPowerUp() {
        return null;
    }
}
