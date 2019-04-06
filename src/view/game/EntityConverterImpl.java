package view.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import model.entities.DynamicEntity;
import model.entities.Entity;
import model.entities.EntityType;
import model.entities.Player;
import model.entities.RollingEnemy;
import model.entities.StaticEntity;
import model.entities.WalkingEnemy;

import org.apache.commons.lang3.tuple.Pair;

/**
 * An implementation of {@link EntityConverter} that maintains the
 * {@link DrawableEntity} converted in the past, so that if requested again it
 * does not need to create them again.
 */
public class EntityConverterImpl implements EntityConverter {

    private static final String SPRITES_DIR = "images/";
    private static final String MODULE_LADDER_SPRITE_URL = SPRITES_DIR + "ladder.png";
    private static final String MODULE_PLATFORM_SPRITE_URL = SPRITES_DIR + "platform.png";

    private final Pair<Double, Double> worldDimensions;
    private final Pair<Double, Double> sceneDimensions;
    private final Map<EntityType, Image> images;
    private final Map<Entity, DrawableEntity> convertedEntities;

    /**
     * builds a new {@link EntityConverterImpl}.
     * 
     * @param worldDimensions
     *            the dimensions of the world in which the {@link Entity} to convert
     *            lives
     * @param sceneDimensions
     *            the dimensions of the scene in which the {@link DrawableEntity}
     *            produced will be put
     */
    public EntityConverterImpl(final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
        this.worldDimensions = worldDimensions;
        this.sceneDimensions = sceneDimensions;
        this.images = new HashMap<>();
        this.fillImagesMap();
        this.convertedEntities = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    public DrawableEntity getDrawableEntity(final Entity entity) {
        if (!this.convertedEntities.containsKey(entity)) {
            if (entity instanceof DynamicEntity) {
                if (entity.getType() == EntityType.PLAYER) {
                    return getAbstractDrawableEntity(entity, new PlayerView((Player) entity, this.worldDimensions, this.sceneDimensions));
                } else if (entity.getType() == EntityType.WALKING_ENEMY) {
                    return getAbstractDrawableEntity(entity, new WalkingEnemyView((WalkingEnemy) entity, this.worldDimensions, this.sceneDimensions));
                } else if (entity.getType() == EntityType.ROLLING_ENEMY) {
                    return getAbstractDrawableEntity(entity, new RollingEnemyView((RollingEnemy) entity, worldDimensions, sceneDimensions));
                }
            } else if (entity instanceof StaticEntity) {
                final Image image;
                if (entity.getType() == EntityType.LADDER) {
                    final Double timesPerModule = this.sceneDimensions.getLeft() * entity.getDimensions().getLeft()
                            / this.worldDimensions.getLeft();
                    image = replicateSprite(this.images.get(EntityType.LADDER), timesPerModule, false);
                } else if (entity.getType() == EntityType.PLATFORM) {
                    final Double timesPerModule = this.sceneDimensions.getRight() * entity.getDimensions().getRight()
                            / this.worldDimensions.getRight();
                    image = replicateSprite(this.images.get(EntityType.PLATFORM), timesPerModule, true);
                } else {
                    image = this.images.get(entity.getType());
                }
                return this.getAbstractDrawableEntity(entity, new StaticDrawableEntity(image, (StaticEntity) entity, this.worldDimensions,
                        this.sceneDimensions));
            } else {
                throw new IllegalArgumentException("Not supported entity");
            }
        }

        if (entity instanceof DynamicEntity) {
            final DynamicDrawableEntity dynamicDrawableEntity = (DynamicDrawableEntity) this.convertedEntities
                    .get(entity);
            dynamicDrawableEntity.updateSpritePosition();
        }
        return this.convertedEntities.get(entity);
    }

    /**
     * removes the {@link DrawableEntity}, saved converted in the past that are now
     * unused.
     * 
     * @param entities
     *            the {@link Entity} that will never be used in the future again
     */
    public void removeUnusedEntities(final Collection<Entity> entities) {
        entities.forEach(e -> this.convertedEntities.remove(e));
    }

    private AbstractDrawableEntity getAbstractDrawableEntity(final Entity entity, final AbstractDrawableEntity drawableEntity) {
        this.convertedEntities.put(entity, drawableEntity);
        return drawableEntity;
    }

    private void fillImagesMap() {
        this.images.put(EntityType.LADDER, loadImage(MODULE_LADDER_SPRITE_URL));
        this.images.put(EntityType.PLATFORM, loadImage(MODULE_PLATFORM_SPRITE_URL));
    }

    private Image loadImage(final String imageUrl) {
        return new Image(imageUrl);
    }

    /*
     * axis should be true to replicate a sprite along the x axis, and it should be
     * false to replicate it along the y axis
     */
    private Image replicateSprite(final Image module, final double timesPerModule, final boolean axis) {
        /* width and height of the sprite */
        final int width = ((Double) module.getWidth()).intValue();
        final int height = ((Double) module.getHeight()).intValue();
        final int times = ((Double) (timesPerModule / (axis ? width : height))).intValue() + 1;
        /* PixelReader to read pixel per pixel the module of the sprite */
        final PixelReader pixelReader = module.getPixelReader();
        final WritableImage image = new WritableImage(axis ? times * width : width, !axis ? times * height : height);
        /* PixelWriter to write pixel per pixel the sprite */
        final PixelWriter pixelWriter = image.getPixelWriter();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < times; k++) {
                    pixelWriter.setColor(axis ? i + k * width : i, !axis ? j + k * height : j,
                            pixelReader.getColor(i, j));
                }
            }
        }
        return image;
    }
}
