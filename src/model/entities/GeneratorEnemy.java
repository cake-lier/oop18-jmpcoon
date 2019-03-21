package model.entities;

import java.util.Collection;

import model.State;
import model.physics.PhysicalBody;
import utils.Pair;

/**
 * 
 */
public final class GeneratorEnemy implements Entity {

    @Override
    public Pair<Double, Double> getPosition() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EntityShape getShape() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EntityType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public State getState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAlive() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Pair<Double, Double> getDimensions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PhysicalBody getInternalPhysicalBody() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 
     * @return a.
     */
    public Collection<RollingEnemy> onTimeAdvanced() {
        return null;
    }

}
