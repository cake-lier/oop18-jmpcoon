package view.game;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import model.entities.DynamicEntity;
import model.entities.Entity;
import model.entities.State;

public class PlayerView extends DynamicDrawableEntity {
    
    private static final Image WALKING = new Image("images/raccoon_walking.png");
    private static final int WALKING_FRAMES = 7;
    private static final Image IDLE = new Image("images/raccoon_idle.png");
    private static final int IDLE_FRAMES = 1;
    private static final int WIDTH = 128;
    private static final int HEIGHT = 128;
    private static final int DURATION = 700;

    private final Map<State, SpriteAnimation> map = new HashMap<>();
    private Animation currentAnimation;
    private State lastState = State.IDLE;

    /**
     */
    public PlayerView(final DynamicEntity entity,
            final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
        super(IDLE, entity, worldDimensions, sceneDimensions);
        this.mapAnimation(State.IDLE, new SpriteAnimation(IDLE, Duration.millis(DURATION), IDLE_FRAMES, WIDTH, HEIGHT));
        this.mapAnimation(State.MOVING_RIGHT, new SpriteAnimation(WALKING, Duration.millis(DURATION), WALKING_FRAMES, WIDTH, HEIGHT));
        this.mapAnimation(State.MOVING_LEFT, new SpriteAnimation(WALKING, Duration.millis(DURATION), WALKING_FRAMES, WIDTH, HEIGHT));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateSpriteProperties() {
        final Entity entity = this.getEntity();
        final ImageView imageView = this.getImageView();
        final double x = entity.getPosition().getLeft();
        final double y = entity.getPosition().getRight();
        final double width = entity.getDimensions().getLeft();
        final double height = entity.getDimensions().getRight();
        this.changeAnimation(this.getEntity().getState());
        this.getImageView().setImage(this.map.get(this.getEntity().getState()).getImage());
        this.getImageView().setScaleX((width * this.getXRatio() / this.getImageView().getImage().getWidth()));
        this.getImageView().setScaleY(height * this.getYRatio() / this.getImageView().getImage().getHeight());

        final double diffX = this.getImageView().getImage().getWidth() - width * this.getXRatio();
        final double diffY = this.getImageView().getImage().getHeight() - height * this.getYRatio();
        final Pair<Double, Double> sceneCoordinates = this
                .getConvertedCoordinates(new ImmutablePair<>(x - width / 2, y - height / 2));
        imageView.setX(sceneCoordinates.getLeft() - diffX / 2);
        imageView.setY(sceneCoordinates.getRight() - diffY);
    }

    /**
     * @param state
     *            state
     * @param animation
     *            animation
     */
    public void mapAnimation(final State state, final SpriteAnimation animation) {
        this.map.put(state, animation);
        if (state.equals(State.IDLE)) {
            this.currentAnimation = animation;
        }
    }

    private void changeAnimation(final State state) {
        if (!this.lastState.equals(state)) {
            this.currentAnimation.stop();
            this.currentAnimation = this.map.get(state);
            this.currentAnimation.setCycleCount(Animation.INDEFINITE);
            this.currentAnimation.play();
            this.lastState = state;
        }
    }

}
