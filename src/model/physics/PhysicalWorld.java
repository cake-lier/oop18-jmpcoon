package model.physics;

/**
 * A convenience interface for manipulating objects with properties both of a {@link PhysicalWorld} and a
 * {@link ModifiablePhysicalWorld}.
 */
public interface PhysicalWorld extends UpdatablePhysicalWorld, ModifiablePhysicalWorld, ReadablePhysicalWorld {
}
