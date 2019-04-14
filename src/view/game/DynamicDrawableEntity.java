package view.game;

import javafx.animation.Animation;
import javafx.scene.image.Image;
import model.entities.DynamicEntity;
import model.entities.State;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

/**
 * a {@link DynamicEntity} that can be drawn.
 */
public abstract class DynamicDrawableEntity extends AbstractDrawableEntity {

    private final Map<State, SpriteAnimation> map = new HashMap<>();
    private Animation currentAnimation;
    private int direction = 1;
    private State lastState = State.IDLE;
    private State currentState = State.IDLE;

    /**
     * builds a new {@link StaticDrawableEntity}.
     * 
     * @param image
     *            the image representing entity in the view
     * @param entity
     *            the {@link StaticEntity} represented by this {@link DynamicDrawableEntity}
     * @param worldDimensions
     *            the dimensions of the {@link World} in which the {@link Entity} lives
     * @param sceneDimensions
     *            the dimensions of the view in which this {@link DynamicDrawableEntity} will be drawn
     */
    public DynamicDrawableEntity(final Image image, final DynamicEntity entity,
            final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
        super(image, entity, worldDimensions, sceneDimensions);
    }


    /**
     * updates the image view.
     */
    public void updateSpritePosition() {
            super.updateSpriteProperties();
            this.changeAnimation(this.getEntity().getState());
            if (!checkClimb()) {
                this.getImageView().setImage(this.map.get(this.getEntity().getState()).getImage());
            }
            this.getImageView().setScaleX(this.getImageView().getScaleX() * this.direction);
    }

    /**
     * @param state the {@link State} to which is associated a {@link SpriteAnimation}
     * @param animation the {@link SpriteAnimation} istance to map
     */
    public void mapAnimation(final State state, final SpriteAnimation animation) {
        this.map.put(state, animation);
        if (state.equals(State.IDLE)) {
            this.currentAnimation = animation;
        }
    }

    private void changeAnimation(final State state) {
        if (!this.currentState.equals(state)) {
            this.direction = setDirection(state);
            this.currentAnimation.stop();
            this.currentAnimation = this.map.get(state);
            this.currentAnimation.setCycleCount(Animation.INDEFINITE);
            this.currentAnimation.play();
            this.lastState = this.currentState;
            this.currentState = state;
        }
    }

    private int setDirection(final State state) {
        return state.equals(State.MOVING_RIGHT) ? 1 : state.equals(State.MOVING_LEFT) ? -1 : this.direction;
    }

    private boolean checkClimb() {
        return this.currentState.equals(State.IDLE) && (this.lastState.equals(State.CLIMBING_UP) || this.lastState.equals(State.CLIMBING_DOWN));
    }
}
