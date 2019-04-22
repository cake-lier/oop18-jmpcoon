package it.unibo.jmpcoon.view.game;

import it.unibo.jmpcoon.model.entities.EntityType;
import javafx.scene.image.ImageView;

/**
 * An interface representing an {@link it.unibo.jmpcoon.model.entities.Entity} that can be drawn.
 */
public interface DrawableEntity {
    /**
     * Returns the {@link ImageView} representing the {@link it.unibo.jmpcoon.model.entities.Entity} contained.
     * @return the {@link ImageView} representing the {@link it.unibo.jmpcoon.model.entities.Entity}
     */
    ImageView getImageView();

    /**
     * Returns the {@link EntityType} of the {@link it.unibo.jmpcoon.model.entities.Entity} represented.
     * @return the {@link EntityType} of this {@link it.unibo.jmpcoon.model.entities.Entity}
     */
    EntityType getEntityType();
}
