package view.game;

import java.util.Collection;

import model.entities.UnmodifiableEntity;

/**
 * An {@link EntityConverter} that maintains the {@link DrawableEntity} converted in the past, so that if requested again it
 * does not need to create them again.
 */
public interface MemoizedEntityConverter extends EntityConverter {
    /**
     * Removes the entities previously converted that were memorized but are now useless. 
     * @param deadEntities the entities to remove
     */
    void removeUnusedEntities(Collection<UnmodifiableEntity> deadEntities);
}
