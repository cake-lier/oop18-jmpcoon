package model.entities;

/**
 * A utility class for producing builders for each {@link Entity} of this game.
 */
public final class EntityBuilderUtils {

    private EntityBuilderUtils() {
    }

    /**
     * @return a {@link EntityBuilder} for creating a {@link EnemyGenerator}.
     */
    public static EntityBuilder getEnemyGeneratorBuilder() {
        return new EntityBuilder() {
            @Override
            public Entity buildEntity() {
                return new EnemyGenerator(super.createStaticPhysicalBody(EntityType.ENEMY_GENERATOR));
            }
        };
    }

    /**
     * @return a {@link EntityBuilder} for creating a {@link Ladder}.
     */
    public static EntityBuilder getLadderBuilder() {
        return new EntityBuilder() {
            @Override
            public Entity buildEntity() {
                return new Ladder(super.createStaticPhysicalBody(EntityType.LADDER));
            }
        };
    }

    /**
     * @return a {@link EntityBuilder} for creating a {@link Player}.
     */
    public static EntityBuilder getPlayerBuilder() {
        return new EntityBuilder() {
            @Override
            public Entity buildEntity() {
                return new Player(super.createDynamicPhysicalBody(EntityType.PLAYER));
            }
        };
    }

    /**
     * @return a {@link EntityBuilder} for creating a {@link Platform}.
     */
    public static EntityBuilder getPlatformBuilder() {
        return new EntityBuilder() {
            @Override
            public Entity buildEntity() {
                return new Platform(super.createStaticPhysicalBody(EntityType.PLATFORM));
            }
        };
    }

    /**
     * @return a {@link EntityBuilder} for creating a {@link PowerUp}.
     */
    public static EntityBuilder getPowerUpBuilder() {
        return new EntityBuilder() {
            @Override
            public Entity buildEntity() {
                return new PowerUp(super.createStaticPhysicalBody(EntityType.POWERUP));
            }
        };
    }

    /**
     * @return a {@link EntityBuilder} for creating a {@link RollingEnemy}.
     */
    public static EntityBuilder getRollingEnemyBuilder() {
        return new EntityBuilder() {
            @Override
            public Entity buildEntity() {
                return new RollingEnemy(super.createDynamicPhysicalBody(EntityType.ROLLING_ENEMY));
            }
        };
    }

    /**
     * @return a {@link EntityBuilder} for creating a {@link WalkingEnemy}.
     */
    public static EntityBuilder getWalkingEnemyBuilder() {
        return new EntityBuilder() {
            @Override
            public Entity buildEntity() {
                return new WalkingEnemy(super.createDynamicPhysicalBody(EntityType.WALKING_ENEMY));
            }
        };
    }
}
