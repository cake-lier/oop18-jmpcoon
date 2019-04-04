package view.game;

import javafx.scene.image.ImageView;

/**
 * an interface representing an {@link Entity} that can be drawn.
 */
public interface DrawableEntity {

    /**
     * @return the {@link ImageView} representing the {@link Entity}
     */
    ImageView getImageView();

}
