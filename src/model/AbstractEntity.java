package model;

import java.util.Objects;

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
     * @return the velocity of this {@link AbstractEntity}, divided in its x and y components.
     */
    protected Pair<Double, Double> getVelocity() {
        return this.body.getVelocity();
    }
}
