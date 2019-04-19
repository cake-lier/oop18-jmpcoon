package model.entities;

import java.util.Arrays;

import model.physics.PlayerPhysicalBody;

/**
 * The player inside the {@link model.world.World} of the game.
 */
public final class Player extends DynamicEntity {
    private static final long serialVersionUID = 7632362148460378676L;

    private final PlayerPhysicalBody body;

    /**
     * Creates a new {@link Player} with the given {@link DynamicPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link AbstractEntityBuilder} when creating a new instance of it and no one else.
     * @param body the {@link DynamicPhysicalBody} that should be contained in this {@link Player}
     */
    Player(final PlayerPhysicalBody body) {
        super(body);
        this.body = body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    /**
     * Moves the Player.
     * @param movement the {@link MovementType} to apply to this {@link Player}
     */
    public void move(final MovementType movement) {
        final MovementValues moveValues = Arrays.asList(MovementValues.values())
                                                .stream()
                                                .filter(mValue -> mValue.getMovementType() == movement)
                                                .findAny().get();
        this.body.applyMovement(moveValues.getMovementType(), moveValues.getImpulseX(), moveValues.getImpulseY());
    }

    /**
     * Returns the number of lives of this Player.
     * @return the number of lives of this {@link Player}
     */
    public int getLives() {
        return this.body.getLives();
    }
}
