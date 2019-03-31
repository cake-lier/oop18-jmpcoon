package model.physics;

import java.util.Objects;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import model.entities.State;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * a class implementing {@link PhysicalBody}.
 */
public abstract class AbstractPhysicalBody implements PhysicalBody {

    private final Body body;

    /**
     * builds a new {@link AbstractPhysicalBody}.
     * @param body the {@link Body} encapsulated by this {@link AbstractPhysicalBody}
     */
    public AbstractPhysicalBody(final Body body) {
        this.body = Objects.requireNonNull(body);
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
        return this.body.getLocalPoint(this.body.getWorldCenter().add(1, 0)).getAngleBetween(new Vector2(1, 0));
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
        return new ImmutablePair<>(this.body.getLinearVelocity().x, this.body.getLinearVelocity().y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getDimensions() {
        if (this.body.getFixtureCount() > 1 || this.body.getFixtureCount() <= 0) {
            throw new IllegalArgumentException("The only bodies allowed are the ones with only one fixture");
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
            throw new IllegalArgumentException("The only bodies allowed are the ones which shape is present in EntityShape");
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
