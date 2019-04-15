package view.game;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import javafx.scene.image.Image;
import javafx.util.Duration;
import model.entities.EntityState;
import model.entities.UnmodifiableEntity;

/**
 * Models a view for the {@link WalkingEnemy}.
 */
public class WalkingEnemyView extends DynamicDrawableEntity {

    private static final int DURATION = 500;

    /**
     * @param spritesheets A map that matches the {@link EntityState} of the drawn {@link Entity} to the spritesheets that will 
     * represent it. The value of an entry is a pair with the spritesheet and the number of frames it contains. There must 
     * always be a spritesheet for the {@link EntityState#IDLE}.
     * @param walkingEnemy The {@link model.entities.UnmodifiableEntity} that is a {@link WalkingEnemy}.
     * @param worldDimensions The dimensions of the {@link World}
     * @param sceneDimensions The dimensions of the view in which this {@link UnmodifiableEntity} will be drawn
     */
    public WalkingEnemyView(final Map<EntityState, Pair<Image, Integer>> spritesheets, final UnmodifiableEntity walkingEnemy,
            final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
        super(spritesheets.get(EntityState.IDLE).getLeft(), walkingEnemy, worldDimensions, sceneDimensions);
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
}
