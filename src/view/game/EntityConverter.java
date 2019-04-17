package view.game;

import model.entities.UnmodifiableEntity;

/**
 * Converter from {@link UnmodifiableEntity} to {@link DrawableEntity}.
 */
public interface EntityConverter {

    /**
     * @param entity The {@link UnmodifiableEntity} to convert.
     * @return The converted {@link DrawableEntity}.
     * @throws IllegalArgumentException If the given {@link Entity} is not supported.
     */
    DrawableEntity getDrawableEntity(UnmodifiableEntity entity) throws IllegalArgumentException;
}
