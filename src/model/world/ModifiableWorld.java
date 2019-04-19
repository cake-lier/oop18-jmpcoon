package model.world;

import java.io.Serializable;

import model.entities.RollingEnemy;

/**
 * An interface for letting {@link model.entities.Entity}s to add more {@link model.entities.Entity}s that have been
 * generated to it so as to let it know that they exists and need to be considered.
 */
public interface ModifiableWorld extends Serializable {
    /**
     * Adds a {@link RollingEnemy} that has been generated outside the {@link World} to the {@link World} itself.
     * @param generatedEnemy the {@link RollingEnemy} that has been generated
     */
    void addGeneratedRollingEnemy(RollingEnemy generatedEnemy);
}
