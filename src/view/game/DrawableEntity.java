package view.game;

import javafx.scene.image.ImageView;
import model.entities.EntityType;

/**
 * an interface representing an {@link model.entities.Entity} that can be drawn.
 */
public interface DrawableEntity {

    /**
     * updates the position of the {@link ImageView} of this {@link DrawableEntity} so that it's up to date with the one of the
     * {@link model.entities.Entity}.
     */
    void updatePosition();

    /**
     * @return the {@link ImageView} representing the {@link model.entities.Entity}
     */
    ImageView getImageView();

    /**
     * @return the {@link EntityType} of this {@link model.entities.Entity}
     */
    EntityType getEntityType();
}
