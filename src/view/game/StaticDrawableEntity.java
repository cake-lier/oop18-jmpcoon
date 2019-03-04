package view.game;

import model.StaticEntity;
import utils.Pair;

/**
 * a {@link StaticEntity} that can be drawn.
 */
public class StaticDrawableEntity extends AbstractDrawableEntity {

    /**
     * builds a new {@link StaticDrawableEntity}.
     * @param spriteUrl the url of the image representing entity in the view
     * @param entity the {@link StaticEntity} represented by this {@link StaticDrawableEntity}
     * @param ratios the x and y ratio used to convert the entity coordinates (that refer to the {@link World} dimensions) 
     * to the {@link DrawableEntity} coordinates (that refer t the view dimensions) 
     */
    public StaticDrawableEntity(final String spriteUrl, final StaticEntity entity, final Pair<Double, Double> ratios) {
        super(spriteUrl, entity, ratios);
    }

    /**
     * {@inheritDoc}
     */
    protected void updateSpriteProperties() {
        this.getImageView().setRotate(Math.toDegrees(this.getEntity().getAngle()));
        this.getImageView().setX(this.getConvertedX() 
                + 0.5 * this.getImageViewWidth() * (1 - Math.cos(this.getEntity().getAngle())));
        this.getImageView().setY(this.getConvertedY()
                + 0.5 * this.getImageViewWidth() * Math.sin(this.getEntity().getAngle()));
    }

    private double getConvertedX() {
        return this.getEntity().getPosition().getX() * this.getRatios().getX();
    }

    private double getConvertedY() {
        return this.getEntity().getPosition().getY() * this.getRatios().getY();
    }

    private double getImageViewWidth() {
        return this.getImageView().getImage().getWidth();
    }

}
