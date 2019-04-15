package view.game;

import javafx.animation.Animation;
import javafx.scene.image.Image;
import model.entities.UnmodifiableEntity;
import model.entities.EntityState;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

/**
 * a {@link DynamicEntity} that can be drawn.
 */
public abstract class DynamicDrawableEntity extends AbstractDrawableEntity {

    private final Map<EntityState, SpriteAnimation> map = new HashMap<>();
    private Animation currentAnimation;
    private boolean movingRight = true;
    private EntityState lastState = EntityState.IDLE;
    private EntityState currentState = EntityState.IDLE;

    /**
     * builds a new {@link StaticDrawableEntity}.
     * 
     * @param image the image representing entity in the view
     * @param entity the {@link model.entities.StaticEntity} represented by this {@link DynamicDrawableEntity}
     * @param worldDimensions the dimensions of the {@link model.world.World} in which the {@link Entity} lives
     * @param sceneDimensions the dimensions of the view in which this {@link DynamicDrawableEntity} will be drawn
     */
    public DynamicDrawableEntity(final Image image, final UnmodifiableEntity entity,
            final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
        super(image, entity, worldDimensions, sceneDimensions);
    }


    /**
     * updates the image view.
     */
    public void updateSpritePosition() {
            super.updateSpriteProperties();
            this.changeAnimation(this.getEntity().getState());
            this.updateMovingRight();
            this.getImageView().setImage(this.map.get(this.getEntity().getState()).getImage());
            this.getImageView().setScaleX(this.getImageView().getScaleX() * (this.movingRight ? 1 : -1));
    }

    /**
     * @param state
     *            state
     * @param animation
     *            animation
     */
    public void mapAnimation(final EntityState state, final SpriteAnimation animation) {
        this.map.put(state, animation);
        if (state == EntityState.IDLE) {
            this.currentAnimation = animation;
        }
    }

    private void changeAnimation(final EntityState state) {
        if (!this.currentState.equals(state)) {
            this.currentAnimation.stop();
            this.currentAnimation = this.map.get(state);
            this.currentAnimation.setCycleCount(Animation.INDEFINITE);
            this.currentAnimation.play();
            this.lastState = this.currentState;
            this.currentState = state;
        }
    }

    private void updateMovingRight() {
        if (this.currentState == EntityState.MOVING_RIGHT) {
            this.movingRight = true;
        } else if (this.currentState == EntityState.MOVING_LEFT) {
            this.movingRight = false;
        }
    }

    private boolean checkClimb() {
        return this.currentState == EntityState.IDLE
               && (this.lastState == EntityState.CLIMBING_UP || this.lastState == EntityState.CLIMBING_DOWN);
    }
}
