package model;

import java.util.Objects;

import org.jbox2d.dynamics.Body;

import utils.Pair;
import utils.PairImpl;

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
        return new PairImpl<Double, Double>((double) this.body.getPosition().x, (double) this.body.getPosition().y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAngle() {
        return (double) this.body.getAngle();
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

}
