package view.game;

import javafx.scene.image.Image;
import model.DynamicEntity;
import utils.Pair;

/**
 * a {@link DynamicEntity} that can be drawn.
 */
public class DynamicDrawableEntity extends AbstractDrawableEntity {

    private final SpriteAnimation animation;

    /**
     * builds a new {@link StaticDrawableEntity}.
     * 
     * @param spriteUrl
     *            the url of the image representing entity in the view
     * @param entity
     *            the {@link DynamicEntity} represented by this {@link DynamicDrawableEntity}
     * @param worldDimensions
     *            the dimensions of the {@link World} in which the {@link Entity} lives
     * @param sceneDimensions
     *            the dimensions of the view in which this {@link DynamicDrawableEntity} will be drawn
     * @param SpriteAnimation
     *            the animation associated with this {@link DynamicDrawableEntity}
     */
    public DynamicDrawableEntity(final String spriteUrl, final DynamicEntity entity,
            final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions, final SpriteAnimation animation) {
        super(spriteUrl, entity, worldDimensions, sceneDimensions);
        this.animation = animation;
    }

    /**
     * builds a new {@link StaticDrawableEntity}.
     * 
     * @param image
     *            the image representing entity in the view
     * @param entity
     *            the {@link StaticEntity} represented by this {@link DynamicDrawableEntity}
     * @param worldDimensions
     *            the dimensions of the {@link World} in which the {@link Entity} lives
     * @param sceneDimensions
     *            the dimensions of the view in which this {@link DynamicDrawableEntity} will be drawn
     * @param SpriteAnimation
     *            the animation associated with this {@link DynamicDrawableEntity}
     */
    public DynamicDrawableEntity(final Image image, final DynamicEntity entity,
            final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions, final SpriteAnimation animation) {
        super(image, entity, worldDimensions, sceneDimensions);
        this.animation = animation;
    }

    @Override
    protected void updateSpriteProperties() {
        
    }

}
