package model.physics;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;
import java.util.function.Supplier;

import org.dyn4j.collision.AxisAlignedBounds;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import model.entities.EntityShape;
import model.entities.EntityType;
import model.serializable.SerializableBody;
import model.serializable.SerializableWorld;

/**
 * a class that implements {@link PhysicalFactory}.
 */
public class PhysicalFactoryImpl implements PhysicalFactory {
    private static final long serialVersionUID = -3251686827966500039L;

    private static final String NO_TWO_WORLDS_MSG = "You can't create two worlds for this game";

    private static final long CATEGORY_WALKING_ENEMY = 1; // 0000001
    private static final long CATEGORY_ROLLING_ENEMY = 2; // 0000010
    private static final long CATEGORY_PLATFORM = 4; // 0000100
    private static final long CATEGORY_PLAYER = 8; // 0001000
    private static final long CATEGORY_LADDER = 16; // 0010000
    //private static final long CATEGORY_GENERATOR_ENEMY = 32; // 0100000
    private static final long CATEGORY_POWERUP = 64; // 1000000

    // TODO: indent correctly the lines that are too long
    private static final CategoryFilter LADDER_FILTER = new CategoryFilter(CATEGORY_LADDER, CATEGORY_PLAYER);
    private static final CategoryFilter PLATFORM_FILTER = new CategoryFilter(CATEGORY_PLATFORM,
            CATEGORY_WALKING_ENEMY | CATEGORY_ROLLING_ENEMY | CATEGORY_PLATFORM | CATEGORY_PLAYER);
    private static final CategoryFilter PLAYER_FILTER = new CategoryFilter(CATEGORY_PLAYER,
            CATEGORY_LADDER | CATEGORY_WALKING_ENEMY | CATEGORY_ROLLING_ENEMY | CATEGORY_PLATFORM | CATEGORY_PLAYER | CATEGORY_POWERUP);
    private static final CategoryFilter ROLLING_ENEMY_FILTER = new CategoryFilter(CATEGORY_ROLLING_ENEMY,
            CATEGORY_ROLLING_ENEMY | CATEGORY_PLATFORM | CATEGORY_PLAYER);
    private static final CategoryFilter WALKING_ENEMY_FILTER = new CategoryFilter(CATEGORY_WALKING_ENEMY,
            CATEGORY_WALKING_ENEMY | CATEGORY_PLATFORM | CATEGORY_PLAYER);
    /*private static final CategoryFilter GENERATOR_ENEMY_FILTER = new CategoryFilter(CATEGORY_GENERATOR_ENEMY,
            CATEGORY_GENERATOR_ENEMY | CATEGORY_WALKING_ENEMY | CATEGORY_ROLLING_ENEMY | CATEGORY_PLATFORM
                    | CATEGORY_PLAYER);*/
    private static final CategoryFilter POWERUP_FILTER = new CategoryFilter(CATEGORY_POWERUP,
            CATEGORY_PLATFORM | CATEGORY_PLAYER | CATEGORY_POWERUP);

    // TODO: consider a static method and a private constructor

    private transient Optional<WholePhysicalWorld> physicalWorld;
    private Pair<Double, Double> worldDimensions;

