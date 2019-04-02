package model.serializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.stream.IntStream;

import org.apache.commons.lang3.SerializationException;
import org.dyn4j.collision.AxisAlignedBounds;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

/**
 * A version of {@link World} that is serializable.
 */
public class SerializableWorld extends World implements Serializable {

    private static final long serialVersionUID = -3797499068693856432L;

    /**
     * builds a new {@link SerializableWorld}.
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
        /* writing number of bodies */
        out.writeInt(this.getBodyCount());
        /* writing bodies */
        /* need to be sure all the bodies are serializable */
        if (this.getBodies().stream().allMatch(b -> (b instanceof Serializable))) {
            this.getBodies().forEach(b -> {
                try {
                    out.writeObject(b);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        } else {
            throw new SerializationException("Not all the bodies contained in this World are serializable");
        }
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        /* reading dimensions */
        final double width = in.readDouble();
        final double height = in.readDouble();
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
