package view.game;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * a class that creates an animation.
 * taken from: https://netopyr.com/2012/03/09/creating-a-sprite-animation-with-javafx/
 */
public class SpriteAnimation extends Transition {

    private final ImageView imageView;
    private final int frame;
    private final int width;
    private final int height;

    private int lastIndex;

    /**
     * creates a new {@link SpriteAnimation}.
     * 
     * @param imageView
     *            image view
     * @param duration
     *            duration
     * @param frame
     *            number of frame of the animation
     * @param width
     *            width of the image
     * @param height
     *            height of the image
     */
    public SpriteAnimation(final ImageView imageView, final Duration duration, final int frame, final int width,
            final int height) {
        this.imageView = imageView;
        this.frame = frame;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    /**
     * {@inheritDoc}
     */
    protected void interpolate(final double k) {
        final int index = Math.min((int) (k * frame), frame - 1);
        if (index != lastIndex) {
            final int x = (index % frame) * width;
            imageView.setViewport(new Rectangle2D(x, 0, width, height));
            lastIndex = index;
        }
    }
}
