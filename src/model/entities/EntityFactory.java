package model.entities;

import java.util.Objects;
import model.physics.PhysicalFactory;
import org.apache.commons.lang3.tuple.Pair;

/**
 * a class used to produce instances of {@link Entity} whose {@link PhysicalBody} is inside a specific {@link PhysicalWorld}.
 */
public class EntityFactory {

    private final PhysicalFactory factory;

    /**
     * builds a new {@link EntityFactory}.
     * @param factory the factory that will produce the {@link PhysicalBody} for the {@link Entity}
     */
    public EntityFactory(final PhysicalFactory factory) {
        this.factory = Objects.requireNonNull(factory);
    }

    /**
     * creates a {@link Double}.
     * @param position the center of the {@link Double} created
     * @param width the width of the {@link Double} created
     * @param height the height of the {@link Double} created
     * @return the {@link Double} created
     */
    public Ladder createLadder(final Pair<Double, Double> position, final double width, final double height) {
        return new Ladder(this.factory.createStaticPhysicalBody(position, 0, EntityShape.RECTANGLE, width, height, EntityType.LADDER));
    }

    /**
     * creates a {@link Platform}.
     * @param position the center of the {@link Platform} created
     * @param width the width of the {@link Platform} created
     * @param height the height of the {@link Platform} created
     * @param angle the inclination of the {@link Platform}
     * @return the {@link Platform} created
     */
    public Platform createPlatform(final Pair<Double, Double> position, final double width, final double height, final double angle) {
        return new Platform(this.factory.createStaticPhysicalBody(position, angle, EntityShape.RECTANGLE, width, height, EntityType.PLATFORM));
    }

    /**
     * 
     * @param position the center of the {@link WalkingEnemy} created
     * @param width the width of the {@link WalkingEnemy} created
     * @param height the height of the {@link WalkingEnemy} created
     * @return the {@link WalkingEnemy} created
     */
    public WalkingEnemy createWalkingEnemy(final Pair<Double, Double> position, final double width, final double height) {
        return new WalkingEnemy(this.factory.createDynamicPhysicalBody(position, 0, EntityShape.RECTANGLE, width, height, EntityType.WALKING_ENEMY));
    }

    /**
     * creates a {@link RollingEnemy}.
     * @param position the center of the {@link RollingEnemy} created
     * @param width the width of the {@link RollingEnemy} created
     * @param height the height of the {@link RollingEnemy} created
     * @return the {@link RollingEnemy} created
     */
    public RollingEnemy createRollingEnemy(final Pair<Double, Double> position, final double width, final double height) {
        return new RollingEnemy(this.factory.createDynamicPhysicalBody(position, 0, EntityShape.CIRCLE, width, height, EntityType.ROLLING_ENEMY));
    }

    /**
     * creates a {@link Player}.
     * @param position the center of the {@link Player} created
     * @param width the width of the {@link Player} created
     * @param height the height of the {@link Player} created
     * @return the {@link Player} created
     */
    public Player createPlayer(final Pair<Double, Double> position, final double width, final double height) {
        return new Player(this.factory.createDynamicPhysicalBody(position, 0, EntityShape.RECTANGLE,
                width, height, EntityType.PLAYER));
    }
}
