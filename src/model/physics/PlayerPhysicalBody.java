package model.physics;

import model.entities.PowerUpType;
import model.serializable.SerializableBody;

/**
 * 
 */
public class PlayerPhysicalBody extends DynamicPhysicalBody {

    private static final long serialVersionUID = -6099710781272943170L;

    private static final double STARVELOCITY_X = 1.60;
    private static final double STARVELOCITY_Y = 1.50;


    private boolean superStar = false;
    private boolean invulnerable = false;
    private int lives = 1;

    private final SerializableBody body;

    /**
     * Builds a new {@link PlayerPhysicalBody}.
     * @param body The {@link SerializableBody} encapsulated by this {@link DynamicPhysicalBody}.
     */
    public PlayerPhysicalBody(final SerializableBody body) {
        super(body);
        this.body = body;
    }

    /**
     * @return true if {@link Player} is immune to hits.
     */
    public boolean isInvulnerable() {
        return this.invulnerable;
    }
   /**
     * When {@link PlayerPhysicalBody} is invincible, enemies that collide with it die.
     * @return true if {@link Player} is invincible.
     */
    public boolean isInvincible() {
        return this.superStar;
    }

    /**
     * Gives the specified {@link PowerUp} to the {@link PlayerPhysicalBody}.
     * @param powerUpType The {@link PowerUpType} of the power-up.
     */
    public void givePowerUp(final PowerUpType powerUpType) {
        if (powerUpType == PowerUpType.EXTRA_LIFE) {
            this.lives++;
        } else if (powerUpType == PowerUpType.SUPER_STAR) {
            this.superStar = true;
            this.invulnerable = true;
            this.modifyMaxVelocity(STARVELOCITY_X, STARVELOCITY_Y);
        }
    }

    /**
     * @return the number of lives of this {@link PlayerPhysicalBody}.
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * Modifies the maximum velocity of this {@link PlayerPhysicalBody}.
     * @param multiplierX The multiplier for the horizontal maximum velocity
     * @param multiplierY The multiplier for the vertical maximum velocity
     */
    public void modifyMaxVelocity(final double multiplierX, final double multiplierY) {
        this.setMaxVelocity(multiplierX, multiplierY);
    }

    /**
     * {@link Player} is hit by and enemy, loses one life.
     * If Player has 0 lives, he dies.
     */
    public void hit() {
        if (!this.invulnerable) {
            this.lives--;
            this.invulnerable = true;
        }
        if (this.lives == 0) {
            this.body.setActive(false);
        }
    }

    /**
     * The effect of the Super Star {@link PowerUp} ends.
     */
    public void endSuperStar() {
        this.superStar = false;
        this.invulnerable = false;
        this.modifyMaxVelocity(1 / STARVELOCITY_X, 1 / STARVELOCITY_Y);
    }

    /**
     * Ends {@link Player} immunity, which is set after player takes one hit
     * for a short period of time.
     */
    public void endInvulnerability() {
        this.invulnerable = false;
    }
}