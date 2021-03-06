package it.unibo.jmpcoon.model.serializable;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.dyn4j.collision.AxisAlignedBounds;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

import it.unibo.jmpcoon.model.physics.collisions.CollisionRules;
import it.unibo.jmpcoon.model.physics.collisions.ContactRules;

/**
 * A version of {@link World} that is serializable. The serialization isn't complete, only the informations necessary for this
 * game are serialized.
 */
public class SerializableWorld extends World implements Serializable {
    private static final long serialVersionUID = -3797499068693856432L;

    /**
     * Builds a new {@link SerializableWorld}.
     * @param axisAlignedBounds the {@link AxisAlignedBounds} of the {@link SerializableWorld} created
     */
    public SerializableWorld(final AxisAlignedBounds axisAlignedBounds) {
        super(axisAlignedBounds);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        /* writing world dimensions */
        /* allowed because this class can be built only using AxisAlignedBounds */
        final AxisAlignedBounds bounds = (AxisAlignedBounds) this.bounds;
        out.writeDouble(bounds.getWidth());
        out.writeDouble(bounds.getHeight());
        /* writes the physics rules classes, which are unique */
        out.writeObject(this.getListeners(ContactRules.class).get(0));
        out.writeObject(this.getListeners(CollisionRules.class).get(0));
        /* writing number of bodies */
        out.writeInt(this.getBodyCount());
        /* writing bodies */
        /* need to be sure all the bodies are serializable */
        if (this.getBodies().stream().allMatch(b -> (b instanceof Serializable))) {
            for (final Body body: this.getBodies()) {
                out.writeObject((Serializable) body);
            }
        } else {
            throw new NotSerializableException("Not all the bodies contained in this World are serializable");
        }
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        /* reading dimensions */
        final double width = in.readDouble();
        final double height = in.readDouble();
        /* reading saved physics rules */
        this.addListener(ContactRules.class.cast(in.readObject()));
        this.addListener(CollisionRules.class.cast(in.readObject()));
        this.setBounds(new AxisAlignedBounds(width, height));
        /* reading number of bodies */
        final int numBodies = in.readInt();
        /* reading bodies */
        for (int i = 0; i < numBodies; i++) {
            final Body body = (Body) in.readObject();
            this.addBody(body);
        }
    }
}
