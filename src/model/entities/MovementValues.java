package model.entities;

/**
 * An enumeration associating a {@link MovementType} with the appropriate impulse values 
 * used to move a {@link DynamicPhysicalBody}.Its scope is package protected because it should
 * be only used by the {@link Entity} associated with the {@link DynamicPhysicalBody} to be moved.
 */
enum MovementValues {
    CLIMB_DOWN(MovementType.CLIMB_DOWN, 0, -0.5),
    CLIMB_UP(MovementType.CLIMB_UP, 0, 0.5),
    JUMP(MovementType.JUMP, 0, 0.26),
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

    /**
     * Returns the {@link MovementType} associated to this value.
     * @return the {@link MovementType} associated
     */
    public MovementType getMovementType() {
        return this.movementType;
    }

    /**
     * Returns the value the impulse should have on the x axis.
     * @return the x value of the impulse
     */
    public double getImpulseX() {
        return this.impulseX;
    }

    /**
     * Returns the value the impulse should have on the y axis.
     * @return the y value of the impulse
     */
    public double getImpulseY() {
        return this.impulseY;
    }
}
