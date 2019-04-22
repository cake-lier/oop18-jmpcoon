package it.unibo.jmpcoon.view.game;

import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import it.unibo.jmpcoon.model.entities.EntityType;
import it.unibo.jmpcoon.model.entities.UnmodifiableEntity;

/**
 * An implementation of {@link DrawableEntity}.
 */
public abstract class AbstractDrawableEntity implements DrawableEntity {
    private final ImageView sprite;
    private final UnmodifiableEntity entity;
    private final Pair<Double, Double> worldDimensions;
    private final Pair<Double, Double> sceneDimensions;

    /**
     * Builds a new {@link AbstractDrawableEntity}.
     * @param image the {@link Image} representing the entity in the view
     * @param entity the {@link UnmodifiableEntity} represented by this {@link AbstractDrawableEntity}
     * @param worldDimensions the dimensions of the {@link it.unibo.jmpcoon.model.world.World} in which the {@link UnmodifiableEntity} lives
     * @param sceneDimensions the dimensions of the view in which this {@link AbstractDrawableEntity} will be drawn
     */
    public AbstractDrawableEntity(final Image image, final UnmodifiableEntity entity, 
                                  final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
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
     * {@inheritDoc}
     */
    public EntityType getEntityType() {
        return this.entity.getType();
    }

    /**
     * Updates properties like position, rotation, ... of the {@link ImageView} of this {@link DrawableEntity}.
     */
    protected final void updateSpriteProperties() {
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
        final Pair<Double, Double> sceneCoordinates = 
                this.getConvertedCoordinates(new ImmutablePair<>(entityX - entityWidth / 2, entityY + entityHeight / 2));
        this.getImageView().setX(sceneCoordinates.getLeft() - diffX / 2);
        this.getImageView().setY(sceneCoordinates.getRight() - diffY / 2);
    }

    /**
     * Returns the {@link UnmodifiableEntity} represented by this {@link DrawableEntity}.
     * @return the {@link UnmodifiableEntity} represented by this {@link DrawableEntity}
     */
    protected UnmodifiableEntity getEntity() {
        return this.entity;
    }

    /**
     * Converts the given world coordinates into scene coordinates.
     * @param worldCoordinates the coordinates to be converted
     * @return the converted coordinates 
     */
    protected final Pair<Double, Double> getConvertedCoordinates(final Pair<Double, Double> worldCoordinates) {
        return new ImmutablePair<>(worldCoordinates.getLeft() * this.getXRatio(),
                this.sceneDimensions.getRight() - worldCoordinates.getRight() * this.getYRatio());
    }

    /**
     * Return the conversion ratio from world to scene dimensions along the x axis.
     * @return the ratio to convert world dimensions to scene dimensions along the x axis
     */
    protected final double getXRatio() {
        return this.sceneDimensions.getLeft() / this.worldDimensions.getLeft();
    }

    /**
     * Return the conversion ratio from world to scene dimensions along the y axis.
     * @return the ratio to convert world dimensions to scene dimensions along the y axis
     */
    protected final double getYRatio() {
        return this.sceneDimensions.getRight() / this.worldDimensions.getRight();
    }
}
