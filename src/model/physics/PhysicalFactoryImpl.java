package model.physics;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Supplier;

import com.google.common.base.Optional;

import org.dyn4j.collision.AxisAlignedBounds;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import model.entities.EntityType;
import model.entities.PowerUpType;
import model.serializable.SerializableBody;
import model.serializable.SerializableWorld;
import model.world.NotifiableWorld;

/**
 * Class implementation of a {@link PhysicalFactory}.
 */
public class PhysicalFactoryImpl implements PhysicalFactory {
    private static final long serialVersionUID = -3251686827966500039L;

    private static final double PLATFORM_FRICTION = 0.5;
    //private static final double ROLLING_ENEMY_FRICTION = 0.0;
    private static final double ROLLING_ENEMY_ANGULAR_DAMPING = 1.3;
    private static final double ROLLING_ENEMY_GRAVITY_SCALE = 2.6;

    private static final String NO_TWO_WORLDS_MSG = "You can't create two worlds for this game";
    private static final String NO_WORLD_MSG = "A PhysicalWorld has yet to be created!";
    private static final String ILLEGAL_ENTITY_MSG = "No such Entity can be created";
    private static final String TOO_MANY_FIXTURES_MSG = "The body created has an illegal number of fixtures";
    private static final String ILLEGAL_DIMENSIONS_MSG = "A circular entity can't have different width and height";
    private static final String OUTSIDE_WORLD_MSG = "The entity would be created outside the world";

    private static final long CATEGORY_WALKING_ENEMY = 1; // 000001
    private static final long CATEGORY_ROLLING_ENEMY = 2; // 000010
    private static final long CATEGORY_PLATFORM = 4; // 000100
    private static final long CATEGORY_PLAYER = 8; // 001000
    private static final long CATEGORY_LADDER = 16; // 010000
    private static final long CATEGORY_GENERATOR_ENEMY = 32; // 100000
    private static final long CATEGORY_POWERUP = 64; // 1000000

    private static final CategoryFilter LADDER_FILTER = new CategoryFilter(CATEGORY_LADDER, CATEGORY_PLAYER);
    private static final CategoryFilter PLATFORM_FILTER = new CategoryFilter(CATEGORY_PLATFORM, CATEGORY_WALKING_ENEMY
                                                                                                | CATEGORY_ROLLING_ENEMY
                                                                                                | CATEGORY_PLATFORM 
                                                                                                | CATEGORY_PLAYER);
    private static final CategoryFilter PLAYER_FILTER = new CategoryFilter(CATEGORY_PLAYER, CATEGORY_LADDER 
                                                                                            | CATEGORY_WALKING_ENEMY
                                                                                            | CATEGORY_ROLLING_ENEMY
                                                                                            | CATEGORY_PLATFORM
                                                                                            | CATEGORY_PLAYER
                                                                                            | CATEGORY_POWERUP);
    private static final CategoryFilter ROLLING_ENEMY_FILTER = new CategoryFilter(CATEGORY_ROLLING_ENEMY, CATEGORY_ROLLING_ENEMY
                                                                                                          | CATEGORY_PLATFORM 
                                                                                                          | CATEGORY_PLAYER);
    private static final CategoryFilter WALKING_ENEMY_FILTER = new CategoryFilter(CATEGORY_WALKING_ENEMY, CATEGORY_WALKING_ENEMY
                                                                                                          | CATEGORY_PLATFORM
                                                                                                          | CATEGORY_PLAYER);
    private static final CategoryFilter ENEMY_GENERATOR_FILTER = new CategoryFilter(CATEGORY_GENERATOR_ENEMY, 
                                                                                        CATEGORY_GENERATOR_ENEMY
                                                                                        | CATEGORY_WALKING_ENEMY 
                                                                                        | CATEGORY_ROLLING_ENEMY 
                                                                                        | CATEGORY_PLATFORM
                                                                                        | CATEGORY_PLAYER);

