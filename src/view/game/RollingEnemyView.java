package view.game;

import org.apache.commons.lang3.tuple.Pair;

import javafx.scene.image.Image;
import javafx.util.Duration;
import model.entities.DynamicEntity;
import model.entities.State;

/**
 * Models a view for the rolling enemy.
 */
public class RollingEnemyView extends DynamicDrawableEntity {

    private static final Image SPRITE = new Image("images/images/rollingEnemy.png");
    private static final int SPRITE_FRAMES = 2;
    private static final int WIDTH = 128;
    private static final int HEIGHT = 128;
    private static final int DURATION = 500;

    /**
     * @param entity
     *            the {@link RollingEnemy} entity
     * @param worldDimensions
     *            the dimensions of the {@link World}
     * @param sceneDimensions
     *            the dimensions of the view in which this {@link RollingEnemy} will be drawn
     */
    public RollingEnemyView(final DynamicEntity entity, final Pair<Double, Double> worldDimensions,
            final Pair<Double, Double> sceneDimensions) {
        super(SPRITE, entity, worldDimensions, sceneDimensions);
        this.mapAnimation(State.IDLE, new SpriteAnimation(SPRITE, Duration.millis(DURATION), SPRITE_FRAMES, WIDTH, HEIGHT));
        this.mapAnimation(State.MOVING_RIGHT, new SpriteAnimation(SPRITE, Duration.millis(DURATION), SPRITE_FRAMES, WIDTH, HEIGHT));
        this.mapAnimation(State.MOVING_LEFT, new SpriteAnimation(SPRITE, Duration.millis(DURATION), SPRITE_FRAMES, WIDTH, HEIGHT));
    }
}
