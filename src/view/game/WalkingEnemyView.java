package view.game;

import org.apache.commons.lang3.tuple.Pair;

import javafx.scene.image.Image;
import javafx.util.Duration;
import model.entities.State;
import model.entities.WalkingEnemy;

/**
 * Models a view for the {@link WalkingEnemy}.
 */
public class WalkingEnemyView extends DynamicDrawableEntity {

    private static final Image WALKING = new Image("images/walkingEnemy_walking.png");
    private static final int WALKING_FRAMES = 2;
    private static final Image IDLE = new Image("images/walkingEnemy.png");
    private static final int IDLE_FRAMES = 1;
    private static final int WIDTH = 128;
    private static final int HEIGHT = 128;
    private static final int DURATION = 500;

    /**
     * @param walkingEnemy
     *            the {@link WalkingEnemy}
     * @param worldDimensions
     *            the dimensions of the {@link World}
     * @param sceneDimensions
     *            the dimensions of the view in which this {@link WalkingEnemy} will be drawn
     */
    public WalkingEnemyView(final WalkingEnemy walkingEnemy, final Pair<Double, Double> worldDimensions,
            final Pair<Double, Double> sceneDimensions) {
        super(IDLE, walkingEnemy, worldDimensions, sceneDimensions);
        this.mapAnimation(State.IDLE, new SpriteAnimation(IDLE, Duration.millis(DURATION), IDLE_FRAMES, WIDTH, HEIGHT));
        this.mapAnimation(State.MOVING_RIGHT, new SpriteAnimation(WALKING, Duration.millis(DURATION), WALKING_FRAMES, WIDTH, HEIGHT));
        this.mapAnimation(State.MOVING_LEFT, new SpriteAnimation(WALKING, Duration.millis(DURATION), WALKING_FRAMES, WIDTH, HEIGHT));
    }
}
