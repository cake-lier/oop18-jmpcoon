package model.entities;

/**
 * The types of movement the player can do.
 */
public enum MovementType {
    /**
     * Player climbs down a ladder.
     */
    CLIMB_DOWN(State.CLIMBING_DOWN),
    /**
     * Player climbs up a ladder.
     */
    CLIMB_UP(State.CLIMBING_UP),
    /**
     * Player jumps.
     */
    JUMP(State.JUMPING),
    /**
     * Player walks to the right.
     */
    MOVE_RIGHT(State.MOVING_RIGHT),
    /**
     * Player walks to the left.
     */
    MOVE_LEFT(State.MOVING_LEFT);

    private State correspondingState;

    MovementType(final State state) {
        this.correspondingState = state;
    }

    /**
     * Converts the {@link MovementType} on which it's called to a {@link State}.
     * @return the corresponding {@link State}.
     */
    public State convert() {
        return this.correspondingState;
    }

}
