package model.entities;

import java.util.Collection;
import java.util.LinkedList;

public class PowerUpManager {
    private final Player player;
    private final Collection<PowerUp> worldPowerups;

    private boolean goal = false;

    public PowerUpManager(final Player player, final Collection<PowerUp> worldPowerups) {
        this.player = player;
        this.worldPowerups = new LinkedList<>(worldPowerups);
    }

    public void checkPowerUps() {
        this.worldPowerups.forEach(powerup -> {
            if (!powerup.isAlive()) {
                this.addPowerup(powerup.getPowerUpType());
                this.worldPowerups.remove(powerup);
            }
        });
    }

    public void checkPlayer() {
        if (!this.player.isAlive() /*&& !this.isPlayerDead()*/) {
            this.player.removeLife();
        }
    }

   /* public boolean isPlayerDead() {
        return this.player.getLives() == 0;
    }
*/
    public boolean isGoalReached() {
        return this.goal;
    }

    private void addPowerup(final PowerUpType powerup) {
        if (powerup == PowerUpType.GOAL) {
            this.goal = true;
        } else if (powerup == PowerUpType.EXTRA_LIFE) {
            this.player.addLife();
            System.out.println("Received powerup EXTRA_LIFE");
        }
    }

}
