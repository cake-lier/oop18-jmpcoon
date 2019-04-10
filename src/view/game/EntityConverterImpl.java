package view.game;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import model.entities.Entity;
import model.entities.EntityType;
import model.entities.State;
import model.entities.Player;
import model.entities.RollingEnemy;
import model.entities.StaticEntity;
import model.entities.WalkingEnemy;

import org.apache.commons.lang3.tuple.ImmutablePair;
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
    private static final String PLAYER_IDLE_SPRITE_URL = SPRITES_DIR + "raccoon_idle.png";
    private static final int PLAYER_IDLE_FRAMES = 1;
    private static final String PLAYER_CLIMBING_SPRITE_URL = SPRITES_DIR + "raccoon_climb.png";
    private static final int PLAYER_CLIMBING_FRAMES = 2;
    private static final String PLAYER_JUMPING_SPRITE_URL = SPRITES_DIR + "raccoon_jump.png";
    private static final int PLAYER_JUMPING_FRAMES = 3;
    private static final String PLAYER_WALKING_SPRITE_URL = SPRITES_DIR + "raccoon_walking.png";
    private static final int PLAYER_WALKING_FRAMES = 8;
    private static final String WALKING_ENEMY_WALKING_SPRITE_URL = SPRITES_DIR + "walkingEnemy_walking.png";
    private static final int WALKING_ENEMY_WALKING_FRAMES = 2;
    private static final String WALKING_ENEMY_IDLE_SPRITE_URL = SPRITES_DIR + "walkingEnemy_idle.png";
    private static final int WALKING_ENEMY_IDLE_FRAMES = 1;
    private static final String ROLLING_ENEMY_SPRITE_URL = SPRITES_DIR + "rollingEnemy.png";
    private static final int ROLLING_ENEMY_MOVING_FRAMES = 2;
    private static final int ROLLING_ENEMY_IDLE_FRAMES = 1;

    private final Pair<Double, Double> worldDimensions;
    private final Pair<Double, Double> sceneDimensions;
    private final Map<EntityType, Image> imagesForStaticEntities;
    private final Map<EntityType, Map<State, Pair<Image, Integer>>> imagesForDynamicEntities;
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
        this.imagesForStaticEntities = new EnumMap<>(EntityType.class);
        this.imagesForDynamicEntities = new EnumMap<>(EntityType.class);
        this.fillImagesMaps();
        this.convertedEntities = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    public DrawableEntity getDrawableEntity(final Entity entity) {
        if (!this.convertedEntities.containsKey(entity)) {
            if (entity instanceof StaticEntity) {
                final Image image;
                if (entity.getType() == EntityType.LADDER) {
                    final Double timesPerModule = this.sceneDimensions.getLeft() * entity.getDimensions().getLeft()
                            / this.worldDimensions.getLeft();
                    image = replicateSprite(this.imagesForStaticEntities.get(EntityType.LADDER), timesPerModule, false);
                } else if (entity.getType() == EntityType.PLATFORM) {
                    final Double timesPerModule = this.sceneDimensions.getRight() * entity.getDimensions().getRight()
                            / this.worldDimensions.getRight();
                    image = replicateSprite(this.imagesForStaticEntities.get(EntityType.PLATFORM), timesPerModule, true);
                } else {
                    image = this.imagesForStaticEntities.get(entity.getType());
                }
                this.convertedEntities.put(entity, 
                        new StaticDrawableEntity(image, (StaticEntity) entity, this.worldDimensions, this.sceneDimensions));
            } else {
                final Map<State, Pair<Image, Integer>> spritesheets = this.imagesForDynamicEntities.get(entity.getType());
                switch (entity.getType()) {
                    case PLAYER:
                        this.convertedEntities.put(entity, 
                                new PlayerView(spritesheets, (Player) entity, this.worldDimensions, this.sceneDimensions));
                        break;
                    case ROLLING_ENEMY:
                        this.convertedEntities.put(entity, new RollingEnemyView(spritesheets, (RollingEnemy) entity, 
                                                                            this.worldDimensions, this.sceneDimensions));
                        break;
                    case WALKING_ENEMY:
                        this.convertedEntities.put(entity, new WalkingEnemyView(spritesheets, (WalkingEnemy) entity,
                                                                            this.worldDimensions, this.sceneDimensions));
                        break;
                    default:
                        throw new IllegalArgumentException("Not supported entity");
                }
            }
        }
        final DrawableEntity drawableEntity = this.convertedEntities.get(entity);
        if (drawableEntity instanceof DynamicDrawableEntity) {
            ((DynamicDrawableEntity) drawableEntity).updateSpritePosition();
        }
        return drawableEntity;
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

    private void fillImagesMaps() {
        this.imagesForStaticEntities.put(EntityType.LADDER, loadImage(MODULE_LADDER_SPRITE_URL));
        this.imagesForStaticEntities.put(EntityType.PLATFORM, loadImage(MODULE_PLATFORM_SPRITE_URL));
        /* player images */
        final Map<State, Pair<Image, Integer>> playerImages = new EnumMap<>(State.class);
        playerImages.put(State.IDLE, new ImmutablePair<>(loadImage(PLAYER_IDLE_SPRITE_URL), PLAYER_IDLE_FRAMES));
        playerImages.put(State.MOVING_RIGHT, new ImmutablePair<>(loadImage(PLAYER_WALKING_SPRITE_URL), PLAYER_WALKING_FRAMES));
        playerImages.put(State.MOVING_LEFT, new ImmutablePair<>(loadImage(PLAYER_WALKING_SPRITE_URL), PLAYER_WALKING_FRAMES));
        playerImages.put(State.CLIMBING_DOWN, 
                new ImmutablePair<>(loadImage(PLAYER_CLIMBING_SPRITE_URL), PLAYER_CLIMBING_FRAMES));
        playerImages.put(State.CLIMBING_UP, new ImmutablePair<>(loadImage(PLAYER_CLIMBING_SPRITE_URL), PLAYER_CLIMBING_FRAMES));
        playerImages.put(State.JUMPING, new ImmutablePair<>(loadImage(PLAYER_JUMPING_SPRITE_URL), PLAYER_JUMPING_FRAMES));
        this.imagesForDynamicEntities.put(EntityType.PLAYER, playerImages);
        /* walking enemies images */
        final Map<State, Pair<Image, Integer>> walkingEnemyImages = new EnumMap<>(State.class);
        walkingEnemyImages.put(State.IDLE, new ImmutablePair<>(loadImage(WALKING_ENEMY_IDLE_SPRITE_URL), WALKING_ENEMY_IDLE_FRAMES));
        walkingEnemyImages.put(State.MOVING_RIGHT, 
                new ImmutablePair<>(loadImage(WALKING_ENEMY_WALKING_SPRITE_URL), WALKING_ENEMY_WALKING_FRAMES));
        walkingEnemyImages.put(State.MOVING_LEFT, 
                new ImmutablePair<>(loadImage(WALKING_ENEMY_WALKING_SPRITE_URL), WALKING_ENEMY_WALKING_FRAMES));
        this.imagesForDynamicEntities.put(EntityType.WALKING_ENEMY, walkingEnemyImages);
        /* rolling enemies images */
        final Map<State, Pair<Image, Integer>> rollingEnemyImages = new EnumMap<>(State.class);
        rollingEnemyImages.put(State.IDLE, 
                new ImmutablePair<>(loadImage(ROLLING_ENEMY_SPRITE_URL), ROLLING_ENEMY_IDLE_FRAMES));
        rollingEnemyImages.put(State.MOVING_RIGHT, 
                new ImmutablePair<>(loadImage(ROLLING_ENEMY_SPRITE_URL), ROLLING_ENEMY_MOVING_FRAMES));
        rollingEnemyImages.put(State.MOVING_LEFT, 
                new ImmutablePair<>(loadImage(ROLLING_ENEMY_SPRITE_URL), ROLLING_ENEMY_MOVING_FRAMES));
        this.imagesForDynamicEntities.put(EntityType.ROLLING_ENEMY, rollingEnemyImages);
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
