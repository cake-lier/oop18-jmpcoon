package model;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import utils.Pair;

/**
 * a class that implements {@link PhysicalFactory}.
 */
public class PhysicalFactoryImpl implements PhysicalFactory {

    private static final float GRAVITY = 9.81f;

    // TODO: consider a static method and a private constructor

    private final World world;

    /**
     * builds a new {@link PhysicalFactoryImpl}.
     */
    public PhysicalFactoryImpl() {
        final Vec2 gravity = new Vec2();
        gravity.x = 0;
        gravity.y = -GRAVITY;
        this.world = new World(gravity);
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
    public StaticPhysicalBody createStaticPhysicalBody(final Pair<Double, Double> position, final double angle) {
        final Vec2 pos = new Vec2();
        // TODO: implement safe casting
        pos.x = (float) (double) position.getX();
        pos.y = (float) (double) position.getY();
        final BodyDef bDef = new BodyDef();
        bDef.position = pos;
        bDef.angle = (float) angle;
        final Body body = new Body(bDef, this.world);
        return new StaticPhysicalBody(body);
    }

}
