package controller.game;

import model.entities.MovementType;

/**
 * an enumeration representing the types of input a {@link GameController} can process.
 */
public enum InputType {

    // TODO: comment the types
    CLIMB_DOWN(MovementType.CLIMB_DOWN),
    CLIMB_UP(MovementType.CLIMB_UP),
    LEFT(MovementType.MOVE_LEFT),
    RIGHT(MovementType.MOVE_RIGHT),
    UP(MovementType.JUMP);

    private final MovementType associatedMovementType;

    /**
     * 
     * @param associatedMovementType hk
     */
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
