package view.game;

import model.entities.Entity;

/**
 * Converter from {@link Entity} to {@link DrawableEntity}.
 */
public interface EntityConverter {

    /**
     * @param entity the {@link Entity} to convert
     * @return the converted {@link DrawableEntity}
     */
    DrawableEntity getDrawableEntity(Entity entity);

}
