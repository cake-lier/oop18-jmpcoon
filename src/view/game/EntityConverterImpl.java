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
import model.entities.PowerUp;
import model.entities.PowerUpType;
import model.entities.StaticEntity;
import org.apache.commons.lang3.tuple.Pair;

/**
 * An implementation of {@link EntityConverter} that maintains the {@link DrawableEntity} converted in the past,
 * so that if requested again it does not need to create them again.
 */
public class EntityConverterImpl implements EntityConverter {

    private static final String SPRITES_DIR = "images/";
    private static final String MODULE_LADDER_SPRITE_URL = SPRITES_DIR + "ladder.png";
    private static final String MODULE_PLATFORM_SPRITE_URL = SPRITES_DIR + "platform.png";
    private static final String GOAL_SPRITE_URL = SPRITES_DIR + "goal.png";
    private static final String INVINCIBILITY_SPRITE_URL = SPRITES_DIR + "invincibility.png";
    private static final String EXTRA_LIFE_SPRITE_URL = SPRITES_DIR + "extra_life.png";
    private static final String PLAYER_SPRITE_URL = SPRITES_DIR + "raccoon.png";
    private static final String ROLLING_ENEMY_SPRITE_URL = SPRITES_DIR + "rollingEnemy.png";
    private static final String WALKING_ENEMY_SPRITE_URL = SPRITES_DIR + "walkingEnemy.png";

    private static final double LADDER_RATIO = 0.5; // one ladder sprite is about 0.5m (height) in the world
    private static final double PLATFORM_RATIO = 0.9; // one platform sprite is about 0.9m (width) in the world

    private final Pair<Double, Double> worldDimensions;
    private final Pair<Double, Double> sceneDimensions;
    private final Map<EntityType, Image> images;
    private final Map<Entity, DrawableEntity> convertedEntities;

    /**
     * builds a new {@link EntityConverterImpl}.
     * @param worldDimensions the dimensions of the world in which the {@link Entity} to convert lives
     * @param sceneDimensions the dimensions of the scene in which the {@link DrawableEntity} produced will be put
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
        if (this.convertedEntities.containsKey(entity)) {
            this.convertedEntities.get(entity).updatePosition();
            return this.convertedEntities.get(entity);
        } else {
            final DrawableEntity drawableEntity;
            if (entity instanceof DynamicEntity) {
                drawableEntity = new DynamicDrawableEntity(this.images.get(entity.getType()), (DynamicEntity) entity, this.worldDimensions, this.sceneDimensions);
            } else if (entity instanceof StaticEntity) {
                final Image image;
                if (entity.getType() == EntityType.LADDER) {
                    image = replicateSprite(this.images.get(EntityType.LADDER), 
                                            entity.getDimensions().getRight() / LADDER_RATIO,
                                            false);
                } else if (entity.getType() == EntityType.PLATFORM) {
                    image = replicateSprite(this.images.get(EntityType.PLATFORM), 
                                            entity.getDimensions().getLeft() / PLATFORM_RATIO, 
                                            true);
                } else {
                    image = this.getPowerUpImage((PowerUp) entity);
                }
                drawableEntity = new StaticDrawableEntity(image, (StaticEntity) entity, this.worldDimensions, this.sceneDimensions);
            } else {
                throw new IllegalArgumentException("Not supported entity");
            }
            this.convertedEntities.put(entity, drawableEntity);
            return drawableEntity;
        }
    }

    private Image getPowerUpImage(final PowerUp powerUp) {
        switch (powerUp.getPowerUpType()) {
            case GOAL: return this.loadImage(GOAL_SPRITE_URL);
            case INVINCIBILITY: return this.loadImage(INVINCIBILITY_SPRITE_URL);
            case EXTRA_LIFE: return this.loadImage(EXTRA_LIFE_SPRITE_URL);
            default: return null;
            }
    }

    /**
     * removes the {@link DrawableEntity}, saved  converted in the past that are now unused.
     * @param entities the {@link Entity} that will never be used in the future again
     */
    public void removeUnusedEntities(final Collection<Entity> entities) {
        entities.forEach(e -> this.convertedEntities.remove(e));
    }

    private void fillImagesMap() {
        this.images.put(EntityType.LADDER, loadImage(MODULE_LADDER_SPRITE_URL));
        this.images.put(EntityType.PLATFORM, loadImage(MODULE_PLATFORM_SPRITE_URL));
        this.images.put(EntityType.PLAYER, loadImage(PLAYER_SPRITE_URL));
        this.images.put(EntityType.ROLLING_ENEMY, loadImage(ROLLING_ENEMY_SPRITE_URL));
        this.images.put(EntityType.WALKING_ENEMY, loadImage(WALKING_ENEMY_SPRITE_URL));
    }

    private Image loadImage(final String imageUrl) {
        return new Image(imageUrl);
    }

    /* axis should be true to replicate a sprite along the x axis, and it should be false to replicate it along the y axis */
    private Image replicateSprite(final Image module, final double times, final boolean axis) {
        /* width and height of the sprite */
        final int width = ((Double) module.getWidth()).intValue();
        final int height = ((Double) module.getHeight()).intValue();
        final int nRepetitions = ((Double) times).intValue() + 1;
        /* PixelReader to read pixel per pixel the module of the sprite */
        final PixelReader pixelReader = module.getPixelReader();
        final WritableImage image = new WritableImage(axis ? nRepetitions * width : width, !axis ? nRepetitions * height : height);
        /* PixelWriter to write pixel per pixel the sprite */
        final PixelWriter pixelWriter = image.getPixelWriter();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < nRepetitions; k++) {
                    pixelWriter.setColor(axis ? i + k * width : i, !axis ? j + k * height : j, pixelReader.getColor(i, j));
                }
            }
        }
        return image;
    }
}
