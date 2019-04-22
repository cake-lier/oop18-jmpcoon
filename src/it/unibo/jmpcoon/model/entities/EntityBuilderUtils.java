package it.unibo.jmpcoon.model.entities;

/**
 * A utility class for producing builders for each {@link it.unibo.jmpcoon.model.entities.Entity} of this game.
 */
public final class EntityBuilderUtils {
    private EntityBuilderUtils() {
    }

    /**
     * Produces a new {@link AbstractEntityBuilder} for creating a new {@link EnemyGenerator}.
     * @return a {@link AbstractEntityBuilder} for creating a {@link EnemyGenerator}
     */
    public static AbstractEntityBuilder<EnemyGenerator> getEnemyGeneratorBuilder() {
        return new AbstractEntityBuilder<EnemyGenerator>() {
            @Override
            protected EnemyGenerator buildEntity() {
                if (super.getPhysicalFactory().isPresent() && super.getWorld().isPresent()) {
                    return new EnemyGenerator(super.createStaticPhysicalBody(EntityType.ENEMY_GENERATOR),
                                              super.getPhysicalFactory().get(),
                                              super.getWorld().get());
                } else {
                    throw new IllegalStateException("Not all the fields necessary to build an EnemyGenerator have been "
                                                    + "initialized");
                }
            }
        };
    }

    /**
     * Produces a new {@link AbstractEntityBuilder} for creating a new {@link Ladder}.
     * @return a {@link AbstractEntityBuilder} for creating a {@link Ladder}
     */
    public static AbstractEntityBuilder<Ladder> getLadderBuilder() {
        return new AbstractEntityBuilder<Ladder>() {
            @Override
            protected Ladder buildEntity() {
                return new Ladder(super.createStaticPhysicalBody(EntityType.LADDER));
            }
        };
    }

    /**
     * Produces a new {@link AbstractEntityBuilder} for creating a new {@link Player}.
     * @return a {@link AbstractEntityBuilder} for creating a {@link Player}
     */
    public static AbstractEntityBuilder<Player> getPlayerBuilder() {
        return new AbstractEntityBuilder<Player>() {
            @Override
            protected Player buildEntity() {
                return new Player(super.createPlayerPhysicalBody());
            }
        };
    }

    /**
     * Produces a new {@link AbstractEntityBuilder} for creating a new {@link Platform}.
     * @return a {@link AbstractEntityBuilder} for creating a {@link Platform}
     */
    public static AbstractEntityBuilder<Platform> getPlatformBuilder() {
        return new AbstractEntityBuilder<Platform>() {
            @Override
            protected Platform buildEntity() {
                return new Platform(super.createStaticPhysicalBody(EntityType.PLATFORM));
            }
        };
    }

    /**
     * Produces a new {@link AbstractEntityBuilder} for creating a new {@link PowerUp}.
     * @return a {@link AbstractEntityBuilder} for creating a {@link PowerUp}
     */
    public static AbstractEntityBuilder<PowerUp> getPowerUpBuilder() {
        return new AbstractEntityBuilder<PowerUp>() {
            @Override
            protected PowerUp buildEntity() {
                if (super.getPowerUpType().isPresent()) {
                    return new PowerUp(super.createStaticPhysicalBody(EntityType.POWERUP), super.getPowerUpType().get());
                } else {
                    throw new IllegalStateException("Not all the fields necessary to build a PowerUp have been initialized");
                }
            }
        };
    }

    /**
     * Produces a new {@link AbstractEntityBuilder} for creating a new {@link RollingEnemy}.
     * @return a {@link AbstractEntityBuilder} for creating a {@link RollingEnemy}
     */
    public static AbstractEntityBuilder<RollingEnemy> getRollingEnemyBuilder() {
        return new AbstractEntityBuilder<RollingEnemy>() {
            @Override
            protected RollingEnemy buildEntity() {
                return new RollingEnemy(super.createDynamicPhysicalBody(EntityType.ROLLING_ENEMY));
            }
        };
    }

    /**
     * Produces a new {@link AbstractEntityBuilder} for creating a new {@link WalkingEnemy}.
     * @return a {@link AbstractEntityBuilder} for creating a {@link WalkingEnemy}
     */
    public static AbstractEntityBuilder<WalkingEnemy> getWalkingEnemyBuilder() {
        return new AbstractEntityBuilder<WalkingEnemy>() {
            @Override
            protected WalkingEnemy buildEntity() {
                if (super.getWalkingRange().isPresent()) {
                    return new WalkingEnemy(super.createDynamicPhysicalBody(EntityType.WALKING_ENEMY), 
                                            super.getWalkingRange().get());
                } else {
                    throw new IllegalStateException("Not all the fields necessary to build a WalkingEnemy have been "
                                                       + "initialized");
                }
            }
        };
    }
}
