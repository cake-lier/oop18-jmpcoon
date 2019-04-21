package controller.game;

import com.google.common.base.Optional;

import model.world.CollisionEvent;
/**
 * An enumeration representing the events that can happen in the game.
 */
public enum GameEvent {
    /**
     * The event in which the {@link model.entities.Player} gets killed.
     */
    PLAYER_KILLED(Optional.of(CollisionEvent.PLAYER_KILLED)),
    /**
     * The event in which a {@link model.entities.WalkingEnemy} gets killed from the {@link model.entities.Player}.
     */
    WALKING_ENEMY_KILLED(Optional.of(CollisionEvent.WALKING_ENEMY_KILLED)),
    /**
     * The event in which a {@link model.entities.RollingEnemy} gets killed from the {@link model.entities.Player}.
     */
    ROLLING_ENEMY_KILLED(Optional.of(CollisionEvent.ROLLING_ENEMY_KILLED)),
    /**
     * The event in which the {@link model.entities.Player} hits the goal of this game and the game should end.
     */
    GOAL_HIT(Optional.of(CollisionEvent.GOAL_HIT)),
    /**
     * The event in which the {@link model.entities.Player} hits some {@link model.entities.PowerUp}.
     */
    POWER_UP_HIT(Optional.of(CollisionEvent.POWER_UP_HIT)),
    /**
     * The event in which the {@link model.entities.Player} hits the {@link model.entities.PowerUp} that grants the
     * possibility to be invincible.
     */
    INVINCIBILITY_HIT(Optional.of(CollisionEvent.INVINCIBILITY_HIT)),
    /**
     * A jump executed by the {@link Player}.
     */
    JUMP(Optional.absent());

    private final Optional<CollisionEvent> associatedCollisionEvent;

    GameEvent(final Optional<CollisionEvent> associatedCollisionEvent) {
        this.associatedCollisionEvent = associatedCollisionEvent;
    }

    /**
     * Returns the {@link CollisionEvent} associated to this {@link GameEvent}.
     * @return the {@link CollisionEvent} associated to this {@link GameEvent}
     */
    public Optional<CollisionEvent> getAssociatedCollisionEvent() {
        return this.associatedCollisionEvent;
    }
}
