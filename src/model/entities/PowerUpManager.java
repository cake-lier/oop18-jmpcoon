package model.entities;

import java.util.Collection;
import java.util.LinkedList;

import org.dyn4j.dynamics.contact.ContactConstraint;

/**
 * This class manages the relations between {@link Player} and all {@link PowerUp}s
 * present in the {@link World}.
 */
public class PowerUpManager {
    private final Player player;
    private final Collection<PowerUp> worldPowerUps;
    private static final double STARVELOCITY_X = 1.60;
    private static final double STARVELOCITY_Y = 1.50;
    private static final int STAR_DURATION = 400;
    private static final int HIT_COOLDOWN = 60;

    private boolean goal = false;
    private boolean superStar = false;
    private int stepCounterStar = 0;
    private int stepCounterHit = -1;

    /**
     * @param player the current {@link Player}.
     * @param worldPowerups all {@link PowerUp}s.
     */
    public PowerUpManager(final Player player, final Collection<PowerUp> worldPowerups) {
        this.player = player;
        this.worldPowerUps = new LinkedList<>(worldPowerups);
    }

    /**
     * Checks whether a {@link PowerUp} has been picked up by {@link Player};
     * if it has, it gets processed by this {@link PowerUpManager}.
     */
    public void checkPowerUps() {
        this.worldPowerUps.stream()
                           .filter(powerup -> !powerup.isAlive())
                           .forEach(powerup -> this.processPowerup(powerup.getPowerUpType()));
        this.worldPowerUps.removeIf(powerup -> !powerup.isAlive());
        if (this.superStar) {
            superStarCooldown();
        }
    }

    /**
     * @return true if {@link Player} has one or more lives.
     */
    public boolean isPlayerAlive() {
        return this.player.getLives() != 0;
    }

    /**
     * {@link Player} is hit by and enemy, loses one life.
     * @param contactConstraint The contact to be disabled during hit cool-down
     */
    public void playerHit(final ContactConstraint contactConstraint) {
        this.hitCooldown(contactConstraint);
        if (this.stepCounterHit == 0) {
            this.player.removeLife();
        }
    }

    /**
     * @return true if {@link Player} is invincible.
     */
    public boolean isPlayerInvincible() {
        return this.superStar;
    }

    /**
     * @return true if the GOAL {@link PowerUp} has been picked up.
     */
    public boolean isGoalReached() {
        return this.goal;
    }

    private void processPowerup(final PowerUpType powerUp) {
        if (powerUp == PowerUpType.GOAL) {
            this.goal = true;
        } else if (powerUp == PowerUpType.EXTRA_LIFE) {
            this.player.addLife();
        } else if (powerUp == PowerUpType.SUPER_STAR) {
            this.superStar = true;
            this.player.modifyMaxVelocity(STARVELOCITY_X, STARVELOCITY_Y);
        }
    }

    private void superStarCooldown() {
        this.stepCounterStar++;
        if (this.stepCounterStar == STAR_DURATION) {
            this.stepCounterStar = 0;
            this.superStar = false;
            this.player.modifyMaxVelocity(1 / STARVELOCITY_X, 1 / STARVELOCITY_Y);
        }
    }

    private void hitCooldown(final ContactConstraint contactConstraint) {
        this.stepCounterHit++;
        contactConstraint.setEnabled(false);
        if (this.stepCounterHit == HIT_COOLDOWN) {
            this.stepCounterHit = -1;
        }
    }

}