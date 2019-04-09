package view.game;

import model.entities.Entity;

/**
 * Converter from {@link Entity} to {@link DrawableEntity}.
 */
public interface EntityConverter {

    /**
     * @param entity The {@link Entity} to convert.
     * @return The converted {@link DrawableEntity}.
     * @throws IllegalArgumentException If the given {@link Entity} is not supported.
     */
    DrawableEntity getDrawableEntity(Entity entity) throws IllegalArgumentException;

}
