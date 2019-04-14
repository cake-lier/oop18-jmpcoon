package view.game;

import javafx.scene.image.Image;
import model.entities.UnmodifiableEntity;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * a {@link StaticEntity} that can be drawn.
 */
public class StaticDrawableEntity extends AbstractDrawableEntity {

    /**
     * builds a new {@link StaticDrawableEntity}.
     * @param image the image representing entity in the view
     * @param entity the {@link StaticEntity} represented by this {@link StaticDrawableEntity}
     * @param worldDimensions the dimensions of the {@link model.world.World} in which the {@link model.entities.Entity} lives
     * @param sceneDimensions the dimensions of the view in which this {@link StaticDrawableEntity} will be drawn
     */
    public StaticDrawableEntity(final Image image, 
                                    final UnmodifiableEntity entity, 
                                        final Pair<Double, Double> worldDimensions,
                                            final Pair<Double, Double> sceneDimensions) {
        super(image, entity, worldDimensions, sceneDimensions);
    }

    /**
     * {@inheritDoc}
     */
    protected void updateSpriteProperties() {
        final double entityWidth = this.getEntity().getDimensions().getLeft();
        final double entityHeight = this.getEntity().getDimensions().getRight();
        final double entityX = this.getEntity().getPosition().getLeft();
        final double entityY = this.getEntity().getPosition().getRight();
        /* scaling the ImageView to correct dimensions */
        this.getImageView().setScaleX(entityWidth * this.getXRatio() / this.getImageView().getImage().getWidth());
        this.getImageView().setScaleY(entityHeight * this.getYRatio() / this.getImageView().getImage().getHeight());
        this.getImageView().setRotate(-Math.toDegrees(this.getEntity().getAngle()));
        /* differences between the sizes of the ImageView and of the image really shown */
        final double diffX = this.getImageView().getImage().getWidth() - entityWidth * this.getXRatio();
        final double diffY = this.getImageView().getImage().getHeight() - entityHeight * this.getYRatio();
        final Pair<Double, Double> sceneCoordinates = this.getConvertedCoordinates(new ImmutablePair<>(entityX - entityWidth / 2, entityY + entityHeight / 2));
        this.getImageView().setX(sceneCoordinates.getLeft() - diffX / 2);
        this.getImageView().setY(sceneCoordinates.getRight() - diffY / 2);
    }

}
