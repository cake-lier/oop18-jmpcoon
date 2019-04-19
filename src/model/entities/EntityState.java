package model.entities;

/**
 * An enumeration representing the possible states an {@link Entity} can be in.
 */
public enum EntityState {
    /**
     * It's the state of an {@link Entity} while climbing down a {@link Ladder}. It's used only by the {@link Player}.
     */
    CLIMBING_DOWN,
    /**
     * It's the state of an {@link Entity} while climbing up a {@link Ladder}. It's used only by the {@link Player}.
     */
    CLIMBING_UP,
    /**
     * It's the state of an {@link Entity} while jumping. It's used only by the {@link Player}.
     */
    JUMPING,
    /**
     * It's the state of an {@link Entity} while moving to the left. Every moving {@link Entity} can enter it.
     */
    MOVING_LEFT,
    /**
     * It's the state of an {@link Entity} while moving to the right. Every moving {@link Entity} can enter it.
     */
    MOVING_RIGHT,
    /**
     * It's the state of an {@link Entity} while standing still. Every {@link Entity} can enter it.
     */
    IDLE;
}
