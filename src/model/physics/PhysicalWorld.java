package model.physics;

/**
 * An interface representing a physical world in all its entirety, so as made of the methods of {@link ReadablePhysicalWorld},
 * of {@link ModifiablePhysicalWorld} and of {@link UpdatablePhysicalWorld}.
 */
public interface PhysicalWorld extends UpdatablePhysicalWorld, ModifiablePhysicalWorld, ReadablePhysicalWorld {
}
