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
    public StaticPhysicalBody createRectangleStaticPhysicalBody(
            final Pair<Double, Double> position, final double angle, final double width, final double height, final boolean compenetrable) {
        final Body body = new Body();
        body.addFixture(Geometry.createRectangle(width, height));
        final Vector2 center = new Vector2(position.getX() + width / 2, position.getY() - height / 2);
        body.translate(center);
        body.rotate(angle);
        body.setMass(MassType.INFINITE);
        // TODO: maybe the EntityType can be passed instead of a boolean
        if (compenetrable) {
            body.setUserData(EntityType.LADDER);
        } else {
            body.setUserData(EntityType.PLATFORM);
        }
        this.world.addBody(body);
        return new StaticPhysicalBody(body);
    }

}
