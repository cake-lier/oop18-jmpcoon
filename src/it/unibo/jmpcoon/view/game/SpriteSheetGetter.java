package it.unibo.jmpcoon.view.game;

/**
 * An interface for classes that have to given the possibility to get the url of a sprite sheet and the relative
 * number of frames.
 */
public interface SpriteSheetGetter extends ImageUrlGetter {
    /**
     * Returns the number of frames contained in the sprite sheet associated.
     * @return the number of frames contained in the sprite sheet associated 
     */
    int getFramesNumber();
}
