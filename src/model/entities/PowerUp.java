package model.entities;

import model.physics.BodyShape;
import model.physics.StaticPhysicalBody;

/**
 * 
 *
 */
public final class PowerUp extends StaticEntity {
    /**
     * 
     */
    private static final long serialVersionUID = -6954816914217174592L;

    /**
     * Creates a new {@link PowerUp} with the given {@link StaticPhysicalBody}. This constructor is package protected
     * because it should be only invoked by the {@link EntityBuilder} when creating a new instance of it and no one else.
     * @param body The {@link StaticPhysicalBody} that should be contained in this {@link PowerUp}.
     */
    PowerUp(final StaticPhysicalBody body) {
        super(body);
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BodyShape getShape() {
        // TODO Auto-generated method stub
        return null;
    }

}
