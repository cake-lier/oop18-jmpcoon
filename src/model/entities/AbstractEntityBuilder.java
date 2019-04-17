package model.entities;

import org.apache.commons.lang3.tuple.Pair;

import model.physics.BodyShape;
import model.physics.DynamicPhysicalBody;
import model.physics.PhysicalFactory;
import model.physics.StaticPhysicalBody;
import model.world.World;

import com.google.common.base.Optional;

/**
 * A class used to create builders for building all types of {@link Entity}.
 * @param <E> the type of {@link Entity} to create, which should be a subclass of this type.
 */
public abstract class AbstractEntityBuilder<E extends Entity> {
    private static final String INCOMPLETE_BUILDER_MSG = "Not all the fields have been initialized";
    private static final String ALREADY_BUILT_MSG = "This builder has already been used";
    private Optional<Pair<Double, Double>> center;
    private Optional<Pair<Double, Double>> dimensions;
    private Optional<BodyShape> shape;
    private Optional<Double> angle;
    private Optional<PhysicalFactory> factory;
    private Optional<PowerUpType> powerUpType;
    private Optional<Double> walkingRange;
    private Optional<World> world;
    private boolean built;

    /**
     * The default constructor. Initializes as empty all parameters of this builder.
     */
    protected AbstractEntityBuilder() {
        this.center = Optional.absent();
        this.dimensions = Optional.absent();
        this.shape = Optional.absent();
        this.angle = Optional.absent();
        this.factory = Optional.absent();
        this.powerUpType = Optional.absent();
        this.walkingRange = Optional.absent();
        this.world = Optional.absent();
        this.built = false;
    }

    /**
     * Sets the position of the {@link Entity} that will be created by this {@link AbstractEntityBuilder}.
     * @param center the position of the {@link Entity} that will be created
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setPosition(final Pair<Double, Double> center) {
        this.center = Optional.of(center);
        return this;
    }

    /**
     * Sets the dimensions of the {@link Entity} that will be created by this {@link AbstractEntityBuilder}.
     * @param dimensions the dimensions (width and height) of the {@link Entity} that will be created
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setDimensions(final Pair<Double, Double> dimensions) {
        this.dimensions = Optional.of(dimensions);
        return this;
    }

    /**
     * Sets the {@link BodyShape} of the {@link Entity} that will be created by this {@link AbstractEntityBuilder}.
     * @param shape the {@link BodyShape} of the {@link Entity} that will be created
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setShape(final BodyShape shape) {
        this.shape = Optional.of(shape);
        return this;
    }

    /**
     * Sets the angle of the {@link Entity} that will be created by this {@link AbstractEntityBuilder}.
     * @param angle the angle of the {@link Entity} that will be created
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setAngle(final double angle) {
        this.angle = Optional.of(angle);
        return this;
    }

    /**
     * Sets the {@link PhysicalFactory} that will be used to create the {@link model.physics.PhysicalBody} of the {@link Entity}
     * that will be created by this {@link AbstractEntityBuilder}.
     * @param factory the {@link PhysicalFactory} that will be used to create the {@link model.physics.PhysicalBody} of the
     * {@link Entity} that will be created.
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setFactory(final PhysicalFactory factory) {
        this.factory = Optional.of(factory);
        return this;
    }

    /**
     * Sets the {@link PowerUpType} of the {@link Entity} that will be created by this {@link AbstractEntityBuilder}, if said Entity is 
     * a {@link PowerUp}.
     * @param powerUpType an {@link Optional} containing the {@link PowerUpType} of the {@link PowerUp} being built, an empty
     * {@link Optional} if a PowerUp isn't being built
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setPowerUpType(final Optional<PowerUpType> powerUpType) {
        this.powerUpType = powerUpType;
        return this;
    }

    /**
     * Sets the distance the {@link Entity} to be created by this {@link AbstractEntityBuilder} will walk across, if said Entity
     * is a {@link WalkingEnemy}.
     * @param walkingRange an {@link Optional} containing the distance the {@link WalkingEnemy} being built should walk across, 
     * an empty {@link Optional} if a WalkingEnemy isn't being built
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setWalkingRange(final Optional<Double> walkingRange) {
        this.walkingRange = walkingRange;
        return this;
    }

    /**
     * Sets the world the {@link Entity} to be created by this {@link AbstractEntityBuilder} will interact with, if said Entity
     * is a {@link EnemyGenerator}.
     * @param world an {@link Optional} containing the world the {@link EnemyGenerator} being built will interact with, 
     * an empty {@link Optional} if an EnemyGenerator isn't being built
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setWorld(final Optional<World> world) {
        this.world = world;
        return this;
    }

    /**
     * Builds the {@link Entity} with parameters as previously set. All the parameters are needed and, as a builder, once the
     * build has happened this builder won't produce any other copies of the produced {@link Entity}.
     * @return The {@link Entity} with parameters specified with the others methods.
     * @throws IllegalStateException if not every field has been initialized or if this {@link AbstractEntityBuilder} has already been
     * built.
     */
    public E build() throws IllegalStateException {
        this.checkIfbuildable();
        this.built = true;
        return this.buildEntity();
    }

