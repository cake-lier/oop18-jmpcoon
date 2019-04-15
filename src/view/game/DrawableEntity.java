package view.game;

import javafx.scene.image.ImageView;
import model.entities.EntityType;

/**
 * an interface representing an {@link model.entities.Entity} that can be drawn.
 */
public interface DrawableEntity {

    /**
     * @return the {@link ImageView} representing the {@link model.entities.Entity}
     */
    ImageView getImageView();

    /**
     * @return the {@link EntityType} of this {@link model.entities.Entity}
     */
    EntityType getEntityType();
}
