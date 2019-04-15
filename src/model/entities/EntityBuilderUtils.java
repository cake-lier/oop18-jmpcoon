package model.entities;

/**
 * A utility class for producing builders for each {@link Entity} of this game.
 */
public final class EntityBuilderUtils {

    private EntityBuilderUtils() {
    }

    /**
     * Produces a new {@link EntityBuilder} for creating a new {@link EnemyGenerator}.
     * @return A {@link EntityBuilder} for creating a {@link EnemyGenerator}.
     */
    public static EntityBuilder<EnemyGenerator> getEnemyGeneratorBuilder() {
        return new EntityBuilder<EnemyGenerator>() {
            @Override
            protected EnemyGenerator buildEntity() {
                return new EnemyGenerator(super.createStaticPhysicalBody(EntityType.ENEMY_GENERATOR));
            }
        };
    }

    /**
     * Produces a new {@link EntityBuilder} for creating a new {@link Ladder}.
     * @return a {@link EntityBuilder} for creating a {@link Ladder}.
     */
    public static EntityBuilder<Ladder> getLadderBuilder() {
        return new EntityBuilder<Ladder>() {
            @Override
            protected Ladder buildEntity() {
                return new Ladder(super.createStaticPhysicalBody(EntityType.LADDER));
            }
        };
    }

    /**
     * Produces a new {@link EntityBuilder} for creating a new {@link Player}.
     * @return a {@link EntityBuilder} for creating a {@link Player}.
     */
    public static EntityBuilder<Player> getPlayerBuilder() {
        return new EntityBuilder<Player>() {
            @Override
            protected Player buildEntity() {
                return new Player(super.createDynamicPhysicalBody(EntityType.PLAYER));
            }
        };
    }

    /**
     * Produces a new {@link EntityBuilder} for creating a new {@link Platform}.
     * @return a {@link EntityBuilder} for creating a {@link Platform}.
     */
    public static EntityBuilder<Platform> getPlatformBuilder() {
        return new EntityBuilder<Platform>() {
            @Override
            protected Platform buildEntity() {
                return new Platform(super.createStaticPhysicalBody(EntityType.PLATFORM));
            }
        };
    }

    /**
     * Produces a new {@link EntityBuilder} for creating a new {@link PowerUp}.
     * @return a {@link EntityBuilder} for creating a {@link PowerUp}.
     */
    public static EntityBuilder<PowerUp> getPowerUpBuilder() {
        return new EntityBuilder<PowerUp>() {
            @Override
            protected PowerUp buildEntity() {
                return new PowerUp(super.createStaticPhysicalBody(EntityType.POWERUP), this.getPowerUpType());
            }
        };
    }

    /**
     * Produces a new {@link EntityBuilder} for creating a new {@link RollingEnemy}.
     * @return a {@link EntityBuilder} for creating a {@link RollingEnemy}.
     */
    public static EntityBuilder<RollingEnemy> getRollingEnemyBuilder() {
        return new EntityBuilder<RollingEnemy>() {
            @Override
            protected RollingEnemy buildEntity() {
                return new RollingEnemy(super.createDynamicPhysicalBody(EntityType.ROLLING_ENEMY));
            }
        };
    }

    /**
     * Produces a new {@link EntityBuilder} for creating a new {@link WalkingEnemy}.
     * @return a {@link EntityBuilder} for creating a {@link WalkingEnemy}.
     */
    public static EntityBuilder<WalkingEnemy> getWalkingEnemyBuilder() {
        return new EntityBuilder<WalkingEnemy>() {
            @Override
            protected WalkingEnemy buildEntity() {
                return new WalkingEnemy(super.createDynamicPhysicalBody(EntityType.WALKING_ENEMY));
            }
        };
    }
}
