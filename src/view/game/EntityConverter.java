package view.game;

import model.entities.UnmodifiableEntity;

/**
 * Converter from {@link UnmodifiableEntity} to {@link DrawableEntity}.
 */
public interface EntityConverter {
    /**
     * Returns a {@link DrawableEntity} referring to the given {@link UnmodifiableEntity}.
     * @param entity the {@link UnmodifiableEntity} to convert
     * @return the converted {@link DrawableEntity}
     * @throws IllegalArgumentException if the given {@link UnmodifiableEntity} is not supported
     */
    DrawableEntity getDrawableEntity(UnmodifiableEntity entity) throws IllegalArgumentException;
}
