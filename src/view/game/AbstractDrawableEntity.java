package view.game;

import javafx.scene.image.ImageView;
import model.Entity;
import model.StaticEntity;
import utils.Pair;

/**
 *  a {@link Entity} that can be drawn.
 */
public abstract class AbstractDrawableEntity implements DrawableEntity {

    private final ImageView sprite;
    private final Entity entity;
    private final Pair<Double, Double> ratios;

    /**
     * builds a new {@link StaticDrawableEntity}.
     * @param spriteUrl the url of the image representing entity in the view
     * @param entity the {@link StaticEntity} represented by this {@link StaticDrawableEntity}
     * @param ratios the x and y ratio used to convert the entity coordinates (that refer to the {@link World} dimensions) 
     * to the {@link DrawableEntity} coordinates (that refer t the view dimensions) 
     */
    public AbstractDrawableEntity(final String spriteUrl, final StaticEntity entity, final Pair<Double, Double> ratios) {
        // TODO: new ImageView() may not work
        this.sprite = new ImageView(spriteUrl);
        this.entity = entity;
        this.ratios = ratios;
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
     * @return the x and y ratio used to convert the entity coordinates (that refer to the {@link World} dimensions) 
     * to the {@link DrawableEntity} coordinates (that refer t the view dimensions) 
     */
    protected Pair<Double, Double> getRatios() {
        return this.ratios;
    }
}
