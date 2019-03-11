package model;

import utils.Pair;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

/**
 * a class that implements {@link PhysicalFactory}.
 */
public class PhysicalFactoryImpl implements PhysicalFactory {

    // TODO: consider a static method and a private constructor

    private final World world;

    /**
     * builds a new {@link PhysicalFactoryImpl}.
     */
    public PhysicalFactoryImpl() {
        // TODO: fixed size world?
        this.world = new World();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhysicalWorld createPhysicalWorld() {
        return new PhysicalWorldImpl(this.world);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StaticPhysicalBody createStaticPhysicalBody(
            final Pair<Double, Double> position, final double angle, final EntityShape shape,
                final double width, final double height, final EntityType type) {
        // TODO: make this code better
        final Body body;
        if (shape.equals(EntityShape.RECTANGLE)) {
            body = createRectangleBody(position, angle, width, height);
        } else {
            throw new IllegalArgumentException("Static entities are only rectangular");
        }
        body.setMass(MassType.INFINITE);
        if (type.equals(EntityType.PLATFORM) || type.equals(EntityType.LADDER)) {
            body.setUserData(type);
        } else {
            // TODO: document exception throwing
            throw new IllegalArgumentException("The only rectangular entities with static bodies are platforms and ladders");
        }
        this.world.addBody(body);
        return new StaticPhysicalBody(body);
    }

    private Body createRectangleBody(final Pair<Double, Double> position, final double angle, final double width, final double height) {
        final Body body = new Body();
        body.addFixture(Geometry.createRectangle(width, height));
        final Vector2 center = new Vector2(position.getX() + width / 2, position.getY() - height / 2);
        body.translate(center);
        body.rotate(angle);
        return body;
    }

}
