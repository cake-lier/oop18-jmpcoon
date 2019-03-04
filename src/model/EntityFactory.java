package model;

import org.jbox2d.dynamics.Body;

import utils.Pair;

/**
 * a class used to produce instances of {@link Entity} whose {@link PhysicalBody} is inside a specific {@link PhysicalWorld}.
 */
public class EntityFactory {

    private final PhysicalWorld world;

    /**
     * builds a new {@link EntityFactory}.
     * @param world the {@link PhysicalWorld} inside which the {@link PhysicalBody} of the {@link Entity} produced lives
     */
    public EntityFactory(final PhysicalWorld world) {
        this.world = world;
    }

    /**
     * creates a {@link Ladder}.
     * @param position the position in which the {@link Ladder} is created
     * @return the {@link Ladder} created
     */
    public Ladder createLadder(final Pair<Double, Double> position) {
        // TODO: create the body
        final Body body = null;
        final Ladder ladder = new Ladder(new StaticPhysicalBody(body));
        return ladder;
    }

    /**
     * creates a {@link Platform}.
     * @param position the position in which the {@link Platform} is created
     * @param angle the inclination of the {@link Platform}
     * @return the {@link Platform} created
     */
    public Platform createPlatform(final Pair<Double, Double> position, final double angle) {
        // TODO: create the body
        final Body body = null;
        final Platform platform = new Platform(new StaticPhysicalBody(body));
        return platform;
    }
}
