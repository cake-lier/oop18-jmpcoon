package model.physics;

import java.util.Objects;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Rectangle;

import model.entities.State;

import org.dyn4j.dynamics.World;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * a class implementing {@link PhysicalBody}.
 */
public abstract class AbstractPhysicalBody implements PhysicalBody {

    private final Body body;
    private double angle;

    /**
     * builds a new {@link AbstractPhysicalBody}.
     * @param body the {@link Body} encapsulated by this {@link AbstractPhysicalBody}
     * @param world the {@link World} in which the given {@link Body} lives
     */
    public AbstractPhysicalBody(final Body body, final World world) {
        this.body = Objects.requireNonNull(body);
        this.angle = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getPosition() {
        return new ImmutablePair<>(this.body.getWorldCenter().x, this.body.getWorldCenter().y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAngle() {
        this.angle = this.angle + this.body.getChangeInOrientation();
        return this.angle;
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
        return new ImmutablePair<>((double) this.body.getLinearVelocity().x, (double) this.body.getLinearVelocity().y);
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
        return new ImmutablePair<>(width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Position: (" + this.getPosition().getLeft() + ", " + this.getPosition().getRight()
                + "); Dimensions: " + this.getDimensions().getLeft() + "x" + this.getDimensions().getRight()
                + "; Angle: " + this.getAngle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof AbstractPhysicalBody) {
            final AbstractPhysicalBody otherBody = (AbstractPhysicalBody) obj;
            return this.body.equals(otherBody.body);
        } else {
            return false;
        }
    }
}
