package it.unibo.jmpcoon.model.world;

import java.util.function.Supplier;

import it.unibo.jmpcoon.model.entities.AbstractEntityBuilder;
import it.unibo.jmpcoon.model.entities.EnemyGenerator;
import it.unibo.jmpcoon.model.entities.Entity;
import it.unibo.jmpcoon.model.entities.EntityBuilderUtils;
import it.unibo.jmpcoon.model.entities.EntityType;
import it.unibo.jmpcoon.model.entities.Ladder;
import it.unibo.jmpcoon.model.entities.Platform;
import it.unibo.jmpcoon.model.entities.Player;
import it.unibo.jmpcoon.model.entities.PowerUp;
import it.unibo.jmpcoon.model.entities.RollingEnemy;
import it.unibo.jmpcoon.model.entities.WalkingEnemy;

/**
 * An enum collecting all the possible creators of all possible {@link Entity}s. Its scope is package protected because it should
 * be used by the sole {@link World} which is the one who should create new {@link Entity}s.
 */
enum EntityCreator {
    /**
     * A {@link Ladder} creator.
     */
    LADDER(EntityType.LADDER, Ladder.class, EntityBuilderUtils::getLadderBuilder),
    /**
     * A {@link Player} creator.
     */
    PLAYER(EntityType.PLAYER, Player.class, EntityBuilderUtils::getPlayerBuilder),
    /**
     * A {@link Platform} creator.
     */
    PLATFORM(EntityType.PLATFORM, Platform.class, EntityBuilderUtils::getPlatformBuilder),
    /**
     * A {@link PowerUp} creator.
     */
    POWERUP(EntityType.POWERUP, PowerUp.class, EntityBuilderUtils::getPowerUpBuilder),
    /**
     * A {@link RollingEnemy} creator.
     */
    ROLLING_ENEMY(EntityType.ROLLING_ENEMY, RollingEnemy.class, EntityBuilderUtils::getRollingEnemyBuilder),
    /**
     * A {@link WalkingEnemy} creator.
     */
    WALKING_ENEMY(EntityType.WALKING_ENEMY, WalkingEnemy.class, EntityBuilderUtils::getWalkingEnemyBuilder),
    /**
     * A {@link EnemyGenerator} creator.
     */
    ENEMY_GENERATOR(EntityType.ENEMY_GENERATOR, EnemyGenerator.class, EntityBuilderUtils::getEnemyGeneratorBuilder);

    private transient Supplier<AbstractEntityBuilder<? extends Entity>> supplier;
    private final Class<? extends Entity> associatedClass;
    private final EntityType associatedType;

    EntityCreator(final EntityType associatedType, final Class<? extends Entity> associatedClass,
                  final Supplier<AbstractEntityBuilder<? extends Entity>> supplier) {
        this.associatedType = associatedType;
        this.supplier = supplier;
        this.associatedClass = associatedClass;
    }

    /**
     * Gets the {@link EntityType} of the {@link Entity} that the creator produces. 
     * @return the {@link EntityType} of the {@link Entity} that the creator produces
     */
    public EntityType getAssociatedType() {
        return this.associatedType;
    }

    /**
     * Gets the {@link Entity} builder for producing the {@link Entity} that this creator should create.
     * @return an {@link AbstractEntityBuilder} capable of building an {@link Entity} which type is associated with the
     * creator
     */
    public AbstractEntityBuilder<? extends Entity> getEntityBuilder() {
        return this.supplier.get();
    }

    /**
     Gets the {@link Class} of the {@link Entity} that the creator produces. 
     * @return the {@link Class} of the {@link Entity} that the creator produces
     */
    public Class<? extends Entity> getAssociatedClass() {
        return this.associatedClass;
    }
}
