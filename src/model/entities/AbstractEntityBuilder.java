package model.entities;

import org.apache.commons.lang3.tuple.Pair;

import model.physics.BodyShape;
import model.physics.DynamicPhysicalBody;
import model.physics.PhysicalFactory;
import model.physics.PlayerPhysicalBody;
import model.physics.StaticPhysicalBody;
import model.world.ModifiableWorld;

import com.google.common.base.Optional;

/**
 * A class representing a general builder for building all types of {@link model.entities.Entity}.
 * @param <E> the type of {@link model.entities.Entity} to create, which should be a subclass of {@link model.entities.Entity}
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
    private Optional<ModifiableWorld> world;
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
     * Sets the position of the {@link model.entities.Entity} that will be created by this {@link AbstractEntityBuilder}.
     * @param center the position of the {@link model.entities.Entity} that will be created
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setPosition(final Pair<Double, Double> center) {
        this.center = Optional.of(center);
        return this;
    }

    /**
     * Sets the dimensions of the {@link model.entities.Entity} that will be created by this {@link AbstractEntityBuilder}.
     * @param dimensions the dimensions (width and height) of the {@link model.entities.Entity} that will be created
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setDimensions(final Pair<Double, Double> dimensions) {
        this.dimensions = Optional.of(dimensions);
        return this;
    }

    /**
     * Sets the {@link BodyShape} of the {@link model.entities.Entity} that will be created by this 
     * {@link AbstractEntityBuilder}.
     * @param shape the {@link BodyShape} of the {@link model.entities.Entity} that will be created
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setShape(final BodyShape shape) {
        this.shape = Optional.of(shape);
        return this;
    }

    /**
     * Sets the angle of the {@link model.entities.Entity} that will be created by this {@link AbstractEntityBuilder}.
     * @param angle the angle of the {@link model.entities.Entity} that will be created
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setAngle(final double angle) {
        this.angle = Optional.of(angle);
        return this;
    }

    /**
     * Sets the {@link PhysicalFactory} that will be used to create the {@link model.physics.PhysicalBody} of the 
     * {@link model.entities.Entity} that will be created by this {@link AbstractEntityBuilder}.
     * @param factory the {@link PhysicalFactory} that will be used to create the {@link model.physics.PhysicalBody} of the
     * {@link model.entities.Entity} that will be created
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setFactory(final PhysicalFactory factory) {
        this.factory = Optional.of(factory);
        return this;
    }

    /**
     * Sets the {@link PowerUpType} of the {@link model.entities.Entity} that will be created by this 
     * {@link AbstractEntityBuilder}, if said Entity is a {@link PowerUp}.
     * @param powerUpType an {@link Optional} containing the {@link PowerUpType} of the {@link PowerUp} being built, an empty
     * {@link Optional} if a PowerUp isn't being built
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setPowerUpType(final Optional<PowerUpType> powerUpType) {
        this.powerUpType = powerUpType;
        return this;
    }

    /**
     * Sets the distance the {@link model.entities.Entity} to be created by this {@link AbstractEntityBuilder} will walk across, 
     * if said Entity is a {@link WalkingEnemy}.
     * @param walkingRange an {@link Optional} containing the distance the {@link WalkingEnemy} being built should walk across, 
     * an empty {@link Optional} if a WalkingEnemy isn't being built
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setWalkingRange(final Optional<Double> walkingRange) {
        this.walkingRange = walkingRange;
        return this;
    }

    /**
     * Sets the {@link World} the {@link model.entities.Entity} to be created by this {@link AbstractEntityBuilder} will notify
     * when it creates a {@link RollingEnemy}, if the entity is a {@link EnemyGenerator}.
     * @param world an {@link Optional} containing the world the {@link EnemyGenerator} will notify, 
     * an empty {@link Optional} if a WalkingEnemy isn't being built
     * @return a reference to this {@link AbstractEntityBuilder}
     */
    public AbstractEntityBuilder<E> setWorld(final Optional<ModifiableWorld> world) {
        this.world = world;
        return this;
    }

    /**
     * Builds the {@link model.entities.Entity} with the parameters previously set. All the parameters (except for walking range
     * and {@link PowerUpType} that must be set only for {@link WalkingEnemy} and {@link PowerUp} respectively) are needed and, 
     * as any builder, once the build has happened this builder won't produce any other copies of the produced 
     * {@link model.entities.Entity}.
     * @return the {@link model.entities.Entity} with parameters specified with the others methods
     * @throws IllegalStateException if not every necessary field has been initialized or if this {@link AbstractEntityBuilder}
     * has already been built
     */
    public E build() throws IllegalStateException {
        this.checkIfbuildable();
        this.built = true;
        return this.buildEntity();
    }

    /**
     * The actual method the subclass of this class should implement to correctly create a new {@link model.entities.Entity} 
     * of this type.
     * @return the {@link model.entities.Entity} that should be returned by the {@link #build()} method
     */
    protected abstract E buildEntity();

    /**
     * Method that allows the subclass of this class to get the {@link PowerUpType} set.
     * @return the {@link PowerUpType} set
     * @throws IllegalStateException if the {@link PowerUpType} for this {@link AbstractEntityBuilder} has not been set
     */
    protected PowerUpType getPowerUpType() throws IllegalStateException {
        return this.returnIfPresent(this.powerUpType);
    }

    /**
     * Method that allows the subclass of this class to get the walking distance set.
     * @return the walking distance set
     * @throws IllegalStateException if the walking distance for this {@link AbstractEntityBuilder} has not been set
     */
    protected double getWalkingRange() throws IllegalStateException {
        return this.returnIfPresent(this.walkingRange);
    }

    /**
     * Method that allows the subclass of this class to get the {@link PhysicalFactory} set.
     * @return the {@link PhysicalFactory} set
     * @throws IllegalStateException if the PhysicalFactory for this {@link AbstractEntityBuilder} has not been set
     */
    protected PhysicalFactory getPhysicalFactory() throws IllegalStateException {
        return this.returnIfPresent(this.factory);
    }

    /**
     * Method that allows the subclass of this class to get the {@link World} set.
     * @return the {@link World} set
     * @throws IllegalStateException if the World for this {@link AbstractEntityBuilder} has not been set
     */
    protected ModifiableWorld getWorld() throws IllegalStateException {
        return this.returnIfPresent(this.world);
    }

    private <O> O returnIfPresent(final Optional<O> optional) {
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new IllegalStateException(INCOMPLETE_BUILDER_MSG);
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
     * Creates a {@link StaticPhysicalBody} for the {@link model.entities.Entity} that will be created.
     * @param type the {@link EntityType} of the {@link model.entities.Entity} that will be created
     * @return the {@link StaticPhysicalBody} that the {@link model.entities.Entity} that will be created should contain
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
     * Creates a {@link DynamicPhysicalBody} for the {@link model.entities.Entity} that will be created.
     * @param type the {@link EntityType} of the {@link model.entities.Entity} that will be created
     * @return the {@link DynamicPhysicalBody} that the {@link model.entities.Entity} that will be created should contain
     */
    protected DynamicPhysicalBody createDynamicPhysicalBody(final EntityType type) {
        return this.factory.get().createDynamicPhysicalBody(this.center.get(), 
                                                            this.angle.get(), 
                                                            this.shape.get(), 
                                                            this.dimensions.get().getLeft(), 
                                                            this.dimensions.get().getRight(), 
                                                            type);
    }

    /**
     * Creates a {@link PlayerPhysicalBody} for the {@link model.entities.Entity} that will be created.
     * @return the {@link PlayerPhysicalBody} that the {@link model.entities.Entity} that will be created should contain
     */
    protected PlayerPhysicalBody createPlayerPhysicalBody() {
        return this.factory.get().createPlayerPhysicalBody(this.center.get(), 
                                                           this.angle.get(), 
                                                           this.shape.get(), 
                                                           this.dimensions.get().getLeft(), 
                                                           this.dimensions.get().getRight());
    }
}
