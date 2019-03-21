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
     * @param type a.
     * @param shape a.
     * @param xCoord a.
     * @param yCoord a.
     * @param width a.
     * @param height a.
     * @param angle a.
     * @return a.
     */
    public Player createPlayer(final EntityType type, final EntityShape shape, final double xCoord, final double yCoord,
            final double width, final double height, final double angle) {
        this.physicsFactory.getClass();
        return null;
    }
    /**
     * 
     * @param type q.
     * @param shape a.
     * @param xCoord a.
     * @param yCoord a.
     * @param width a.
     * @param height a.
     * @param angle a.
     * @return a.
     */
    public Platform createPlatform(final EntityType type, final EntityShape shape, final double xCoord, final double yCoord,
            final double width, final double height, final double angle) {
        return null;
    }
    /**
     * 
     * @param type a.
     * @param shape a.
     * @param xCoord a.
     * @param yCoord a.
     * @param width a.
     * @param height a.
     * @param angle a.
     * @return a.
     */
    public Ladder createLadder(final EntityType type, final EntityShape shape, final double xCoord, final double yCoord,
            final double width, final double height, final double angle) {
        return null;
    }
    /**
     * 
     * @param type a.
     * @param shape a.
     * @param xCoord a.
     * @param yCoord a.
     * @param width a.
     * @param height a.
     * @param angle a.
     * @return a.
     */
    public WalkingEnemy createWalkingEnemy(final EntityType type, final EntityShape shape, final double xCoord, final double yCoord,
            final double width, final double height, final double angle) {
        return null;
    }
    /**
     * 
     * @param type a.
     * @param shape a.
     * @param xCoord a.
     * @param yCoord a.
     * @param width a.
     * @param height a.
     * @param angle a.
     * @return a.
     */
    public RollingEnemy createRollingEnemy(final EntityType type, final EntityShape shape, final double xCoord, final double yCoord,
            final double width, final double height, final double angle) {
        return null;
    }
    /**
     * 
     * @param type a.
     * @param shape a.
     * @param xCoord a.
     * @param yCoord a.
     * @param width a.
     * @param height a.
     * @param angle a.
     * @return a.
     */
    public PowerUp createPowerUp(final EntityType type, final EntityShape shape, final double xCoord, final double yCoord,
            final double width, final double height, final double angle) {
        return null;
    }
    /**
     * 
     * @param type a.
     * @param shape a.
     * @param xCoord a.
     * @param yCoord a.
     * @param width a.
     * @param height a.
     * @param angle a.
     * @return a.
     */
    public GeneratorEnemy createGeneratorEnemy(final EntityType type, final EntityShape shape, final double xCoord, final double yCoord,
            final double width, final double height, final double angle) {
        return null;
    }
}
