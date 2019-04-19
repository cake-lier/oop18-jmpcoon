package model.serializable;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import com.google.common.hash.Hashing;

import model.physics.BodyShape;

/**
 * A {@link Body} that can be serialized. The serialization isn't complete, only the informations necessary for this
 * game are serialized.
 */
public class SerializableBody extends Body implements Serializable {
    private static final long serialVersionUID = 8243356758623109937L;

    private static final String NO_WRITABLE_MSG = "This body is in an illegal state, so it isn't serializable";
    private static final String NO_READABLE_MSG = "The body read is in an illegal state";

    private void writeObject(final ObjectOutputStream out) throws IOException {
        /* writing number of fixtures */
        out.writeInt(this.getFixtureCount());
        for (final BodyFixture fixture: this.getFixtures()) { 
            /* writing dimensions */
            if (fixture.getShape() instanceof Rectangle) {
                final Rectangle rectangle = (Rectangle) fixture.getShape();
                out.writeObject(BodyShape.RECTANGLE);
                out.writeDouble(rectangle.getWidth());
                out.writeDouble(rectangle.getHeight());
            } else if (fixture.getShape() instanceof Circle) {
                final Circle circle = (Circle) fixture.getShape();
                out.writeObject(BodyShape.CIRCLE);
                out.writeDouble(circle.getRadius());
            } else {
                throw new NotSerializableException(NO_WRITABLE_MSG);
            }
            /* writing physical properties */
            out.writeDouble(fixture.getDensity());
            out.writeDouble(fixture.getFriction());
            out.writeDouble(fixture.getRestitution());
            /* writing filters */
            if (fixture.getFilter() instanceof CategoryFilter) {
                final CategoryFilter filter = (CategoryFilter) fixture.getFilter();
                /* writing category */
                out.writeLong(filter.getCategory());
                /* writing mask */
                out.writeLong(filter.getMask());
            } else {
                throw new NotSerializableException(NO_WRITABLE_MSG);
            }
            /* writing if the fixture is a sensor */
            out.writeBoolean(fixture.isSensor());
        }
        /* writing world position */
        out.writeDouble(this.getWorldCenter().x);
        out.writeDouble(this.getWorldCenter().y);
        /* writing local position */
        out.writeDouble(this.getLocalCenter().x);
        out.writeDouble(this.getLocalCenter().y);
        /* writing angle */
        final double angle = this.getLocalPoint(this.getWorldCenter().add(1, 0)).getAngleBetween(new Vector2(1, 0));
        out.writeDouble(angle);
        /* writing linear velocity */
        out.writeDouble(this.getLinearVelocity().x);
        out.writeDouble(this.getLinearVelocity().y);
        /* writing angular velocity */
        out.writeDouble(this.getAngularVelocity());
        /* writing gravity scale */
        out.writeDouble(this.getGravityScale());
        /* writing linear damping */
        out.writeDouble(this.getLinearDamping());
        /* writing angular damping */
        out.writeDouble(this.getAngularDamping());
        /* writing inertia */
        out.writeDouble(this.getMass().getInertia());
        /* writing mass */
        out.writeDouble(this.getMass().getMass());
        /* writing whether the body has infinite mass or not */
        out.writeBoolean(this.getMass().isInfinite());
        /* writing whether the body can rotate or not */
        out.writeBoolean(this.getMass().getType() == MassType.FIXED_ANGULAR_VELOCITY);
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        /* reading number of fixtures */
        final int numFixtures = in.readInt();
        for (int i = 0; i < numFixtures; i++) {
            /* reading fixture dimensions */
            final BodyShape shape = (BodyShape) in.readObject();
            final BodyFixture fixture;
            if (shape == BodyShape.RECTANGLE) {
                final double width = in.readDouble();
                final double height = in.readDouble();
                fixture = this.addFixture(Geometry.createRectangle(width, height));
            } else if (shape == BodyShape.CIRCLE) {
                final double radius = in.readDouble();
                fixture = this.addFixture(Geometry.createCircle(radius));
            } else {
                throw new IllegalStateException(NO_READABLE_MSG);
            }
            /* reading physical properties */
            fixture.setDensity(in.readDouble());
            fixture.setFriction(in.readDouble());
            fixture.setRestitution(in.readDouble());
            /* reading filters */
            final long category = in.readLong();
            final long mask = in.readLong();
            fixture.setFilter(new CategoryFilter(category, mask));
            fixture.setSensor(in.readBoolean());
        }
        /* reading world position */
        final double xWorld = in.readDouble();
        final double yWorld = in.readDouble();
        /* reading local position */
        final double xLocal = in.readDouble();
        final double yLocal = in.readDouble();
        final double angle = in.readDouble();
        final Vector2 center = new Vector2(xWorld, yWorld);
        this.translate(center);
        this.rotate(angle, center);
        /* reading linear velocity */
        final double velocityX = in.readDouble();
        final double velocityY = in.readDouble();
        this.setLinearVelocity(new Vector2(velocityX, velocityY));
        /* reading angular velocity */
        this.setAngularVelocity(in.readDouble());
        /* reading gravity scale */
        this.setGravityScale(in.readDouble());
        /* reading linear damping */
        this.setLinearDamping(in.readDouble());
        /* writing angular damping */
        this.setAngularDamping(in.readDouble());
        /* reading information about mass */
        final double inertia = in.readDouble();
        final double mass = in.readDouble();
        final boolean isInfinite = in.readBoolean();
        final boolean hasFixedAngularVelocity = in.readBoolean();
        if (isInfinite && hasFixedAngularVelocity) {
            throw new IllegalStateException(NO_READABLE_MSG);
        } else if (isInfinite) {
            this.setMass(MassType.INFINITE);
        } else if (hasFixedAngularVelocity) {
            this.setMass(MassType.FIXED_ANGULAR_VELOCITY);
        } else {
            // this.setMass(new Mass(new Vector2(massCenterX, massCenterY), mass, inertia));
            this.setMass(new Mass(new Vector2(xLocal, yLocal), mass, inertia));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj != null && this.getClass().equals(obj.getClass())) {
            return this.getId().equals(((SerializableBody) obj).getId());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Hashing.murmur3_128().hashInt(this.getId().hashCode()).asInt();
    }
}
