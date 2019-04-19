package view.game;

import model.entities.UnmodifiableEntity;

/**
 * Converter from {@link UnmodifiableEntity} to {@link DrawableEntity}.
 */
public interface EntityConverter {

    /**
     * @param entity The {@link UnmodifiableEntity} to convert.
     * @return the converted {@link DrawableEntity}
     * @throws IllegalArgumentException if the given {@link UnmodifiableEntity} is not supported
     */
    DrawableEntity getDrawableEntity(UnmodifiableEntity entity) throws IllegalArgumentException;
}
