package model;

import utils.Pair;

import java.util.Optional;

import org.dyn4j.collision.AxisAlignedBounds;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

/**
 * a class that implements {@link PhysicalFactory}.
 */
public class PhysicalFactoryImpl implements PhysicalFactory {

    // TODO: consider a static method and a private constructor

    private Optional<World> world;

    /**
     * builds a new {@link PhysicalFactoryImpl}.
     */
    public PhysicalFactoryImpl() {
        this.world = Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhysicalWorld createPhysicalWorld(final double width, final double height) {
        this.world = Optional.of(new World(new AxisAlignedBounds(width * 2, height * 2)));
        return new PhysicalWorldImpl(this.world.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StaticPhysicalBody createStaticPhysicalBody(
            final Pair<Double, Double> position, final double angle, final EntityShape shape,
                final double width, final double height, final EntityType type) {
        if (!this.world.isPresent()) {
            throw new IllegalStateException("A PhysicalWorld has yet to be created!");
        }
        if (isStaticBodyAllowed(shape, type)) {
            final Body body;
            if (shape.equals(EntityShape.RECTANGLE)) {
                body = createRectangleBody(position, angle, width, height);
            } else {
                /* if a shape isn't rectangular, automatically it's circular */
                body = createCircleBody(position, width);
            }
            body.setMass(MassType.INFINITE);
            body.setUserData(type);
            this.world.get().addBody(body);
            return new StaticPhysicalBody(body, this.world.get());
        } else {
            throw new IllegalArgumentException("No such Entity can be created");
        }
    }

    private boolean isStaticBodyAllowed(final EntityShape shape, final EntityType type) {
        /* other allowed combinations could be added in the future */
        return shape.equals(EntityShape.RECTANGLE) && (type.equals(EntityType.PLATFORM) || type.equals(EntityType.LADDER));
    }

    private Body createRectangleBody(final Pair<Double, Double> position, final double angle, final double width, final double height) {
        final Body body = new Body();
        body.addFixture(Geometry.createRectangle(width, height));
        final Vector2 center = new Vector2(position.getX() + width / 2, position.getY() - height / 2);
        body.translate(center);
        body.rotate(angle);
        return body;
    }

    private Body createCircleBody(final Pair<Double, Double> position, final double radius) {
        final Body body = new Body();
        body.addFixture(Geometry.createCircle(radius));
        final Vector2 center = new Vector2(position.getX() + radius / 2, position.getY() - radius / 2);
        body.translate(center);
        return body;
    }

    @Override
    //TODO: add density/friction/restitution too?
    //TODO: add control with isPresent()
    public DynamicPhysicalBody createDynamicPhysicaBody(Pair<Double, Double> position, double angle, EntityShape shape,
            double width, double height, EntityType type) {
        final Body body = createRectangleBody(position, angle, width, height);
        body.setMass(MassType.NORMAL);
        this.world.get().addBody(body);
        return new DynamicPhysicalBody(body, this.world.get());
    }

}
