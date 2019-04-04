package view.game;

import javafx.scene.image.Image;
import model.entities.StaticEntity;
import org.apache.commons.lang3.tuple.Pair;

/**
 * a {@link StaticEntity} that can be drawn.
 */
public class StaticDrawableEntity extends AbstractDrawableEntity {

    /**
     * builds a new {@link StaticDrawableEntity}.
     * @param image the image representing entity in the view
     * @param entity the {@link StaticEntity} represented by this {@link StaticDrawableEntity}
     * @param worldDimensions the dimensions of the {@link World} in which the {@link Entity} lives
     * @param sceneDimensions the dimensions of the view in which this {@link StaticDrawableEntity} will be drawn
     */
    public StaticDrawableEntity(final Image image, 
                                    final StaticEntity entity, 
                                        final Pair<Double, Double> worldDimensions,
                                            final Pair<Double, Double> sceneDimensions) {
        super(image, entity, worldDimensions, sceneDimensions);
    }
}
