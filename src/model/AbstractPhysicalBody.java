package model;

import java.util.Objects;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.dynamics.World;

import utils.Pair;
import utils.PairImpl;

/**
 * a class implementing {@link PhysicalBody}.
 */
public abstract class AbstractPhysicalBody implements PhysicalBody {

    private final Body body;
    private final World world;

    /**
     * builds a new {@link AbstractPhysicalBody}.
     * @param body the {@link Body} encapsulated by this {@link AbstractPhysicalBody}
     * @param world the {@link World} in which the given {@link Body} lives
     */
    public AbstractPhysicalBody(final Body body, final World world) {
        this.body = Objects.requireNonNull(body);
        this.world = Objects.requireNonNull(world);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getPosition() {
        return new PairImpl<Double, Double>(this.body.getWorldCenter().x, this.body.getWorldCenter().y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAngle() {
        return this.body.getChangeInOrientation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract State getState();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exist() {
        return this.body.isActive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getVelocity() {
        return new PairImpl<Double, Double>((double) this.body.getLinearVelocity().x, (double) this.body.getLinearVelocity().y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getDimensions() {
        if (this.body.getFixtureCount() > 1 || this.body.getFixtureCount() <= 0) {
            throw new IllegalStateException(); // TODO: can we do this?
        }
        final Convex shape = this.body.getFixture(0).getShape();
        double width = 0;
        double height = 0;
        if (shape instanceof Circle) {
            width = ((Circle) shape).getRadius();
            height = width;
        } else if (shape instanceof Rectangle) {
            width = ((Rectangle) shape).getWidth();
            height = ((Rectangle) shape).getHeight();
        } else {
            // TODO: what can we do here?
        }
        return new PairImpl<Double, Double>(width, height);
    }
}
