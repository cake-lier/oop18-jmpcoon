package model.entities;

/**
 * An enum associating a {@link MovementType} with the appropriate impulse values 
 * used to move a {@link DynamicPhysicalBody}.Its scope is package protected because it should
 * be only used by the {@link Entity} associated with the {@link DynamicPhysicalBody} to be moved.
 */
enum MovementValues {
    CLIMB_DOWN(MovementType.CLIMB_DOWN, 0, -0.5),
    CLIMB_UP(MovementType.CLIMB_UP, 0, 0.5),
    JUMP(MovementType.JUMP, 0, 0.13),
    MOVE_RIGHT(MovementType.MOVE_RIGHT, 0.5, 0),
    MOVE_LEFT(MovementType.MOVE_LEFT, -0.5, 0);

    private final MovementType movementType;
    private final double impulseX;
    private final double impulseY;

    MovementValues(final MovementType movementType, final double impulseX, final double impulseY) {
        this.movementType = movementType;
        this.impulseX = impulseX;
        this.impulseY = impulseY;
    }

    public MovementType getMovementType() {
        return this.movementType;
    }

    public double getImpulseX() {
        return this.impulseX;
    }

    public double getImpulseY() {
        return this.impulseY;
    }
}
