package model.world;

import java.util.function.Supplier;

import model.entities.Entity;
import model.entities.EntityBuilder;
import model.entities.GeneratorEnemy;
import model.entities.Ladder;
import model.entities.Player;
import model.entities.Platform;
import model.entities.PowerUp;
import model.entities.RollingEnemy;
import model.entities.WalkingEnemy;

/**
 * An enum collecting all the possible creators of all possible {@link Entity}s. Its scope is package protected because it should
 * be used by the sole {@link World} which is the one who should create new {@link Entity}s.
 */
enum EntityCreator {
    /**
     * A {@link Ladder} creator.
     */
    LADDER(Ladder.class, EntityBuilder::getLadderBuilder),
    /**
     * A {@link Player} creator.
     */
    PLAYER(Player.class, EntityBuilder::getPlayerBuilder),
    /**
     * A {@link Platform} creator.
     */
    PLATFORM(Platform.class, EntityBuilder::getPlatformBuilder),
    /**
     * A {@link PowerUp} creator.
     */
    POWERUP(PowerUp.class, EntityBuilder::getPowerUpBuilder),
    /**
     * A {@link RollingEnemy} creator.
     */
    ROLLING_ENEMY(RollingEnemy.class, EntityBuilder::getRollingEnemyBuilder),
    /**
     * A {@link WalkingEnemy} creator.
     */
    WALKING_ENEMY(WalkingEnemy.class, EntityBuilder::getWalkingEnemyBuilder),
    /**
     * A {@link GeneratorEnemy} creator.
     */
    GENERATOR_ENEMY(GeneratorEnemy.class, EntityBuilder::getGeneratorEnemyBuilder);


    private final Supplier<EntityBuilder> supplier;
    private final Class<? extends Entity> associatedClass;

    EntityCreator(final Class<? extends Entity> associatedClass, final Supplier<EntityBuilder> supplier) {
        this.supplier = supplier;
        this.associatedClass = associatedClass;
    }

    public EntityBuilder getEntityBuilder() {
        return this.supplier.get();
    }

    public Class<? extends Entity> getAssociatedClass() {
        return this.associatedClass;
    }
}
