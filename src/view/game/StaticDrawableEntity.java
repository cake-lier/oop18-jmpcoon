package view.game;

import javafx.scene.image.ImageView;
import model.Entity;
import model.StaticEntity;

/**
 * a {@link StaticEntity} that can be drawn.
 */
public class StaticDrawableEntity implements DrawableEntity {

    private final ImageView sprite;
    private final Entity entity;

    /**
     * builds a new {@link StaticDrawableEntity}.
     * @param spriteUrl the url of the image representing entity in the view
     * @param entity the {@link StaticEntity} represented by this {@link StaticDrawableEntity}
     */
    public StaticDrawableEntity(final String spriteUrl, final StaticEntity entity) {
        this.sprite = new ImageView(spriteUrl);
        this.entity = entity;
        this.setUpdatedImageViewProperties();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePosition() {
        this.setUpdatedImageViewProperties();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageView getImageView() {
        return this.sprite;
    }

    private void setUpdatedImageViewProperties() {
        this.sprite.setX(entity.getPosition().getX());
        this.sprite.setY(entity.getPosition().getY());
        this.sprite.setRotate(entity.getAngle());
    }

}
