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

    private boolean goal = false;

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
            System.out.println("Received powerup EXTRA_LIFE");
        }
    }

}
