package view.game;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import javafx.scene.image.Image;
import javafx.util.Duration;
import model.entities.State;
import model.entities.WalkingEnemy;

/**
 * Models a view for the {@link WalkingEnemy}.
 */
public class WalkingEnemyView extends DynamicDrawableEntity {

    private static final int DURATION = 500;

    /**
     * @param spritesheets A map that matches the {@link State} of the drawn {@link Entity} to the spritesheets that will 
     * represent it. The value of an entry is a pair with the spritesheet and the number of frames it contains. 
     * @param walkingEnemy The {@link WalkingEnemyView}.
     * @param worldDimensions The dimensions of the {@link World}
     * @param sceneDimensions The dimensions of the view in which this {@link Player} will be drawn
     */
    public WalkingEnemyView(final Map<State, Pair<Image, Integer>> spritesheets, final WalkingEnemy walkingEnemy,
            final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
        super(spritesheets.get(State.IDLE).getLeft(), walkingEnemy, worldDimensions, sceneDimensions);
        /* the sprites should be squares*/
        final int dimension = ((Double) spritesheets.get(State.IDLE).getLeft().getHeight()).intValue();
        spritesheets.entrySet().forEach(entry -> {
            this.mapAnimation(entry.getKey(), new SpriteAnimation(entry.getValue().getLeft(), 
                                                                    Duration.millis(DURATION),
                                                                    entry.getValue().getRight(),
                                                                    dimension,
                                                                    dimension));
        });
    }
}
