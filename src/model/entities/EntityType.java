package model.entities;

/**
 * Collects all the possible types an {@link Entity} can be.
 */
public enum EntityType {

    /**
     * An {@link Entity} which tries to kill the {@link Player} by generating other enemies, specifically {@link RollingEnemy}.
     * It can't move by itself.
     */
    GENERATOR_ENEMY,
    /**
     * An {@link Entity} which can be used by the {@link Player} for moving upwards through the platforms that can't
     * be reached with the sole jump.
     */
    LADDER,
    /**
     * The {@link Entity} which the user can move and play with in this game. It can move, jump, climb ladders,
     * kill enemies while reaching for the goal.
     */
    PLAYER,
    /**
     * An {@link Entity} which supports all the moving entities by giving them a flat plane on which to walk.
     */
    PLATFORM,
    /**
     * An {@link Entity} which represents an help a player could be given by the game making the path to victory a
     * little easier.
     */
    POWERUP,
    /**
     * An {@link Entity} which tries to kill the {@link Player} by rolling over the platforms under the effect of
     * gravity. It's generated by a generator enemy.
     */
    ROLLING_ENEMY,
    /**
     * An {@link Entity} which tries to kill the {@link Player} by walking around and make contact.
     */
    WALKING_ENEMY;
}
