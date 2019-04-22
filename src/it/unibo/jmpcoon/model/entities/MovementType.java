package it.unibo.jmpcoon.model.entities;

/**
 * The types of movement the player can do.
 */
public enum MovementType {
    /**
     * Player climbs down a ladder.
     */
    CLIMB_DOWN(EntityState.CLIMBING_DOWN),
    /**
     * Player climbs up a ladder.
     */
    CLIMB_UP(EntityState.CLIMBING_UP),
    /**
     * Player jumps.
     */
    JUMP(EntityState.JUMPING),
    /**
     * Player walks to the right.
     */
    MOVE_RIGHT(EntityState.MOVING_RIGHT),
    /**
     * Player walks to the left.
     */
    MOVE_LEFT(EntityState.MOVING_LEFT);

    private EntityState correspondingState;

    MovementType(final EntityState state) {
        this.correspondingState = state;
    }

    /**
     * Converts the {@link MovementType} on which it's called to a {@link EntityState}.
     * @return the corresponding {@link EntityState}.
     */
    public EntityState convert() {
        return this.correspondingState;
    }
}