    private static final CategoryFilter POWERUP_FILTER = new CategoryFilter(CATEGORY_POWERUP, CATEGORY_PLATFORM 
                                                                                              | CATEGORY_PLAYER
                                                                                              | CATEGORY_POWERUP);

    private Optional<WholePhysicalWorld> physicalWorld;
    private final MutablePair<Double, Double> worldDimensions;

    /**
     * Default constructor for a {@link PhysicalFactory} builds a new {@link PhysicalFactoryImpl}.
     * @param
     */
    public PhysicalFactoryImpl() {
        this.physicalWorld = Optional.absent();
        this.worldDimensions = new MutablePair<>(0.0, 0.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhysicalWorld createPhysicalWorld(final NotifiableWorld outerWorld, final double width, final double height) {
        this.throwException(this.physicalWorld.isPresent(), () -> new IllegalStateException(NO_TWO_WORLDS_MSG));
        this.worldDimensions.setLeft(width);
        this.worldDimensions.setRight(height);
        this.physicalWorld = Optional.of(new WholePhysicalWorldImpl(outerWorld, 
                                                                        new SerializableWorld(new AxisAlignedBounds(width * 2, 
                                                                                                                        height * 2))));
        return this.physicalWorld.get();
    }

    /**
     * {@inheritDoc}
     * @throws IllegalStateException If the {@link org.dyn4j.dynamics.Body} encapsulated is in an illegal state (specifically, if
     * it has too many {@link org.dyn4j.collision.Fixture}.
     */
    @Override
    public StaticPhysicalBody createStaticPhysicalBody(final Pair<Double, Double> position, final double angle,
                                                           final BodyShape shape, final double width, final double height, 
                                                               final EntityType type, final Optional<PowerUpType> powerUpType)
                                                                       throws IllegalStateException {
        this.checks(position, shape, type, true);
        final SerializableBody body = this.createBody(shape, position, angle, width, height);
        this.throwException(body.getFixtureCount() != 1, () -> new IllegalStateException(TOO_MANY_FIXTURES_MSG));
        switch (type) {
            case LADDER:
                body.getFixture(0).setFilter(LADDER_FILTER);
                body.getFixture(0).setSensor(true);
                break;
            case PLATFORM:
                body.getFixture(0).setFilter(PLATFORM_FILTER);
                body.getFixture(0).setFriction(PLATFORM_FRICTION);
                break;
            case ENEMY_GENERATOR:
                body.getFixture(0).setFilter(ENEMY_GENERATOR_FILTER);
                break;
            case POWERUP:
                body.getFixture(0).setFilter(POWERUP_FILTER);
                break;
            default:
                this.throwException(true, () -> new IllegalArgumentException(ILLEGAL_ENTITY_MSG));
        }
        body.setMass(MassType.INFINITE);
        body.setUserData(type);
        this.physicalWorld.get().getWorld().addBody(body);
        final StaticPhysicalBody physicalBody = new StaticPhysicalBody(body);
        this.physicalWorld.get().addContainerAssociation(physicalBody, body, type);
        if (powerUpType.isPresent()) {
            this.physicalWorld.get().addPowerUpTypeAssociation(body, powerUpType.get());
        }
        return physicalBody;
    }

    /**
     * {@inheritDoc}
     * @throws IllegalStateException If the {@link org.dyn4j.dynamics.Body} encapsulated is in an illegal state (specifically, if
     * it has too many {@link org.dyn4j.collision.Fixture}.
     */
    @Override
    public DynamicPhysicalBody createDynamicPhysicalBody(final Pair<Double, Double> position, final double angle,
                                                             final BodyShape shape, final double width, final double height,
                                                                 final EntityType type) throws IllegalStateException {
        this.checks(position, shape, type, false);
        final SerializableBody body = this.createBody(shape, position, angle, width, height);
        this.throwException(body.getFixtureCount() != 1, () -> new IllegalStateException(TOO_MANY_FIXTURES_MSG));
        switch (type) {
            case PLAYER:
                body.getFixture(0).setFilter(PLAYER_FILTER);
                body.setMass(MassType.FIXED_ANGULAR_VELOCITY);
                break;
            case ROLLING_ENEMY:
                body.getFixture(0).setFilter(ROLLING_ENEMY_FILTER);
                body.setMass(MassType.NORMAL);
                body.setGravityScale(ROLLING_ENEMY_GRAVITY_SCALE);
                body.setAngularDamping(ROLLING_ENEMY_ANGULAR_DAMPING);
                break;
            case WALKING_ENEMY:
                body.getFixture(0).setFilter(WALKING_ENEMY_FILTER);
                body.setMass(MassType.NORMAL);
                break;
            default:
                this.throwException(true, () -> new IllegalArgumentException(ILLEGAL_ENTITY_MSG));
        }
        body.setUserData(type);
        this.physicalWorld.get().getWorld().addBody(body);
        final DynamicPhysicalBody physicalBody = type == EntityType.PLAYER ? new PlayerPhysicalBody(body) : new DynamicPhysicalBody(body);
        this.physicalWorld.get().addContainerAssociation(physicalBody, body, type);
        return physicalBody;
    }

    private SerializableBody createBody(final BodyShape shape, final Pair<Double, Double> position, final double angle,
                                            final double width, final double height) {
        final SerializableBody body;
        final Vector2 center = new Vector2(position.getLeft(), position.getRight());
        if (shape == BodyShape.CIRCLE) {
            this.throwException(width != height, () -> new IllegalArgumentException(ILLEGAL_DIMENSIONS_MSG));
            body = createCircleBody(width / 2);
        } else {
            /* if a shape isn't a circle, automatically it's a rectangle */
            body = createRectangleBody(width, height);
        }
        body.translate(center);
        body.rotate(angle, center);
        return body;
    }

    private SerializableBody createRectangleBody(final double width, final double height) {
        final SerializableBody body = new SerializableBody();
        body.addFixture(Geometry.createRectangle(width, height));
        return body;
    }

    private SerializableBody createCircleBody(final double radius) {
        final SerializableBody body = new SerializableBody();
        body.addFixture(Geometry.createCircle(radius));
        return body;
    }

    private boolean isStaticBodyAllowed(final BodyShape shape, final EntityType type) {
        /* other allowed combinations could be added in the future */
        return (shape == BodyShape.RECTANGLE && (type == EntityType.PLATFORM 
                                                 || type == EntityType.LADDER
                                                 || type == EntityType.POWERUP))
               || (shape == BodyShape.CIRCLE && type == EntityType.ENEMY_GENERATOR);
    }

    private boolean isDynamicBodyAllowed(final BodyShape shape, final EntityType type) {
        /* other allowed combinations could be added in the future */
        return (shape == BodyShape.CIRCLE && type == EntityType.ROLLING_ENEMY) 
               || (shape == BodyShape.RECTANGLE && (type == EntityType.WALKING_ENEMY || type == EntityType.PLAYER));
    }

    private boolean isPositionInsideWorld(final Pair<Double, Double> position) {
        return position.getLeft() >= 0
               && position.getRight() >= 0
               && position.getLeft() <= this.worldDimensions.getLeft()
               && position.getRight() <= this.worldDimensions.getRight();
    }

    private void checks(final Pair<Double, Double> position, final BodyShape shape, 
                            final EntityType type, final boolean isStatic) {
        this.throwException(!this.physicalWorld.isPresent(), () -> new IllegalStateException(NO_WORLD_MSG));
        this.throwException(!this.isPositionInsideWorld(position), () -> new IllegalArgumentException(OUTSIDE_WORLD_MSG));
        this.throwException((isStatic && !this.isStaticBodyAllowed(shape, type)) 
                            || (!isStatic && !this.isDynamicBodyAllowed(shape, type)),
                                () -> new IllegalArgumentException(ILLEGAL_ENTITY_MSG));
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
        } else {
            this.physicalWorld = Optional.absent();
        }
    }
}
