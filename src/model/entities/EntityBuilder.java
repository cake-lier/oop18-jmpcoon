package model.entities;

import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import model.physics.DynamicPhysicalBody;
import model.physics.PhysicalFactory;
import model.physics.StaticPhysicalBody;

/**
 * A class used to create builders for building all types of {@link Entity}.
 */
public abstract class EntityBuilder {
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
    public EntityBuilder setPosition(final Pair<Double, Double> center) {
        this.center = Optional.of(center);
        return this;
    }

    /**
     * Sets the dimensions of the {@link Entity} that will be created by this {@link EntityBuilder}.
     * @param dimensions the dimensions (width and height) of the {@link Entity} that will be created
     * @return a reference to this {@link EntityBuilder}
     */
    public EntityBuilder setDimensions(final Pair<Double, Double> dimensions) {
        this.dimensions = Optional.of(dimensions);
        return this;
    }

    /**
     * Sets the {@link EntityShape} of the {@link Entity} that will be created by this {@link EntityBuilder}.
     * @param shape the {@link EntityShape} of the {@link Entity} that will be created
     * @return a reference to this {@link EntityBuilder}
     */
    public EntityBuilder setShape(final EntityShape shape) {
        this.shape = Optional.of(shape);
        return this;
    }

    /**
     * Sets the angle of the {@link Entity} that will be created by this {@link EntityBuilder}.
     * @param angle the angle of the {@link Entity} that will be created
     * @return a reference to this {@link EntityBuilder}
     */
    public EntityBuilder setAngle(final double angle) {
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
    public EntityBuilder setFactory(final PhysicalFactory factory) {
        this.factory = Optional.of(factory);
        return this;
    }

    // TODO: improve this comment
    /**
     * Builds the {@link Entity} with parameters as previously set. All the parameters are needed and, as a builder, once the
     * build has happened this builder won't produce any other copies of the produced {@link Entity}.
     * @return The {@link Entity} with parameters specified with the others methods.
     * @throws IllegalStateException if not every field has been initialized or if this {@link EntityBuilder} has already been
     *  built
     */
    // TODO: is this a template method?
    // TODO: is this the right exception to throw?
    public Entity build() throws IllegalStateException {
        this.checkIfbuildable();
        this.built = true;
        return this.buildEntity();
    }

    /**
     * @return the {@link Entity} returned by {@link #build()}.
     */
    protected abstract Entity buildEntity();

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
     * 
     * @param type
     * @return
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
     * 
     * @param type
     * @return
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
