package controller.game;

import model.entities.MovementType;

/**
 * an enumeration representing the types of input a {@link GameController} can process.
 */
public enum InputType {

    /**
     * an {@link InputType} that indicates a {@link MovementType#CLIMB_DOWN} should be transmitted to the game.
     */
    CLIMB_DOWN(MovementType.CLIMB_DOWN),
    /**
     * an {@link InputType} that indicates a {@link MovementType#CLIMB_UP} should be transmitted to the game.
     */
    CLIMB_UP(MovementType.CLIMB_UP),
    /**
     * an {@link InputType} that indicates a {@link MovementType#MOVE_LEFT} should be transmitted to the game.
     */
    LEFT(MovementType.MOVE_LEFT),
    /**
     * an {@link InputType} that indicates a {@link MovementType#MOVE_RIGHT} should be transmitted to the game.
     */
    RIGHT(MovementType.MOVE_RIGHT),
    /**
     * an {@link InputType} that indicates a {@link MovementType#JUMP} should be transmitted to the game.
     */
    UP(MovementType.JUMP);

    private final MovementType associatedMovementType;

     InputType(final MovementType associatedMovementType) {
        this.associatedMovementType = associatedMovementType;
    }

    /**
     * @return the {@link MovementType} associated to this {@link InputType}
     */
    public MovementType getAssociatedMovementType() {
        return this.associatedMovementType;
    }

}
