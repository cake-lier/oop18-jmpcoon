package view.game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import model.Entity;
import model.Ladder;
import model.Platform;
import model.Player;
import model.RollingEnemy;
import model.WalkingEnemy;
import utils.Pair;

/**
 * a class that converts {@link Entity} to {@link DrawableEntity}.
 */
public class EntityConverter {

    private static final String SPRITES_DIR = "res" + System.getProperty("file.separator");
    private static final String MODULE_LADDER_SPRITE = SPRITES_DIR + "ladder.png";
    private static final String MODULE_PLATFORM_SPRITE = SPRITES_DIR + "platform.png";
    private static final String MODULE_PLAYER_SPRITE = SPRITES_DIR + "raccoon.png";
    private static final String MODULE_ROLLING_ENEMY_SPRITE = SPRITES_DIR + "rollingEnemy.png";
    private static final String MODULE_WALKING_ENEMY_SPRITE = SPRITES_DIR + "walkingEnemy.png";

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
            case PLAYER:
                if (entity instanceof Player) {
                    return convertPlayer((Player) entity);
                } else {
                    throw new IllegalStateException("Impossible entity");
                }
            case ROLLING_ENEMY:
                if (entity instanceof RollingEnemy) {
                    return convertRollingEnemy((RollingEnemy) entity);
                } else {
                    throw new IllegalStateException("Impossible entity");
                }
            case WALKING_ENEMY: 
                if (entity instanceof WalkingEnemy) {
                    return convertWalkingEnemy((WalkingEnemy) entity);
                } else {
                    throw new IllegalStateException("Impossible entity");
                }
            // TODO: add other entities
            default:
                throw new IllegalStateException("Impossible entity");
        }
    }

    private DrawableEntity convertPlayer(final Player player) {
        return new DynamicDrawableEntity(new Image(MODULE_PLAYER_SPRITE), player, this.worldDimensions, this.sceneDimensions);
    }

    private DrawableEntity convertRollingEnemy(final RollingEnemy enemy) {
        return new DynamicDrawableEntity(new Image(MODULE_ROLLING_ENEMY_SPRITE), enemy, this.worldDimensions, this.sceneDimensions);
    }

    private DrawableEntity convertWalkingEnemy(final WalkingEnemy enemy) {
        return new DynamicDrawableEntity(new Image(MODULE_WALKING_ENEMY_SPRITE), enemy, this.worldDimensions, this.sceneDimensions);
    }

    private DrawableEntity convertLadder(final Ladder ladder) {
        /* how many times should the sprite be replicated multiplied for the sprite size,
         *  (along the y axis, because it's a ladder */
        final Double timesPerModule = this.sceneDimensions.getY() * ladder.getDimensions().getY() / this.worldDimensions.getY();
        try {
            return new StaticDrawableEntity(replicateSprite(MODULE_LADDER_SPRITE, timesPerModule, false), ladder, this.worldDimensions, this.sceneDimensions);
        } catch (FileNotFoundException e) {
            // TODO: what to do? this null should not be here
            return null;
        }
    }

    private DrawableEntity convertPlatform(final Platform platform) {
        /* how many times should the sprite be replicated multiplied for the sprite size,
         * (along the x axis, because it's a platform */
        final Double timesPerModule = this.sceneDimensions.getX() * platform.getDimensions().getX() / this.worldDimensions.getX();
        try {
            return new StaticDrawableEntity(replicateSprite(MODULE_PLATFORM_SPRITE, timesPerModule, true), platform, this.worldDimensions, this.sceneDimensions);
        } catch (FileNotFoundException e) {
            // TODO: what to do? this null should not be here
            return null;
        }
    }

    /* axis should be true to replicate a sprite along the x axis, and it should be false to replicate it along the y axis */
    private Image replicateSprite(final String moduleUrl, final double timesPerModule, final boolean axis) throws FileNotFoundException {
        final Image module = new Image(new FileInputStream(moduleUrl));
        /* width and height of the sprite */
        final int width = ((Double) module.getWidth()).intValue();
        final int height = ((Double) module.getHeight()).intValue();
        final int times = ((Double) (timesPerModule / (axis ? width : height))).intValue();
        /* PixelReader to read pixel per pixel the module of the sprite */
        final PixelReader pixelReader = module.getPixelReader();
        final WritableImage image = new WritableImage(axis ? times * width : width, !axis ? times * height : height);
        /* PixelWriter to write pixel per pixel the sprite */
        final PixelWriter pixelWriter = image.getPixelWriter();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < times; k++) {
                    pixelWriter.setColor(axis ? i + k * height : i, !axis ? j + k * height : j, pixelReader.getColor(i, j));
                }
            }
        }
        return image;
    }
}
