package it.unibo.jmpcoon.view.game;

/**
 * An interface for classes that have to given the possibility to get the url of an image.
 */
public interface ImageUrlGetter {
    /**
     * Returns the url of the image associated to this {@link ImageUrlGetter}.
     * @return the url of the image
     */
    String getImageUrl();
}
