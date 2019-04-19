package view.game;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.util.Duration;

/**
 * A class that creates an animation. taken from:
 * https://netopyr.com/2012/03/09/creating-a-sprite-animation-with-javafx/
 */
public class SpriteAnimation extends Transition {

    private final PixelReader pixelReader;
    private final List<Image> list = new ArrayList<>();
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
        createList();
        setImage(image);
    }

    /**
     * {@inheritDoc}
     */
    protected void interpolate(final double k) {
        if (frame > 1) {
            final int index = Math.min((int) (k * frame), frame - 1);
            if (index != lastIndex) {
                lastIndex = index;
                image = this.list.get(lastIndex);
            }
        }
    }

    private void createList() {
        if (frame > 1) {
            for (int i = 0; i < frame; i++) {
                final int x = i * width;
                this.list.add(new WritableImage(this.pixelReader, x, 0, width, height));
            }
        }
    }

    private void setImage(final Image image) {
        if (frame == 1) {
            this.image = image;
        } else {
            this.image = new WritableImage(this.pixelReader, 0, 0, width, height);
        }
    }

    /**
     * 
     * @return image
     */
    public Image getImage() {
        return image;
    }
}
