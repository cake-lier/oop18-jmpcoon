package model.physics;

import java.util.Objects;

import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import com.google.common.hash.Hashing;

import model.entities.EntityState;
import model.serializable.SerializableBody;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * A class implementing {@link PhysicalBody}. It encapsulates a {@link SerializableBody} that must be either a circle or a 
 * rectangle, and it must have only one {@link org.dyn4j.collision.Fixture}.
 */
public abstract class AbstractPhysicalBody implements PhysicalBody {
    private static final long serialVersionUID = 8007445555444017586L;

    private final SerializableBody body;

    /**
     * builds a new {@link AbstractPhysicalBody}.
     * @param body the {@link SerializableBody} encapsulated by this {@link AbstractPhysicalBody}
     */
    public AbstractPhysicalBody(final SerializableBody body) {
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
    public abstract EntityState getState();

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
        final Convex shape = this.body.getFixture(0).getShape();
        double width = 0;
        double height = 0;
        if (shape instanceof Circle) {
            width = 2 * ((Circle) shape).getRadius();
            height = width;
        } else if (shape instanceof Rectangle) {
            width = ((Rectangle) shape).getWidth();
            height = ((Rectangle) shape).getHeight();
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
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        return obj != null
                && this.getClass().equals(obj.getClass()) 
                && this.body.equals(((AbstractPhysicalBody) obj).body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Hashing.murmur3_128().hashInt(this.body.hashCode()).asInt();
    }
}
