package view.game;

import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.entities.Entity;
import model.entities.AbstractEntity;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *  a {@link Entity} that can be drawn.
 */
public abstract class AbstractDrawableEntity implements DrawableEntity {

    private final ImageView sprite;
    private final Entity entity;
    private final Pair<Double, Double> worldDimensions;
    private final Pair<Double, Double> sceneDimensions;

    /**
     * builds a new {@link AbstractDrawableEntity}.
     * @param image the {@link Image} representing the entity in the view
     * @param entity the {@link AbstractEntity} represented by this {@link AbstractDrawableEntity}
     * @param worldDimensions the dimensions of the {@link World} in which the {@link Entity} lives
     * @param sceneDimensions the dimensions of the view in which this {@link AbstractDrawableEntity} will be drawn
     */
    public AbstractDrawableEntity(final Image image, 
                                    final AbstractEntity entity, 
                                        final Pair<Double, Double> worldDimensions,
                                            final Pair<Double, Double> sceneDimensions) {
        this.sprite = new ImageView(Objects.requireNonNull(image));
        this.entity = Objects.requireNonNull(entity);
        this.worldDimensions = Objects.requireNonNull(worldDimensions);
        this.sceneDimensions = Objects.requireNonNull(sceneDimensions);
        this.updateSpriteProperties();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageView getImageView() {
        return this.sprite;
    }

    /**
     * updates properties like position, rotation, ... of the {@link ImageView} of this {@link DrawableEntity}
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

    /**
     * @return the {@link Entity} represented by this {@link DrawableEntity}
     */
    protected Entity getEntity() {
        return this.entity;
    }

    /**
     * converts the given world coordinates into scene coordinates.
     * @param worldCoordinates the coordinates to be converted
     * @return the converted coordinates 
     */
    protected Pair<Double, Double> getConvertedCoordinates(final Pair<Double, Double> worldCoordinates) {
        return new ImmutablePair<>(worldCoordinates.getLeft() * this.getXRatio(),
                this.sceneDimensions.getRight() - worldCoordinates.getRight() * this.getYRatio());
    }

    /**
     * @return the ratio to convert world dimensions to scene dimensions along the x axis
     */
    protected double getXRatio() {
        return this.sceneDimensions.getLeft() / this.worldDimensions.getLeft();
    }

    /**
     * @return the ratio to convert world dimensions to scene dimensions along the y axis
     */
    protected double getYRatio() {
        return this.sceneDimensions.getRight() / this.worldDimensions.getRight();
    }
}
