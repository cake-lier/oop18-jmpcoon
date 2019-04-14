package view.game;

import javafx.scene.image.Image;
import model.world.UnmodifiableEntity;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * a {@link DynamicEntity} that can be drawn.
 */
public class DynamicDrawableEntity extends AbstractDrawableEntity {

    /**
     * builds a new {@link StaticDrawableEntity}.
     * 
     * @param image the image representing entity in the view
     * @param entity the {@link model.entities.StaticEntity} represented by this {@link DynamicDrawableEntity}
     * @param worldDimensions the dimensions of the {@link model.world.World} in which the {@link Entity} lives
     * @param sceneDimensions the dimensions of the view in which this {@link DynamicDrawableEntity} will be drawn
     */
    public DynamicDrawableEntity(final Image image, final UnmodifiableEntity entity,
            final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
        super(image, entity, worldDimensions, sceneDimensions);
    }
 
    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateSpriteProperties() {
        final UnmodifiableEntity entity = this.getEntity();
        final double x = entity.getPosition().getLeft();
        final double y = entity.getPosition().getRight();
        final double width = entity.getDimensions().getLeft();
        final double height = entity.getDimensions().getRight();

        this.getImageView().setScaleX(width * this.getXRatio() / this.getImageView().getImage().getWidth());
        this.getImageView().setScaleY(height * this.getYRatio() / this.getImageView().getImage().getHeight());
        this.getImageView().setRotate(-Math.toDegrees(this.getEntity().getAngle()));

        /* differences between the sizes of the ImageView and of the image really shown */
        final double diffX = this.getImageView().getImage().getWidth() - width * this.getXRatio();
        final double diffY = this.getImageView().getImage().getHeight() - height * this.getYRatio();
        final Pair<Double, Double> sceneCoordinates = this.getConvertedCoordinates(new ImmutablePair<>(x - width / 2, y + height / 2));
        this.getImageView().setX(sceneCoordinates.getLeft() - diffX / 2);
        this.getImageView().setY(sceneCoordinates.getRight() - diffY / 2);
    }
}
