package view.game;

import javafx.scene.image.ImageView;

/**
 * an interface representing an {@link Entity} that can be drawn.
 */
public interface DrawableEntity {

    /**
     * updates the position of the {@link ImageView} of this {@link DrawableEntity}
     * so that it's up to date with the one of the {@link Entity}.
     */
    void updatePosition();

    /**
     * @return the {@link ImageView} representing the {@link Entity}
     */
    ImageView getImageView();

}
