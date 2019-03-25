package model.entities;

/**
 * The types of movement the player can do.
 */
public enum MovementType {
    /**
     * Player climbs a ladder.
     */
    CLIMB,
    /**
     * Player jumps.
     */
    JUMP,
    /**
     * Player walks to the right.
     */
    MOVE_RIGHT,
    /**
     * Player walks to the left.
     */
    MOVE_LEFT;

    private State conversion;

    static {
        CLIMB.conversion = State.CLIMBING;
        JUMP.conversion = State.JUMPING;
        MOVE_RIGHT.conversion = State.MOVING_RIGHT;
        MOVE_LEFT.conversion = State.MOVING_LEFT;
    }
    /**
     * Converts the {@link MovementType} on which it's called to a {@link State}.
     * @return the corresponding {@link State}.
     */
    public State convert() {
        return conversion;
    }

}
