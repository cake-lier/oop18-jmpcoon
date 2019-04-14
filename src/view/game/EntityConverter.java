package view.game;

import java.util.Collection;

import model.world.UnmodifiableEntity;

/**
 * Converter from {@link Entity} to {@link DrawableEntity}.
 */
public interface EntityConverter {

    /**
     * @param entity the {@link Entity} to convert
     * @return the converted {@link DrawableEntity}
     */
    DrawableEntity getDrawableEntity(UnmodifiableEntity entity);

    /**
     * 
     * @param deadEntities the entities to remove
     */
    void removeUnusedEntities(Collection<UnmodifiableEntity> deadEntities);
}
