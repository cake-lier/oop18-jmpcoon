package view.game;

import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.entities.EntityType;
import model.world.UnmodifiableEntity;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *  a {@link Entity} that can be drawn.
 */
public abstract class AbstractDrawableEntity implements DrawableEntity {

    private final ImageView sprite;
    private final UnmodifiableEntity entity;
    private final Pair<Double, Double> worldDimensions;
    private final Pair<Double, Double> sceneDimensions;

    /**
     * builds a new {@link AbstractDrawableEntity}.
     * @param image the {@link Image} representing the entity in the view
     * @param entity the {@link AbstractEntity} represented by this {@link AbstractDrawableEntity}
     * @param worldDimensions the dimensions of the {@link model.world.World} in which the {@link Entity} lives
     * @param sceneDimensions the dimensions of the view in which this {@link AbstractDrawableEntity} will be drawn
     */
    public AbstractDrawableEntity(final Image image, 
                                    final UnmodifiableEntity entity, 
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
    public void updatePosition() {
        Platform.runLater(() -> this.updateSpriteProperties());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageView getImageView() {
        return this.sprite;
    }

    /**
     * method used to update properties like position, rotation, ... of the {@link ImageView} of this {@link DrawableEntity}
     */
    protected abstract void updateSpriteProperties();

    /**
     * @return the {@link UnmodifiableEntity} represented by this {@link DrawableEntity}
     */
    protected UnmodifiableEntity getEntity() {
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

    /**
     * {@inheritDoc}
     */
    public EntityType getEntityType() {
        return this.entity.getType();
    }
}
