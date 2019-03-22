package view.game;

import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Entity;
import model.StaticEntity;
import utils.Pair;
import utils.PairImpl;

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

    /**
     * {@inheritDoc}
     */
    protected void updateSpriteProperties() {
        final double entityWidth = this.getEntity().getDimensions().getX();
        final double entityHeight = this.getEntity().getDimensions().getY();
        final double entityX = this.getEntity().getPosition().getX();
        final double entityY = this.getEntity().getPosition().getY();
        /* scaling the ImaegView to correct dimensions */
        this.getImageView().setScaleX(entityWidth * this.getXRatio() / this.getImageView().getImage().getWidth());
        this.getImageView().setScaleY(entityHeight * this.getYRatio() / this.getImageView().getImage().getHeight());
        this.getImageView().setRotate(-Math.toDegrees(this.getEntity().getAngle()));
        /* differences between the sizes of the ImageView and of the image reallt shown */
        final double diffX = this.getImageView().getImage().getWidth() - entityWidth * this.getXRatio();
        final double diffY = this.getImageView().getImage().getHeight() - entityHeight * this.getYRatio();
        final Pair<Double, Double> sceneCoordinates = this.getConvertedCoordinates(new PairImpl<>(entityX - entityWidth / 2, entityY + entityHeight / 2));
        this.getImageView().setX(sceneCoordinates.getX() - diffX / 2);
        this.getImageView().setY(sceneCoordinates.getY() - diffY / 2);
    }

}
