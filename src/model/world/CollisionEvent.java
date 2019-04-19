package model.world;

/**
 * This enum represents all types of possible outcomes that a collision could have. The collision can be neutral, such as the
 * {@link model.entities.Player} hitting a {@link model.entities.Platform}, can kill the enemy when the
 * {@link model.entities.Player} jumps on it or can kill the {@link model.entities.Player} if instead is the enemy that jumps or
 * touches the {@link model.entities.Player}.
 */
public enum CollisionEvent {
    /**
     * The collision in which is the {@link model.entities.Player} that gets killed. This can happen when a
     * {@link model.entities.WalkingEnemy} or a {@link model.entities.RollingEnemy} touches the {@link model.entities.Player} 
     * from any angle except from below.
     */
    PLAYER_KILLED,
    /**
     * The collision in which is a {@link model.entities.WalkingEnemy} that gets killed from the {@link model.entities.Player}.
     * This can happen when the player while jumping lands on a {@link model.entities.WalkingEnemy}.
     */
    WALKING_ENEMY_KILLED,
    /**
     * The collision in which is a {@link model.entities.RollingEnemy} that gets killed from the {@link model.entities.Player}.
     * This can happen when the player while jumping lands on a {@link model.entities.RollingEnemy}.
     */
    ROLLING_ENEMY_KILLED,
    /**
     * The collision in which the {@link model.entities.Player} hits the goal of this game and the game should end.
     */
    GOAL_HIT,
    /**
     * The collision in which the {@link model.entities.Player} hits some {@link model.entities.PowerUp}.
     */
    POWER_UP_HIT,
    /**
     * The collision in which the {@link model.entities.Player} hits the {@link model.entities.PowerUp} that grants the
     * possibility to be invincible.
     */
    INVINCIBILITY_HIT;
}
