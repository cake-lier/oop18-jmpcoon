package it.unibo.jmpcoon.view.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.util.Duration;

/**
 * A class that creates an animation. Ispired from the class taken from:
 * https://netopyr.com/2012/03/09/creating-a-sprite-animation-with-javafx/
 */
public class SpriteAnimation extends Transition {

    private final PixelReader pixelReader;
    private final List<Image> list;
    private Image image;
    private final int frame;
    private final int width;
    private final int height;
    private int lastIndex;

    /**
     * Creates a new {@link SpriteAnimation}.
     * @param image the sprite sheets of the animation
     * @param duration duration of the animation
     * @param frame number of frames of the animation
     * @param width width of a single frame
     * @param height height of a single frame
     */
    public SpriteAnimation(final Image image, final Duration duration, final int frame, final int width,
           final int height) {
        super();
        this.pixelReader = image.getPixelReader();
        this.frame = frame;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
        this.list = new ArrayList<>();
        this.createList();
        this.setImage();
    }

    /**
     * {@inheritDoc}
     */
    protected void interpolate(final double k) {
        final int index = Math.min((int) (k * this.frame), this.frame - 1);
        if (index != lastIndex) {
            lastIndex = index;
            this.image = this.list.get(lastIndex);
        }
    }

    /**
     * Returns the image to be displayed.
     * @return an image
     */
    public Image getImage() {
        return this.image;
    }

    private void createList() {
        IntStream.range(0, this.frame).forEach(i -> {
            final int x = i * this.width;
            this.list.add(this.writeImage(x, 0));
        });
    }

    private void setImage() {
        this.image = this.writeImage(0, 0);
    }

    private WritableImage writeImage(final int x, final int y) {
        return new WritableImage(this.pixelReader, x, y, this.width, this.height);
    }
}
