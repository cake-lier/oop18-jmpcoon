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
        final double imgWidth = this.getImageView().getImage().getWidth();
        final double imgHeight = this.getImageView().getImage().getHeight();
        final double entitySceneWidth = this.getEntity().getDimensions().getX() * this.getXRatio();
        final double entitySceneHeight = this.getEntity().getDimensions().getY() * this.getYRatio();
        final double rotationAngle = this.getEntity().getAngle();
        /* scale the ImageView so that its Image has the correct dimensions */
        this.getImageView().setScaleX(entitySceneWidth / imgWidth);
        this.getImageView().setScaleY(entitySceneHeight / imgHeight);
        /* distances between the bounds of the ImageView and the borders of its Image */
        final double diffX = (imgWidth - entitySceneWidth) / 2;
        final double diffY = (imgHeight - entitySceneHeight) / 2;
        this.getImageView().setRotate(-Math.toDegrees(rotationAngle));
        /* translation to correct position after rotation and scaling */
        final double halfDiagonal = Math.sqrt(entitySceneWidth * entitySceneWidth + entitySceneHeight * entitySceneHeight) / 2;
        final double distance = 2 * halfDiagonal * Math.sin(Math.abs(rotationAngle) / 2);
        // TODO: control this calculation
        final double angle1 = (Math.PI - Math.abs(rotationAngle)) / 2;
        final double angle2 = Math.asin(entitySceneHeight / (2 * halfDiagonal));
        final double deltaX = distance * Math.sin(Math.PI / 2 - angle1 - angle2);
        final double deltaY = distance * Math.cos(Math.PI / 2 - angle1 - angle2);
        final Pair<Double, Double> coordinates = new PairImpl<>(this.getEntity().getPosition().getX() + deltaX - diffX, 
                                                                this.getEntity().getPosition().getY() - deltaY + diffY);
        final Pair<Double, Double> finalCoordinates = this.getConvertedCoordinates(coordinates);
        this.getImageView().setX(finalCoordinates.getX());
        this.getImageView().setY(finalCoordinates.getY());
    }

}
