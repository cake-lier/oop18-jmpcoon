package view.game;

import javafx.scene.Scene;

/**
 * an interface representing an {@link Entity} that can be drawn.
 */
public interface DrawableEntity {

    /**
     * draws the {@link DrawableEntity} in the given {@link Scene}.
     * @param scene the scene where the {@link DrawableEntity} will be drawn
     */
    void draw(Scene scene);

}
