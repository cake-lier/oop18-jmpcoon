package model;

import java.util.Objects;

import org.dyn4j.dynamics.Body;

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

}
