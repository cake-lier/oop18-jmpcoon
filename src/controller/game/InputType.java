package controller.game;

import model.entities.MovementType;

/**
 * An enumeration representing the types of input a {@link GameController} can process.
 */
public enum InputType {
    /**
     * An {@link InputType} that indicates a {@link MovementType#CLIMB_DOWN} should be transmitted to the game.
     */
    CLIMB_DOWN(MovementType.CLIMB_DOWN),
    /**
     * An {@link InputType} that indicates a {@link MovementType#CLIMB_UP} should be transmitted to the game.
     */
    CLIMB_UP(MovementType.CLIMB_UP),
    /**
     * An {@link InputType} that indicates a {@link MovementType#MOVE_LEFT} should be transmitted to the game.
     */
    LEFT(MovementType.MOVE_LEFT),
    /**
     * An {@link InputType} that indicates a {@link MovementType#MOVE_RIGHT} should be transmitted to the game.
     */
    RIGHT(MovementType.MOVE_RIGHT),
    /**
     * An {@link InputType} that indicates a {@link MovementType#JUMP} should be transmitted to the game.
     */
    UP(MovementType.JUMP);

    private final MovementType associatedMovementType;

    InputType(final MovementType associatedMovementType) {
        this.associatedMovementType = associatedMovementType;
    }

    /**
     * Getter for the {@link MovementType} associated to this {@link InputType}.
     * @return the {@link MovementType} associated to this {@link InputType}
     */
    public MovementType getAssociatedMovementType() {
        return this.associatedMovementType;
    }
}
