package model.entities;

import java.util.Arrays;

import model.physics.DynamicPhysicalBody;
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
        final MovementValues moveValues = Arrays.asList(MovementValues.values())
                                                .stream()
                                                .filter(mValue -> mValue.getMovementType() == movement)
                                                .findAny().get();
        this.body.applyMovement(moveValues.getMovementType(), moveValues.getImpulseX(), moveValues.getImpulseY());
    }

    /**
     * @return the number of lives of this {@link Player}.
     */
    public int getLives() {
        return this.body.getLives();
    }

}
