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
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import com.google.common.hash.Hashing;

import model.entities.EntityShape;
import model.entities.EntityType;

/**
 * a {@link Body} that can be serialized.
 */
public class SerializableBody extends Body implements Serializable {

    private static final long serialVersionUID = 8243356758623109937L;

    private void writeObject(final ObjectOutputStream out) throws IOException {
        /* writing number of fixtures */
        out.writeInt(this.getFixtureCount());
        for (final BodyFixture fixture: this.getFixtures()) { 
            /* writing dimensions */
            if (fixture.getShape() instanceof Rectangle) {
                final Rectangle rectangle = (Rectangle) fixture.getShape();
                out.writeObject(EntityShape.RECTANGLE);
                out.writeDouble(rectangle.getWidth());
                out.writeDouble(rectangle.getHeight());
            } else if (fixture.getShape() instanceof Circle) {
                final Circle circle = (Circle) fixture.getShape();
                out.writeObject(EntityShape.CIRCLE);
                out.writeDouble(circle.getRadius());
            } else {
                throw new NotSerializableException("This body is in an illegal state, so it isn't serializable");
            }
            /* writing filters */
            if (fixture.getFilter() instanceof CategoryFilter) {
                final CategoryFilter filter = (CategoryFilter) fixture.getFilter();
                /* writing category */
                out.writeLong(filter.getCategory());
                /* writing mask */
                out.writeLong(filter.getMask());
            } else {
                throw new NotSerializableException("This body is in an illegal state, so it isn't serializable");
            }
            /* writing if the fixture is a sensor */
            out.writeBoolean(fixture.isSensor());
        }
        /* writing world position */
        out.writeDouble(this.getWorldCenter().x);
        out.writeDouble(this.getWorldCenter().y);
        /* writing angle */
        final double angle = this.getLocalPoint(this.getWorldCenter().add(1, 0)).getAngleBetween(new Vector2(1, 0));
        out.writeDouble(angle);
        /* writing linear velocity */
        out.writeDouble(this.getLinearVelocity().x);
        out.writeDouble(this.getLinearVelocity().y);
        /* writing angular velocity */
        out.writeDouble(this.getAngularVelocity());
        /* writing whether the body has infinite mass or not */
        out.writeBoolean(this.getMass().getType() == MassType.INFINITE);
        /* writing whether the body can rotate or not */
        out.writeBoolean(this.getMass().getType() == MassType.FIXED_ANGULAR_VELOCITY);
        /* writing type */
        if (this.getUserData() instanceof EntityType) {
            out.writeObject(this.getUserData());
        } else {
            throw new NotSerializableException("This body is in an illegal state, so it isn't serializable");
        }
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        /* reading number of fixtures */
        final int numFixtures = in.readInt();
        for (int i = 0; i < numFixtures; i++) {
            /* reading fixture dimensions */
            final EntityShape shape = (EntityShape) in.readObject();
            final BodyFixture fixture;
            if (shape == EntityShape.RECTANGLE) {
                final double width = in.readDouble();
                final double height = in.readDouble();
                fixture = this.addFixture(Geometry.createRectangle(width, height));
            } else if (shape == EntityShape.CIRCLE) {
                final double radius = in.readDouble();
                fixture = this.addFixture(Geometry.createCircle(radius));
            } else {
                throw new IllegalStateException("This body is can't exist");
            }
            /* reading filters */
            final long category = in.readLong();
            final long mask = in.readLong();
            fixture.setFilter(new CategoryFilter(category, mask));
            fixture.setSensor(in.readBoolean());
        }
        /* reading world position */
        final double x = in.readDouble();
        final double y = in.readDouble();
        final double angle = in.readDouble();
        final Vector2 center = new Vector2(x, y);
        this.translate(center);
        this.rotate(angle, center);
        /* reading linear velocity */
        final double velocityX = in.readDouble();
        final double velocityY = in.readDouble();
        this.setLinearVelocity(new Vector2(velocityX, velocityY));
        /* reading angular velocity */
        this.setAngularVelocity(in.readDouble());
        /* reading information about mass */
        // TODO: look if this conditions are correct
        final boolean isInfinite = in.readBoolean();
        final boolean hasFixedAngularVelocity = in.readBoolean();
        if (isInfinite && hasFixedAngularVelocity) {
            throw new IllegalStateException("This body is can't exist");
        } else if (isInfinite) {
            this.setMass(MassType.INFINITE);
        } else if (hasFixedAngularVelocity) {
            this.setMass(MassType.FIXED_ANGULAR_VELOCITY);
        } else {
            this.setMass(MassType.NORMAL);
        }
        /* reading type */
        final EntityType type = (EntityType) in.readObject();
        this.setUserData(type);
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
