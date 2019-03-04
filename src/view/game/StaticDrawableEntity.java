package view.game;

import model.StaticEntity;
import utils.Pair;

/**
 * a {@link StaticEntity} that can be drawn.
 */
public class StaticDrawableEntity extends AbstractDrawableEntity {

    /**
     * builds a new {@link StaticDrawableEntity}.
     * @param spriteUrl the url of the image representing entity in the view
     * @param entity the {@link StaticEntity} represented by this {@link StaticDrawableEntity}
     * @param worldDimensions the dimensions of the {@link World} in which the {@link Entity} lives
     * @param sceneDimensions the dimensions of the view in which this {@link StaticDrawableEntity} will be drawn
     */
    public StaticDrawableEntity(final String spriteUrl, 
                                    final StaticEntity entity, 
                                        final Pair<Double, Double> worldDimensions,
                                            final Pair<Double, Double> sceneDimensions) {
        super(spriteUrl, entity, worldDimensions, sceneDimensions);
    }

    /**
     * {@inheritDoc}
     */
    protected void updateSpriteProperties() {
        this.getImageView().setRotate(Math.toDegrees(this.getEntity().getAngle()));
        this.getImageView().setX(this.getConvertedPosition().getX()
                + 0.5 * this.getImageViewWidth() * (1 - Math.cos(this.getEntity().getAngle())));
        this.getImageView().setY(this.getConvertedPosition().getY()
                + 0.5 * this.getImageViewWidth() * Math.sin(this.getEntity().getAngle()));
    }

}
