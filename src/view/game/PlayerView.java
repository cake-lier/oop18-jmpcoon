package view.game;

import org.apache.commons.lang3.tuple.Pair;
import javafx.scene.image.Image;
import javafx.util.Duration;
import model.entities.Player;
import model.entities.State;

/**
 * Models a view for the player.
 */
public class PlayerView extends DynamicDrawableEntity {

    private static final String SPRITES_DIR = "images/";
    private static final Image WALKING = new Image(SPRITES_DIR + "raccoon_walking.png");
    private static final Image IDLE = new Image(SPRITES_DIR + "raccoon_idle.png");
    private static final Image CLIMB = new Image(SPRITES_DIR + "raccoon_climb.png");
    private static final Image JUMP = new Image(SPRITES_DIR + "raccoon_jump.png");

    private static final int IDLE_FRAMES = 1;
    private static final int WALKING_FRAMES = 8;
    private static final int CLIMB_FRAMES = 2;
    private static final int JUMP_FRAMES = 3;

    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;
    private static final int DURATION = 700;

    /**
     * @param player
     *            the {@link Player}
     * @param worldDimensions
     *            the dimensions of the {@link World}
     * @param sceneDimensions
     *            the dimensions of the view in which this {@link Player} will be drawn
     */
    public PlayerView(final Player player,
            final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
        super(IDLE, player, worldDimensions, sceneDimensions);
        this.mapAnimation(State.IDLE, new SpriteAnimation(IDLE, Duration.millis(DURATION), IDLE_FRAMES, WIDTH, HEIGHT));
        this.mapAnimation(State.MOVING_RIGHT, new SpriteAnimation(WALKING, Duration.millis(DURATION), WALKING_FRAMES, WIDTH, HEIGHT));
        this.mapAnimation(State.MOVING_LEFT, new SpriteAnimation(WALKING, Duration.millis(DURATION), WALKING_FRAMES, WIDTH, HEIGHT));
        this.mapAnimation(State.CLIMBING_UP, new SpriteAnimation(CLIMB, Duration.millis(DURATION), CLIMB_FRAMES, WIDTH, HEIGHT));
        this.mapAnimation(State.CLIMBING_DOWN, new SpriteAnimation(CLIMB, Duration.millis(DURATION), CLIMB_FRAMES, WIDTH, HEIGHT));
        this.mapAnimation(State.JUMPING, new SpriteAnimation(JUMP, Duration.millis(DURATION), JUMP_FRAMES, WIDTH, HEIGHT));
    }
}
