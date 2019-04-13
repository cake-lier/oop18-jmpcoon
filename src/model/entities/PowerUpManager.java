package model.entities;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This class manages the relations between {@link Player} and all {@link PowerUp}s
 * present in the {@link World}.
 */
public class PowerUpManager {
    private final Player player;
    private final Collection<PowerUp> worldPowerups;
    private static final double STARVELOCITY_X = 1.60;
    private static final double STARVELOCITY_Y = 1.50;
    private static final int STAR_DURATION = 400;

    private boolean goal = false;
    private boolean superStar = false;
    private int counter = 0;

    /**
     * @param player the current {@link Player}.
     * @param worldPowerups all {@link PowerUp}s.
     */
    public PowerUpManager(final Player player, final Collection<PowerUp> worldPowerups) {
        this.player = player;
        this.worldPowerups = new LinkedList<>(worldPowerups);
    }

    /**
     * Checks whether a {@link PowerUp} has been picked up by {@link Player};
     * if it has, it gets processed by this {@link PowerUpManager}.
     */
    public void checkPowerUps() {
        this.worldPowerups.forEach(powerup -> {
            if (!powerup.isAlive()) {
                this.processPowerup(powerup.getPowerUpType());
                this.worldPowerups.remove(powerup);
            }
        });
        if (this.superStar) {
            counter();
        }
    }

    /**
     * @return true if {@link Player} has one or more lives.
     */
    public boolean isPlayerAlive() {
        return this.player.getLives() != 0;
    }

    /**
     * @return true if the GOAL {@link PowerUp} has been picked up.
     */
    public boolean isGoalReached() {
        return this.goal;
    }

    private void processPowerup(final PowerUpType powerup) {
        if (powerup == PowerUpType.GOAL) {
            this.goal = true;
        } else if (powerup == PowerUpType.EXTRA_LIFE) {
            this.player.addLife();
        } else if (powerup == PowerUpType.SUPER_STAR) {
            this.superStar = true;
            this.player.modifyMaxVelocity(STARVELOCITY_X, STARVELOCITY_Y);
            this.player.setInvincible(true);
        }
    }

    private void counter() {
        this.counter++;
        if (this.counter == STAR_DURATION) {
            this.superStar = false;
            this.player.modifyMaxVelocity(1 / STARVELOCITY_X, 1 / STARVELOCITY_Y);
            this.player.setInvincible(false);
        }
    }

}
