package model.physics;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

import org.dyn4j.collision.AxisAlignedBounds;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import model.entities.EntityShape;
import model.entities.EntityType;

/**
 * a class that implements {@link PhysicalFactory}.
 */
public class PhysicalFactoryImpl implements PhysicalFactory {
    private static final String NO_TWO_WORLDS_MSG = "You can't create two worlds for this game";

    private static final long CATEGORY_WALKING_ENEMY = 1; // 000001
    private static final long CATEGORY_ROLLING_ENEMY = 2; // 000010
    private static final long CATEGORY_PLATFORM = 4; // 000100
    private static final long CATEGORY_PLAYER = 8; // 001000
    private static final long CATEGORY_LADDER = 16; // 010000
    private static final long CATEGORY_GENERATOR_ENEMY = 32; // 100000

    // TODO: indent correctly the lines that are too long
    private static final CategoryFilter LADDER_FILTER = new CategoryFilter(CATEGORY_LADDER, CATEGORY_PLAYER);
    private static final CategoryFilter PLATFORM_FILTER = 
            new CategoryFilter(CATEGORY_PLATFORM, CATEGORY_WALKING_ENEMY | CATEGORY_ROLLING_ENEMY | CATEGORY_PLATFORM | CATEGORY_PLAYER);
    private static final CategoryFilter PLAYER_FILTER = 
            new CategoryFilter(CATEGORY_PLAYER, CATEGORY_LADDER | CATEGORY_WALKING_ENEMY | CATEGORY_ROLLING_ENEMY | CATEGORY_PLATFORM | CATEGORY_PLAYER);
    private static final CategoryFilter ROLLING_ENEMY_FILTER = 
            new CategoryFilter(CATEGORY_ROLLING_ENEMY, CATEGORY_ROLLING_ENEMY | CATEGORY_PLATFORM | CATEGORY_PLAYER);
    private static final CategoryFilter WALKING_ENEMY_FILTER =
            new CategoryFilter(CATEGORY_WALKING_ENEMY, CATEGORY_WALKING_ENEMY | CATEGORY_PLATFORM | CATEGORY_PLAYER);
    private static final CategoryFilter GENERATOR_ENEMY_FILTER = 
            new CategoryFilter(CATEGORY_GENERATOR_ENEMY, CATEGORY_GENERATOR_ENEMY | CATEGORY_WALKING_ENEMY | CATEGORY_ROLLING_ENEMY | CATEGORY_PLATFORM | CATEGORY_PLAYER);

    // TODO: consider a static method and a private constructor

    private Optional<WholePhysicalWorld> physicalWorld;

    /**
     * builds a new {@link PhysicalFactoryImpl}.
     */
    public PhysicalFactoryImpl() {
        this.physicalWorld = Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhysicalWorld createPhysicalWorld(final double width, final double height) {
        if (this.physicalWorld.isPresent()) {
            throw new IllegalStateException(NO_TWO_WORLDS_MSG);
        }
        this.physicalWorld = Optional.of(new WholePhysicalWorldImpl(new World(new AxisAlignedBounds(width, height))));
        return this.physicalWorld.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StaticPhysicalBody createStaticPhysicalBody(
            final Pair<Double, Double> position, final double angle, final EntityShape shape,
                final double width, final double height, final EntityType type) {
        // TODO: repetitive code
        if (!this.physicalWorld.isPresent()) {
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
            if (type.equals(EntityType.LADDER)) {
                body.getFixture(0).setFilter(LADDER_FILTER);
                body.getFixture(0).setSensor(true);
            } else if (type.equals(EntityType.PLATFORM)) {
                body.getFixture(0).setFilter(PLATFORM_FILTER);
            }
            body.setMass(MassType.INFINITE);
            body.setUserData(type);
            this.physicalWorld.get().getWorld().addBody(body);
            final StaticPhysicalBody physicalBody = new StaticPhysicalBody(body, this.physicalWorld.get().getWorld());
            this.physicalWorld.get().addContainerAssociation(physicalBody, body, type);
            return physicalBody;
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
        final Vector2 center = new Vector2(position.getLeft(), position.getRight());
        body.translate(center);
        body.rotate(angle, center);
        return body;
    }

    private Body createCircleBody(final Pair<Double, Double> position, final double radius) {
        final Body body = new Body();
        body.addFixture(Geometry.createCircle(radius));
        final Vector2 center = new Vector2(position.getLeft() + radius / 2, position.getRight() - radius / 2);
        body.translate(center);
        return body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //TODO: add density/friction/restitution too?
    //TODO: add control with isPresent()
    public DynamicPhysicalBody createDynamicPhysicalBody(final Pair<Double, Double> position, final double angle, final EntityShape shape,
            final double width, final double height, final EntityType type) {
        final Body body = createRectangleBody(position, angle, width, height);
        body.setMass(MassType.NORMAL);
        this.physicalWorld.get().getWorld().addBody(body);
        final DynamicPhysicalBody physicalBody = new DynamicPhysicalBody(body, this.physicalWorld.get().getWorld());
        this.physicalWorld.get().addContainerAssociation(physicalBody, body, type);
        return physicalBody;
    }

}
