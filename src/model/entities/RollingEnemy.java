package model.entities;

import model.physics.DynamicPhysicalBody;

/**
 * a walking enemy inside the {@link model.world.World} of the game.
 */
public final class RollingEnemy extends DynamicEntity {

    private static final long serialVersionUID = -6624661835399417683L;
    private static final double ROLLING_ENEMY_SPEED = 0.7;

    private final DynamicPhysicalBody body;
    private boolean direction = false;
    private boolean onAir = true;

    /**
     * Creates a new {@link RollingEnemy} with the given {@link DynamicPhysicalBody}. This constructor is package protected because it
     * should be only invoked by the {@link EntityBuilder} when creating a new instance of it and no one else.
     * @param body The {@link DynamicPhysicalBody} that should be contained in this {@link RollingEnemy}.
     */
    RollingEnemy(final DynamicPhysicalBody body) {
        super(body);
        this.body = body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        return EntityType.ROLLING_ENEMY;
    }

    /**
     * applies a fixed velocity to the {@link RollingEnemy}.
     */
    public void computeMovement() {
        if (!this.onAir) {
            this.body.setFixedVelocity(this.getMovement(), this.getDirection() * ROLLING_ENEMY_SPEED, 0);
        }
    }

    /**
     * @param isOnAir set {@link RollingEnemy} state: true if it's on air, false otherwise
     */
    public void setOnAir(final boolean isOnAir) {
        if (this.onAir != isOnAir) {
            this.onAir = isOnAir;
            if (!this.onAir) {
                this.direction = !this.direction;
            }
        }
    }

    private MovementType getMovement() {
        return this.direction ? MovementType.MOVE_RIGHT : MovementType.MOVE_LEFT;
    }

    private int getDirection() {
        return this.direction ? 1 : -1;
    }
}
