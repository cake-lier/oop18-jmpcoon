package model.entities;

import model.physics.DynamicPhysicalBody;

/**
 * The player inside the {@link World} of the game.
 */
public class Player extends DynamicEntity {
    private static final double WALKIMPULSE = 1;
    private static final double CLIMBIMPULSE = 0.5;
    private static final double JUMPIMPULSE = 0.25;

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
            case JUMP: 
                this.body.applyMovement(MovementType.JUMP, 0, JUMPIMPULSE);
                break;
            case CLIMB_UP:
                this.body.applyMovement(MovementType.CLIMB_UP, 0, CLIMBIMPULSE);
                break;
            case CLIMB_DOWN:
                this.body.applyMovement(MovementType.CLIMB_DOWN, 0, -CLIMBIMPULSE);
                break;
            case MOVE_RIGHT:
                this.body.applyMovement(MovementType.MOVE_RIGHT, WALKIMPULSE,  0);
                break;
            case MOVE_LEFT:
                this.body.applyMovement(MovementType.MOVE_LEFT, -WALKIMPULSE,  0);
                break;
            default: 
        }
    }
}
