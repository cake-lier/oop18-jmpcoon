package view.game;

import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.entities.DynamicEntity;
import model.entities.Entity;
import model.entities.State;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * a {@link DynamicEntity} that can be drawn.
 */
public class DynamicDrawableEntity extends AbstractDrawableEntity {

    private final Map<State, SpriteAnimation> map = new HashMap<>();
    private Animation currentAnimation;
    private State lastState = State.IDLE;

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
     * {@inheritDoc}
     */
    @Override
    protected void updateSpritePosition() {
        this.changeAnimation(this.getEntity().getState());
        this.getImageView().setImage(this.map.get(this.getEntity().getState()).getImage());
        this.updateSpriteProperties();
        this.getImageView().setScaleX(this.getImageView().getScaleX() * direction());
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

    private int direction() {
        return this.lastState.equals(State.MOVING_LEFT) ? -1 : 1;
    }
}
