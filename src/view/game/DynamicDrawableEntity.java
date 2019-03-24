package view.game;

import javafx.scene.image.Image;
import model.DynamicEntity;
import model.Entity;
import utils.Pair;
import utils.PairImpl;

/**
 * a {@link DynamicEntity} that can be drawn.
 */
public class DynamicDrawableEntity extends AbstractDrawableEntity {

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
     */
    public DynamicDrawableEntity(final String spriteUrl, final DynamicEntity entity,
            final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
        super(spriteUrl, entity, worldDimensions, sceneDimensions);
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
     */
    public DynamicDrawableEntity(final Image image, final DynamicEntity entity,
            final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
        super(image, entity, worldDimensions, sceneDimensions);
    }
 
    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateSpriteProperties() {
        final Entity entity = this.getEntity();
        final double x = entity.getPosition().getX();
        final double y = entity.getPosition().getY();
        final double width = entity.getDimensions().getX();
        final double height = entity.getDimensions().getY();

        this.getImageView().setScaleX(width * this.getXRatio() / this.getImageView().getImage().getWidth());
        this.getImageView().setScaleY(height * this.getYRatio() / this.getImageView().getImage().getHeight());
        this.getImageView().setRotate(-Math.toDegrees(this.getEntity().getAngle()));

        /* differences between the sizes of the ImageView and of the image really shown */
        final double diffX = this.getImageView().getImage().getWidth() - width * this.getXRatio();
        final double diffY = this.getImageView().getImage().getHeight() - height * this.getYRatio();
        final Pair<Double, Double> sceneCoordinates = this.getConvertedCoordinates(new PairImpl<>(x - width / 2, y + height / 2));
        this.getImageView().setX(sceneCoordinates.getX() - diffX / 2);
        this.getImageView().setY(sceneCoordinates.getY() - diffY / 2);
    }
}
