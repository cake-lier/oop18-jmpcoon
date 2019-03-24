package model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

import utils.Pair;

/**
 * abstract class from which all the {@link Entity} of the {@link World} derives.
 */
public abstract class AbstractEntity implements Entity {

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
    public String toString() {
        return "Type: " + this.getType()
                + "; Shape: " + this.getShape()
                + "; Position: (" + this.getPosition().getX() + ", " + this.getPosition().getY()
                + "); Dimensions: " + this.getDimensions().getX() + "x" + this.getDimensions().getY()
                + "; Angle: " + this.getAngle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.body, this.getType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof AbstractEntity) {
            final AbstractEntity otherEntity = (AbstractEntity) obj;
            return this.body.equals(otherEntity.body) && this.getType().equals(otherEntity.getType());
        } else {
            return false;
        }
    }

}
