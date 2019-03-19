package model;

/**
 * The player inside the {@link World} of the game.
 */
public class Player extends DynamicEntity {
    private static final double WALKIMPULSE = 0.05;
    private static final double JUMPIMPULSE = 0.175;

    private final DynamicPhysicalBody body;
    /**
     * Builds the new {@link Player}.
     * @param body The {@link PhysicalBody} of this {@link Player}
     */
    public Player(final DynamicPhysicalBody body) {
        super(body);
        this.body = body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityShape getShape() {
        return EntityShape.RECTANGLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    /**
     * Moves the player.
     * @param movement The movement to apply to player
     */
    public void move(final MovementType movement) {
        switch (movement) {
            case JUMP: jump(); break;
            case CLIMB: climb(); break;
            case MOVE_RIGHT: moveRight(); break;
            case MOVE_LEFT: moveLeft(); break;
            default: 
        }
    }

    private void jump() {
        this.body.applyMovement(0, JUMPIMPULSE);
    }

    private void climb() {
        this.body.applyMovement(0, 0.5);
    }

    private void moveRight() {
        this.body.applyMovement(WALKIMPULSE,  0);
    }

    private void moveLeft() {
        this.body.applyMovement(-WALKIMPULSE,  0);
    }

}
