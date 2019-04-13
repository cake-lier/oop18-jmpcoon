package model.world;

import java.util.function.Supplier;

import model.entities.Entity;
import model.entities.EntityBuilder;
import model.entities.EntityBuilderUtils;
import model.entities.EnemyGenerator;
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
    LADDER(Ladder.class, EntityBuilderUtils::getLadderBuilder),
    /**
     * A {@link Player} creator.
     */
    PLAYER(Player.class, EntityBuilderUtils::getPlayerBuilder),
    /**
     * A {@link Platform} creator.
     */
    PLATFORM(Platform.class, EntityBuilderUtils::getPlatformBuilder),
    /**
     * A {@link PowerUp} creator.
     */
    POWERUP(PowerUp.class, EntityBuilderUtils::getPowerUpBuilder),
    /**
     * A {@link RollingEnemy} creator.
     */
    ROLLING_ENEMY(RollingEnemy.class, EntityBuilderUtils::getRollingEnemyBuilder),
    /**
     * A {@link WalkingEnemy} creator.
     */
    WALKING_ENEMY(WalkingEnemy.class, EntityBuilderUtils::getWalkingEnemyBuilder),
    /**
     * A {@link EnemyGenerator} creator.
     */
    ENEMY_GENERATOR(EnemyGenerator.class, EntityBuilderUtils::getEnemyGeneratorBuilder);

    private final Supplier<EntityBuilder<? extends Entity>> supplier;
    private final Class<? extends Entity> associatedClass;

    EntityCreator(final Class<? extends Entity> associatedClass, final Supplier<EntityBuilder<? extends Entity>> supplier) {
        this.supplier = supplier;
        this.associatedClass = associatedClass;
    }

    public EntityBuilder<? extends Entity> getEntityBuilder() {
        return this.supplier.get();
    }

    public Class<? extends Entity> getAssociatedClass() {
        return this.associatedClass;
    }
}
