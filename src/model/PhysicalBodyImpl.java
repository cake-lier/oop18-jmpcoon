package model;

import org.jbox2d.dynamics.Body;

import utils.Pair;
import utils.PairImpl;

/**
 * a class implementing {@link PhysicalBody}.
 */
public class PhysicalBodyImpl implements PhysicalBody {

    private final Body body;

    /**
     * builds a new {@link PhysicalBodyImpl}.
     * @param body the {@link Body} encapsulated by this {@link PhysicalBodyImpl}
     */
    public PhysicalBodyImpl(final Body body) {
        this.body = body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getPosition() {
        return new PairImpl<Double, Double>((double) this.body.getPosition().x, (double) this.body.getPosition().y);
    }

    @Override
    public EntityShape getShape() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAngle() {
        return (double) this.body.getAngle();
    }

    @Override
    public State getState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean exist() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getVelocity() {
        return new PairImpl<Double, Double>((double) this.body.getLinearVelocity().x, (double) this.body.getLinearVelocity().y);
    }

}
