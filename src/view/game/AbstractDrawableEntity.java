package view.game;

import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Entity;
import model.StaticEntity;
import utils.Pair;
import utils.PairImpl;

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
     * @param spriteUrl the url of the image representing entity in the view
     * @param entity the {@link StaticEntity} represented by this {@link AbstractDrawableEntity}
     * @param worldDimensions the dimensions of the {@link World} in which the {@link Entity} lives
     * @param sceneDimensions the dimensions of the view in which this {@link AbstractDrawableEntity} will be drawn
     */
    public AbstractDrawableEntity(final String spriteUrl, 
                                    final StaticEntity entity, 
                                        final Pair<Double, Double> worldDimensions,
                                            final Pair<Double, Double> sceneDimensions) {
        // TODO: new Image() may not work
        this(new Image(spriteUrl), entity, worldDimensions, sceneDimensions);
    }

    /**
     * builds a new {@link AbstractDrawableEntity}.
     * @param image the {@link Image} representing the entity in the view
     * @param entity the {@link StaticEntity} represented by this {@link AbstractDrawableEntity}
     * @param worldDimensions the dimensions of the {@link World} in which the {@link Entity} lives
     * @param sceneDimensions the dimensions of the view in which this {@link AbstractDrawableEntity} will be drawn
     */
    public AbstractDrawableEntity(final Image image, 
                                    final StaticEntity entity, 
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
     * method used to update properties like position, rotation, ... of the {@link ImageView} of this {@link DrawableEntity}
     */
    protected abstract void updateSpriteProperties();

    /**
     * @return the {@link Entity} represented by this {@link DrawableEntity}
     */
    protected Entity getEntity() {
        return this.entity;
    }

    /**
     * @return the position of {@link Entity} represented converted to the reference system of the scene. 
     */
    protected Pair<Double, Double> getConvertedPosition() {
        return new PairImpl<Double, Double>(this.entity.getPosition().getX() * this.getXRatio(),
                this.sceneDimensions.getY() - this.entity.getPosition().getY() * this.getYRatio());
    }

    /**
     * @return the width of the {@link ImageView} representing the {@link Entity}
     */
    protected double getImageViewWidth() {
        return this.sprite.getImage().getWidth();
    }

    private double getXRatio() {
        return this.sceneDimensions.getX() / this.worldDimensions.getX();
    }

    private double getYRatio() {
        return this.sceneDimensions.getY() / this.worldDimensions.getY();
    }
}
