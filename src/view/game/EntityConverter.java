package view.game;

import java.util.Collection;

import model.entities.UnmodifiableEntity;

/**
 * Converter from {@link Entity} to {@link DrawableEntity}.
 */
public interface EntityConverter {

    /**
     * @param entity The {@link Entity} to convert.
     * @return The converted {@link DrawableEntity}.
     * @throws IllegalArgumentException If the given {@link Entity} is not supported.
     */
    DrawableEntity getDrawableEntity(UnmodifiableEntity entity) throws IllegalArgumentException;

    /**
     * 
     * @param deadEntities the entities to remove
     */
    void removeUnusedEntities(Collection<UnmodifiableEntity> deadEntities);
}
