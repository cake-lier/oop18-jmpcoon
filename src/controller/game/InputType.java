package controller.game;

import model.entities.MovementType;

/**
 * an enumeration representing the types of input a {@link GameController} can process.
 */
public enum InputType {

    // TODO: comment the types
    CLIMB_DOWN(MovementType.CLIMB_DOWN),
    CLIMB_UP(MovementType.CLIMB_DOWN),
    LEFT(MovementType.CLIMB_DOWN),
    RIGHT(MovementType.CLIMB_DOWN),
    UP(MovementType.CLIMB_DOWN);

    private final MovementType associatedMovementType;

    private InputType(final MovementType associatedMovementType) {
        this.associatedMovementType = associatedMovementType;
    }

    /**
     * @return the {@link MovementType} associated to this {@link InputType}
     */
    public MovementType getAssociatedMovementType() {
        return this.associatedMovementType;
    }

}
