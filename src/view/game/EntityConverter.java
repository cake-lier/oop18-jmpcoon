package view.game;

import javafx.scene.image.ImageView;
import model.Entity;
import model.Ladder;
import model.Platform;
import utils.Pair;

/**
 * a class that converts {@link Entity} to {@link DrawableEntity}.
 */
public class EntityConverter {

    private static final String SPRITES_DIR = "res" + System.getProperty("file.separator");
    private static final String LADDER_SPRITE = SPRITES_DIR + "ladder.png";
    private static final String PLATFORM_SPRITE = SPRITES_DIR + "platform.png";

    private final Pair<Double, Double> worldDimensions;
    private final Pair<Double, Double> sceneDimensions;

    /**
     * builds a new {@link EntityConverter}.
     * @param worldDimensions the dimensions of the world in which the {@link Entity} to convert lives
     * @param sceneDimensions the dimensions of the scene in which the {@link DrawableEntity} produced will be put
     */
    public EntityConverter(final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
        this.worldDimensions = worldDimensions;
        this.sceneDimensions = sceneDimensions;
    }

    /**
     * @param entity the {@link Entity} to convert
     * @return the converted {@link DrawableEntity}
     */
    public DrawableEntity getDrawableEntity(final Entity entity) {
        switch (entity.getType()) {
            // TODO: repetitive code, correct it
            case LADDER:
                if (entity instanceof Ladder) {
                    return convertLadder((Ladder) entity);
                } else {
                    throw new IllegalStateException("Impossible entity");
                }
            case PLATFORM:
                if (entity instanceof Platform) {
                    return convertPlatform((Platform) entity);
                } else {
                    throw new IllegalStateException("Impossible entity");
                }
            // TODO: add other entities
            default:
                throw new IllegalStateException("Impossible entity");
        }
    }

    private DrawableEntity convertLadder(final Ladder ladder) {
        return new StaticDrawableEntity(LADDER_SPRITE, ladder, this.worldDimensions, this.sceneDimensions);
    }

    private DrawableEntity convertPlatform(final Platform platform) {
        return new StaticDrawableEntity(PLATFORM_SPRITE, platform, this.worldDimensions, this.sceneDimensions);
    }
}