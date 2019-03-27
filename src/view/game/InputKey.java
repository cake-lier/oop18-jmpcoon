package view.game;

import controller.game.InputType;

/**
 * The keyboard keys that control the {@link Player}.
 */
public enum InputKey {
    /**
     * Player jumps.
     */
    W, 
    /**
     * Player goes left.
     */
    A, 
    /**
     * Player goes right.
     */
    D, 
    /**
     * Player climbs up a {@link Ladder}.
     */
    E,
    /**
     * Player climbs down a {@link Ladder}.
     */
    S;

    private InputType conversion;

    static {
        W.conversion = InputType.UP;
        A.conversion = InputType.LEFT;
        D.conversion = InputType.RIGHT;
        E.conversion = InputType.CLIMB_UP;
        S.conversion = InputType.CLIMB_DOWN;
    }

    /**
     * Converts the {@link InputKey} on which it's called to a {@link InputType}.
     * @return the corresponding {@link InputType}.
     */
    public InputType convert() {
        return conversion;
    }
}
