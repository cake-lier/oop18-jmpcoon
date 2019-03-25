package model.entities;

import model.physics.DynamicPhysicalBody;

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
            case JUMP: this.body.applyMovement(MovementType.JUMP, 0, JUMPIMPULSE); break;
            case CLIMB: this.body.applyMovement(MovementType.CLIMB, 0, 0.5); break;
            case MOVE_RIGHT: this.body.applyMovement(MovementType.MOVE_RIGHT, WALKIMPULSE,  0); break;
            case MOVE_LEFT: this.body.applyMovement(MovementType.MOVE_LEFT, -WALKIMPULSE,  0); break;
            default: 
        }
    }
}
