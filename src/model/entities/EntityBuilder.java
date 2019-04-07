package model.entities;

import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import model.physics.DynamicPhysicalBody;
import model.physics.PhysicalFactory;
import model.physics.StaticPhysicalBody;

/**
 * A class used to create builders for building all types of {@link Entity}.
 * @param <E> the type of {@link Entity} to create, which should be a subclass of this type.
 */
public abstract class EntityBuilder<E extends Entity> {
    private Optional<Pair<Double, Double>> center;
    private Optional<Pair<Double, Double>> dimensions;
    private Optional<EntityShape> shape;
    private Optional<Double> angle;
    private Optional<PhysicalFactory> factory;
    private boolean built;

    /**
     * The default constructor. Initializes as empty all parameters of this builder.
     */
    protected EntityBuilder() {
        this.center = Optional.empty();
        this.dimensions = Optional.empty();
        this.shape = Optional.empty();
        this.angle = Optional.empty();
        this.factory = Optional.empty();
        this.built = false;
    }

    /**
     * Sets the position of the {@link Entity} that will be created by this {@link EntityBuilder}.
     * @param center the position of the {@link Entity} that will be created
     * @return a reference to this {@link EntityBuilder}
     */
    public EntityBuilder<E> setPosition(final Pair<Double, Double> center) {
        this.center = Optional.of(center);
        return this;
    }

    /**
     * Sets the dimensions of the {@link Entity} that will be created by this {@link EntityBuilder}.
     * @param dimensions the dimensions (width and height) of the {@link Entity} that will be created
     * @return a reference to this {@link EntityBuilder}
     */
    public EntityBuilder<E> setDimensions(final Pair<Double, Double> dimensions) {
        this.dimensions = Optional.of(dimensions);
        return this;
    }

    /**
     * Sets the {@link EntityShape} of the {@link Entity} that will be created by this {@link EntityBuilder}.
     * @param shape the {@link EntityShape} of the {@link Entity} that will be created
     * @return a reference to this {@link EntityBuilder}
     */
    public EntityBuilder<E> setShape(final EntityShape shape) {
        this.shape = Optional.of(shape);
        return this;
    }

    /**
     * Sets the angle of the {@link Entity} that will be created by this {@link EntityBuilder}.
     * @param angle the angle of the {@link Entity} that will be created
     * @return a reference to this {@link EntityBuilder}
     */
    public EntityBuilder<E> setAngle(final double angle) {
        this.angle = Optional.of(angle);
        return this;
    }

    /**
     * Sets the {@link PhysicalFactory} that will be used to create the {@link PhysicalBody} of the {@link Entity} that will be 
     * created by this {@link EntityBuilder}.
     * @param factory the {@link PhysicalFactory} that will be used to create the {@link PhysicalBody} of the {@link Entity} that
     *  will be created
     * @return a reference to this {@link EntityBuilder}
     */
    public EntityBuilder<E> setFactory(final PhysicalFactory factory) {
        this.factory = Optional.of(factory);
        return this;
    }

    /**
     * Builds the {@link Entity} with parameters as previously set. All the parameters are needed and, as a builder, once the
     * build has happened this builder won't produce any other copies of the produced {@link Entity}.
     * @return The {@link Entity} with parameters specified with the others methods.
     * @throws IllegalStateException if not every field has been initialized or if this {@link EntityBuilder} has already been
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

    private void checkIfbuildable() {
        if (!this.areAllFieldsFull()) {
            throw new IllegalStateException("Not all the fields have been initialized");
        }
        if (this.built) {
            throw new IllegalStateException("This builder has already been used");
        }
    }

    private boolean areAllFieldsFull() {
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
                                                            type);
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
