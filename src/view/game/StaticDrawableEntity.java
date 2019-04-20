package view.game;

import javafx.scene.image.Image;

import model.entities.UnmodifiableEntity;

import org.apache.commons.lang3.tuple.Pair;

/**
 * A {@link StaticEntity} that can be drawn.
 */
public class StaticDrawableEntity extends AbstractDrawableEntity {
    /**
     * Builds a new {@link StaticDrawableEntity}.
     * @param image the image representing the entity in the view
     * @param entity the {@link StaticEntity} represented by this {@link StaticDrawableEntity}
     * @param worldDimensions the dimensions of the {@link model.world.World} in which the {@link model.entities.Entity} 
     * lives
     * @param sceneDimensions the dimensions of the view in which this {@link StaticDrawableEntity} will be drawn
     */
    public StaticDrawableEntity(final Image image, final UnmodifiableEntity entity, 
                                final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
        super(image, entity, worldDimensions, sceneDimensions);
    }
}
