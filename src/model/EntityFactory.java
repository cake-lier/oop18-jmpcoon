package model;

import java.util.Objects;

import utils.Pair;

/**
 * a class used to produce instances of {@link Entity} whose {@link PhysicalBody} is inside a specific {@link PhysicalWorld}.
 */
public class EntityFactory {

    private static final double PLATFORM_WIDTH = 200;
    private static final double PLATFORM_HEIGHT = 10;

    private static final double LADDER_WIDTH = 20;
    private static final double LADDER_HEIGHT = 50;

    private final PhysicalFactory factory;

    /**
     * builds a new {@link EntityFactory}.
     * @param factory the factory that will produce the {@link PhysicalBody} for the {@link Entity}
     */
    public EntityFactory(final PhysicalFactory factory) {
        this.factory = Objects.requireNonNull(factory);
    }

    /**
     * creates a {@link Ladder}.
     * @param position the position in which the {@link Ladder} is created
     * @return the {@link Ladder} created
     */
    public Ladder createLadder(final Pair<Double, Double> position) {
        return new Ladder(this.factory.createStaticPhysicalBody(position, 0, EntityShape.RECTANGLE, LADDER_WIDTH, LADDER_HEIGHT, EntityType.LADDER));
    }

    /**
     * creates a {@link Platform}.
     * @param position the position in which the {@link Platform} is created
     * @param angle the inclination of the {@link Platform}
     * @return the {@link Platform} created
     */
    public Platform createPlatform(final Pair<Double, Double> position, final double angle) {
        return new Platform(this.factory.createStaticPhysicalBody(position, angle, EntityShape.RECTANGLE, PLATFORM_WIDTH, PLATFORM_HEIGHT, EntityType.PLATFORM));
    }
}