    /**
     * The actual method the subclass of this class should implement to correctly create a new {@link Entity} of this type.
     * @return The {@link Entity} that should be returned by the {@link #build()} method.
     */
    protected abstract E buildEntity();

    /**
     * Method that allows the subclass of this class to get the {@link PowerUpType} set.
     * @return The {@link PowerUpType} set.
     * @throws IllegalStateException if the {@link PowerUpType} for this {@link AbstractEntityBuilder} has not been set.
     */
    protected PowerUpType getPowerUpType() throws IllegalStateException {
        if (this.powerUpType.isPresent()) {
            return this.powerUpType.get();
        } else {
            throw new IllegalStateException("Not all the necessary fields have been initialized");
        }
    }

    /**
     * Method that allows the subclass of this class to get the walking distance set.
     * @return the walking distance set
     * @throws IllegalStateException if the walking distance for this {@link AbstractEntityBuilder} has not been set.
     */
    protected double getWalkingRange() throws IllegalStateException {
        if (this.walkingRange.isPresent()) {
            return this.walkingRange.get();
        } else {
            throw new IllegalStateException("Not all the necessary fields have been initialized");
        }
    }

    /**
     * Method that allows the subclass of this class to get the {@link World} set.
     * @return the World set
     * @throws IllegalStateException if the world for this {@link AbstractEntityBuilder} has not been set.
     */
    protected World getWorld() throws IllegalStateException {
        if (this.world.isPresent()) {
            return this.world.get();
        } else {
            throw new IllegalStateException("Not all the necessary fields have been initialized");
        }
    }

    /**
     * Method that allows the subclass of this class to get the {@link PhysicalFactory} set.
     * @return the {@link PhysicalFactory} set
     * @throws IllegalStateException if the PhysicalFactory for this {@link AbstractEntityBuilder} has not been set.
     */
    protected PhysicalFactory getPhysicalFactory() throws IllegalStateException {
        if (this.factory.isPresent()) {
            return this.factory.get();
        } else {
            throw new IllegalStateException("Not all the necessary fields have been initialized");
        }
    }

    private void checkIfbuildable() {
        if (!this.areAllBasicFieldsFull()) {
            throw new IllegalStateException(INCOMPLETE_BUILDER_MSG);
        }
        if (this.built) {
            throw new IllegalStateException(ALREADY_BUILT_MSG);
        }
    }

    private boolean areAllBasicFieldsFull() {
        return this.center.isPresent()
                && this.dimensions.isPresent()
                && this.shape.isPresent()
                && this.angle.isPresent()
                && this.factory.isPresent();
    }

    /**
     * Creates a {@link StaticPhysicalBody} for this {@link Entity}.
     * @param type The {@link EntityType} of this {@link Entity}.
     * @return The {@link StaticPhysicalBody} that this {@link Entity} should contain.
     */
    protected StaticPhysicalBody createStaticPhysicalBody(final EntityType type) {
        return this.factory.get().createStaticPhysicalBody(this.center.get(), 
                                                            this.angle.get(), 
                                                            this.shape.get(), 
                                                            this.dimensions.get().getLeft(), 
                                                            this.dimensions.get().getRight(), 
                                                            type,
                                                            this.powerUpType);
    }

    /**
     * Creates a {@link DynamicPhysicalBody} for this {@link Entity}.
     * @param type The {@link EntityType} of this {@link Entity}.
     * @return The {@link DynamicPhysicalBody} that this {@link Entity} should contain.
     */
    protected DynamicPhysicalBody createDynamicPhysicalBody(final EntityType type) {
        return this.factory.get().createDynamicPhysicalBody(this.center.get(), 
                                                            this.angle.get(), 
                                                            this.shape.get(), 
                                                            this.dimensions.get().getLeft(), 
                                                            this.dimensions.get().getRight(), 
                                                            type);
    }
}
