package model.entities;

import java.util.Objects;

import model.physics.PhysicalBody;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.hash.Hashing;

/**
 * abstract class from which all the {@link Entity} of the {@link model.world.World} derives.
 */
public abstract class AbstractEntity implements Entity {
    private static final long serialVersionUID = -7374912841474322755L;

    private final PhysicalBody body;

    /**
     * builds a new {@link AbstractEntity}.
     * @param body the body of this {@link AbstractEntity}
     */
    public AbstractEntity(final PhysicalBody body) {
        this.body = Objects.requireNonNull(body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getPosition() {
        return this.body.getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract EntityShape getShape();

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAngle() {
        return this.body.getAngle();
    }

    @Override
    public abstract EntityType getType();

    /**
     * {@inheritDoc}
     */
    @Override
    public State getState() {
        return this.body.getState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAlive() {
        return this.body.exist();
    }

    /**
     * {@inheritDoc}
     */
    public Pair<Double, Double> getDimensions() {
        return this.body.getDimensions();
    }

    /**
     * @return the velocity of this {@link AbstractEntity}, divided in its x and y components.
     */
    protected Pair<Double, Double> getVelocity() {
        return this.body.getVelocity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhysicalBody getPhysicalBody() {
        return this.body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Type: " + this.getType()
                + "; Shape: " + this.getShape()
                + "; Position: (" + this.getPosition().getLeft() + ", " + this.getPosition().getRight()
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
                && this.body.equals(((AbstractEntity) obj).body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Hashing.murmur3_128().hashInt(this.body.hashCode()).asInt();
    }

}
