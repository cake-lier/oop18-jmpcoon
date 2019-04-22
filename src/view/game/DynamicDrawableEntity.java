package view.game;

import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.util.Duration;
import model.entities.UnmodifiableEntity;
import model.entities.EntityState;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

/**
 * A {@link model.entities.DynamicEntity} that can be drawn.
 */
public class DynamicDrawableEntity extends AbstractDrawableEntity {
    private static final int DURATION = 550;
    private static final double PRECISION = 0.1;

    private final Map<EntityState, SpriteAnimation> map;
    private Animation currentAnimation;
    private boolean movingRight = true;
    private EntityState currentState;

    /**
     * Builds a new {@link DynamicDrawableEntity}.
     * @param spritesheets A map that matches the {@link EntityState} of the drawn {@link UnmodifiableEntity} to the sprite 
     * sheets that will represent it. The value of an entry is a pair with the sprite sheet and the number of frames it contains.
     * There must always be a sprite sheet for the {@link EntityState#IDLE}.
     * @param entity the {@link UnmodifiableEntity}
     * @param worldDimensions the dimensions of the {@link model.world.World}
     * @param sceneDimensions the dimensions of the view in which this {@link UnmodifiableEntity} will be drawn
     */
    public DynamicDrawableEntity(final Map<EntityState, Pair<Image, Integer>> spritesheets, final UnmodifiableEntity entity,
                                 final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
        super(spritesheets.get(EntityState.IDLE).getLeft(), entity, worldDimensions, sceneDimensions);
        this.map = new HashMap<>();
        this.currentState = EntityState.IDLE;
        final int height = ((Double) spritesheets.get(EntityState.IDLE).getLeft().getHeight()).intValue();
        spritesheets.entrySet().forEach(entry -> {
            final int width = ((Double) entry.getValue().getLeft().getWidth()).intValue() / entry.getValue().getRight();
            this.mapAnimation(entry.getKey(), new SpriteAnimation(entry.getValue().getLeft(), 
                                                                  Duration.millis(DURATION),
                                                                  entry.getValue().getRight(),
                                                                  width,
                                                                  height));
        });
    }

    /**
     * Updates the image view.
     */
    public void updateSpritePosition() {
            super.updateSpriteProperties();
            if (isIdleWhileClimbing()) {
                this.currentAnimation.pause();
            } else {
                this.changeAnimation(this.getEntity().getState());
                this.currentAnimation.play();
            }
            this.updateMovingRight();
            this.getImageView().setImage(this.map.get(this.getEntity().getState()).getImage());
            this.getImageView().setScaleX(this.getImageView().getScaleX() * (this.movingRight ? 1 : -1));
    }

    private void mapAnimation(final EntityState state, final SpriteAnimation animation) {
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

    private boolean isIdleWhileClimbing() {
        return ((this.currentState == EntityState.CLIMBING_UP || this.currentState == EntityState.CLIMBING_DOWN)
                && (Math.abs(this.getEntity().getVelocity().getLeft()) <=  PRECISION 
                    && Math.abs(this.getEntity().getVelocity().getRight()) <= PRECISION));
    }
}
