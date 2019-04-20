package model.physics;

import model.entities.PowerUpType;
import model.serializable.SerializableBody;

/**
 * A class representing a {@link PlayerPhysicalBody} that manages {@link Player} lives
 * and {@link PowerUp} effects on the player.
 */
public class PlayerPhysicalBody extends DynamicPhysicalBody {
    private static final long serialVersionUID = -6099710781272943170L;
    private static final double INVINCIBILITY_VELOCITY_X = 1.60;
    private static final double INVINCIBILITY_VELOCITY_Y = 1.50;

    private final SerializableBody body;
    private boolean invincible;
    private boolean invulnerable;
    private int lives;

    /**
     * Builds a new {@link PlayerPhysicalBody}. This constructor is package protected because it should be only invoked 
     * by the {@link PhysicalFactoryImpl} when creating a new instance of it and no one else.
     * @param body the {@link SerializableBody} encapsulated by this {@link PlayerPhysicalBody}
     */
    PlayerPhysicalBody(final SerializableBody body) {
        super(body);
        this.body = body;
        this.invincible = false;
        this.invulnerable = false;
        this.lives = 1;
    }

    /**
     * Returns true if {@link PlayerPhysicalBody} is immune to hits.
     * @return true if player is immune to hits from enemies
     */
    public boolean isInvulnerable() {
        return this.invulnerable;
    }

    /**
     * Returns true if {@link PlayerPhysicalBody} is invincible.
     * When the player is invincible, enemies that collide with it die.
     * @return true if the player is invincible.
     */
    public boolean isInvincible() {
        return this.invincible;
    }

    /**
     * Gives the specified {@link PowerUp} to the {@link PlayerPhysicalBody}.
     * @param powerUpType the {@link PowerUpType} of the power-up
     */
    public void givePowerUp(final PowerUpType powerUpType) {
        if (powerUpType == PowerUpType.EXTRA_LIFE) {
            this.lives++;
        } else if (powerUpType == PowerUpType.INVINCIBILITY) {
            this.invincible = true;
            this.modifyMaxVelocity(INVINCIBILITY_VELOCITY_X, INVINCIBILITY_VELOCITY_Y);
        }
    }

    /**
     * Returns the number of lives of this {@link PlayerPhysicalBody}.
     * @return the number of lives of the player
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * Modifies the maximum velocity of this {@link PlayerPhysicalBody}.
     * @param multiplierX the multiplier for the horizontal maximum velocity
     * @param multiplierY the multiplier for the vertical maximum velocity
     */
    public void modifyMaxVelocity(final double multiplierX, final double multiplierY) {
        this.setMaxVelocity(multiplierX, multiplierY);
    }

    /**
     * Registers a hit from an enemy on this {@link PlayerPhysicalBody} if it is not invulnerable to hits.
     * If the lives count reaches zero, the {@link PlayerPhysicalBody} dies.
     */
    public void hit() {
        if (!this.invulnerable) {
            this.lives--;
            this.invulnerable = true;
        }
        if (this.lives == 0) {
            this.kill();
        }
    }

    /**
     * Kills the {@link PlayerPhysicalBody}.
     */
    public void kill() {
        this.lives = 0;
        this.body.setActive(false);
    }

    /**
     * The effect of the Super Star {@link PowerUp} ends.
     */
    public void endInvincibility() {
        this.invincible = false;
        this.modifyMaxVelocity(1, 1);
    }

    /**
     * Ends {@link PlayerPhysicalBody} immunity, which is set after player takes one hit
     * and lasts for a short period of time.
     */
    public void endInvulnerability() {
        this.invulnerable = false;
    }
}