    /**
     * builds a new {@link PhysicalFactoryImpl}.
     */
    public PhysicalFactoryImpl() {
        this.physicalWorld = Optional.empty();
        this.worldDimensions = new ImmutablePair<>(0.0, 0.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhysicalWorld createPhysicalWorld(final double width, final double height) {
        throwException(this.physicalWorld.isPresent(), () -> new IllegalStateException(NO_TWO_WORLDS_MSG));
        this.worldDimensions = new ImmutablePair<>(width, height);
        this.physicalWorld = Optional.of(new WholePhysicalWorldImpl(new SerializableWorld(new AxisAlignedBounds(width * 2, height * 2))));
        return this.physicalWorld.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StaticPhysicalBody createStaticPhysicalBody(final Pair<Double, Double> position, final double angle,
            final EntityShape shape, final double width, final double height, final EntityType type) {
        // TODO: repetitive code
        throwException(!this.physicalWorld.isPresent(), () -> new IllegalStateException("A PhysicalWorld has yet to be created!"));
        throwException(!isStaticBodyAllowed(shape, type), () -> new IllegalArgumentException("No such Entity can be created"));
        throwException(!isPositionInsideWorld(position), () -> new IllegalArgumentException("The entity would be created outside the world"));

        final SerializableBody body;
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
            body.getFixture(0).setFriction(0.5);
        } else if (type.equals(EntityType.POWERUP)) {
            body.getFixture(0).setFilter(POWERUP_FILTER);
            body.getFixture(0).setFriction(0.5);
        }
        body.setMass(MassType.INFINITE);
        body.setUserData(type);
        this.physicalWorld.get().getWorld().addBody(body);
        final StaticPhysicalBody physicalBody = new StaticPhysicalBody(body);
        this.physicalWorld.get().addContainerAssociation(physicalBody, body, type);
        return physicalBody;

    }

    private boolean isStaticBodyAllowed(final EntityShape shape, final EntityType type) {
        /* other allowed combinations could be added in the future */
        return shape.equals(EntityShape.RECTANGLE) && (type.equals(EntityType.PLATFORM) || type.equals(EntityType.LADDER) || type.equals(EntityType.POWERUP));
    }

    private SerializableBody createRectangleBody(final Pair<Double, Double> position, final double angle, final double width,
                                     final double height) {
        final SerializableBody body = new SerializableBody();
        body.addFixture(Geometry.createRectangle(width, height));
        final Vector2 center = new Vector2(position.getLeft(), position.getRight());
        body.translate(center);
        body.rotate(angle, center);
        return body;
    }

    private SerializableBody createCircleBody(final Pair<Double, Double> position, final double radius) {
        final SerializableBody body = new SerializableBody();
        body.addFixture(Geometry.createCircle(radius));
        final Vector2 center = new Vector2(position.getLeft(), position.getRight());
        body.translate(center);
        return body;
    }

    private boolean isDynamicBodyAllowed(final EntityShape shape, final EntityType type) {
        return (shape.equals(EntityShape.CIRCLE) && type.equals(EntityType.ROLLING_ENEMY)) 
                || shape.equals(EntityShape.RECTANGLE) && (type.equals(EntityType.WALKING_ENEMY) || type.equals(EntityType.PLAYER));
    }

    private boolean isPositionInsideWorld(final Pair<Double, Double> position) {
        return position.getLeft() >= 0
                && position.getRight() >= 0
                && position.getLeft() <= this.worldDimensions.getLeft()
                && position.getRight() <= this.worldDimensions.getRight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    // TODO: add density/friction/restitution too?
    // TODO: add control with isPresent()
    public DynamicPhysicalBody createDynamicPhysicalBody(final Pair<Double, Double> position, final double angle,
            final EntityShape shape, final double width, final double height, final EntityType type) {

        throwException(!this.physicalWorld.isPresent(), () -> new IllegalStateException("A PhysicalWorld has yet to be created!"));
        throwException(!isDynamicBodyAllowed(shape, type), () -> new IllegalArgumentException("No such Entity can be created"));
        throwException(!isPositionInsideWorld(position), () -> new IllegalArgumentException("The entity would be created outside the world"));

        final SerializableBody body;
        if (shape.equals(EntityShape.CIRCLE)) {
            body = createCircleBody(position, width);
        } else {
            body = createRectangleBody(position, angle, width, height);
        }

        body.setMass(MassType.NORMAL);
        if (type.equals(EntityType.PLAYER)) {
            body.getFixture(0).setFilter(PLAYER_FILTER);
            body.setMass(MassType.FIXED_ANGULAR_VELOCITY);
        } else if (type.equals(EntityType.ROLLING_ENEMY)) {
            body.getFixture(0).setFilter(ROLLING_ENEMY_FILTER);
        } else if (type.equals(EntityType.WALKING_ENEMY)) {
            body.getFixture(0).setFilter(WALKING_ENEMY_FILTER);
        }
        body.setUserData(type);

        this.physicalWorld.get().getWorld().addBody(body);
        final DynamicPhysicalBody physicalBody = new DynamicPhysicalBody(body);
        this.physicalWorld.get().addContainerAssociation(physicalBody, body, type);
        return physicalBody;
    }

    private void throwException(final boolean condition, final Supplier<RuntimeException> supplier) {
        if (condition) {
            throw supplier.get();
        }
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (this.physicalWorld.isPresent()) {
            out.writeBoolean(true);
            out.writeObject(this.physicalWorld.get());
        } else {
            out.writeBoolean(false);
        }
    }

    private void readObject(final ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        if (in.readBoolean()) {
            this.physicalWorld = Optional.of((WholePhysicalWorld) in.readObject());
        }
    }
}
