package model.entities;

import model.physics.DynamicPhysicalBody;
import model.physics.PlayerPhysicalBody;

/**
 * The player inside the {@link model.world.World} of the game.
 */
public final class Player extends DynamicEntity {

    private static final long serialVersionUID = 7632362148460378676L;
    private static final double WALKIMPULSE = 1;
    private static final double CLIMBIMPULSE = 0.5;
    private static final double JUMPIMPULSE = 0.25;

    private final PlayerPhysicalBody body;

    /**
     * Creates a new {@link Player} with the given {@link DynamicPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link EntityBuilder} when creating a new instance of it and no one else.
     * @param body The {@link DynamicPhysicalBody} that should be contained in this {@link Player}.
     */
    Player(final DynamicPhysicalBody body) {
        super(body);
        this.body = PlayerPhysicalBody.class.cast(body);
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

    /**
     * @return the number of lives of this {@link Player}.
     */
    public int getLives() {
        return this.body.getLives();
    }

}
