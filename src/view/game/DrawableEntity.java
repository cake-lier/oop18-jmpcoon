package view.game;

import javafx.scene.image.ImageView;
import model.entities.EntityType;

/**
 * An interface representing an {@link model.entities.Entity} that can be drawn.
 */
public interface DrawableEntity {
    /**
     * Returns the {@link ImageView} representing the {@link model.entities.Entity} contained.
     * @return the {@link ImageView} representing the {@link model.entities.Entity}
     */
    ImageView getImageView();

    /**
     * Returns the {@link EntityType} of the {@link model.entities.Entity} represented.
     * @return the {@link EntityType} of this {@link model.entities.Entity}
     */
    EntityType getEntityType();
}
