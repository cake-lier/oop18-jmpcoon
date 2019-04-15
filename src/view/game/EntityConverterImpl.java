package view.game;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import model.entities.EntityType;
import model.entities.UnmodifiableEntity;
import model.entities.EntityState;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * An implementation of {@link EntityConverter} that maintains the
 * {@link DrawableEntity} converted in the past, so that if requested again it
 * does not need to create them again.
 */
public class EntityConverterImpl implements EntityConverter {
    private static final String NOT_SUPPORTED_ENTITY_MSG = "This Entity is not supported";
    private static final String SPRITES_DIR = "images/";
    private static final String MODULE_LADDER_SPRITE_URL = SPRITES_DIR + "ladder.png";
    private static final String MODULE_PLATFORM_SPRITE_URL = SPRITES_DIR + "platform.png";
    /* if the sprite sheets are changed this section could be in need of changes */
    private static final String PLAYER_IDLE_SPRITE_URL = SPRITES_DIR + "raccoon_idle.png";
    private static final int PLAYER_IDLE_FRAMES = 1;
    private static final String PLAYER_CLIMBING_SPRITE_URL = SPRITES_DIR + "raccoon_climb.png";
    private static final int PLAYER_CLIMBING_FRAMES = 2;
    private static final String PLAYER_JUMPING_SPRITE_URL = SPRITES_DIR + "raccoon_jump.png";
    private static final int PLAYER_JUMPING_FRAMES = 6;
    private static final String PLAYER_WALKING_SPRITE_URL = SPRITES_DIR + "raccoon_walking.png";
    private static final int PLAYER_WALKING_FRAMES = 2;
    private static final String WALKING_ENEMY_WALKING_SPRITE_URL = SPRITES_DIR + "walkingEnemy_walking.png";
    private static final int WALKING_ENEMY_WALKING_FRAMES = 3;
    private static final String WALKING_ENEMY_IDLE_SPRITE_URL = SPRITES_DIR + "walkingEnemy_idle.png";
    private static final int WALKING_ENEMY_IDLE_FRAMES = 1;
    private static final String GOAL_SPRITE_URL = SPRITES_DIR + "goal.png";
    private static final String EXTRA_LIFE_SPRITE_URL = SPRITES_DIR + "extra_life.png";
    private static final String SUPER_STAR_URL = SPRITES_DIR + "super_star.png";
    private static final String ROLLING_ENEMY_SPRITE_URL = SPRITES_DIR + "rollingEnemy.png";
    private static final int ROLLING_ENEMY_MOVING_FRAMES = 1;
    private static final int ROLLING_ENEMY_IDLE_FRAMES = 1;
    private static final String ENEMY_GENERATOR_SPRITE_URL = SPRITES_DIR + "enemyGenerator.png";
    private static final double LADDER_RATIO = 0.5; // one ladder sprite is about 0.5m (height) in the world
    private static final double PLATFORM_RATIO = 0.9; // one platform sprite is about 0.9m (width) in the world

    private final Pair<Double, Double> worldDimensions;
    private final Pair<Double, Double> sceneDimensions;
    private final Map<UnmodifiableEntity, DrawableEntity> convertedEntities;
    private final Map<EntityType, Image> imagesForStaticEntities;
    private final Map<EntityType, Map<EntityState, Pair<Image, Integer>>> imagesForDynamicEntities;

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
    @Override
    public DrawableEntity getDrawableEntity(final UnmodifiableEntity entity) {
        if (!this.convertedEntities.containsKey(entity)) {
            if (!entity.isDynamic()) {
                final Image image;
                switch (entity.getType()) {
                    case LADDER:
                        image = replicateSprite(this.imagesForStaticEntities.get(EntityType.LADDER), 
                                entity.getDimensions().getRight() / LADDER_RATIO,
                                false);
                        break;
                    case PLATFORM:
                        image = replicateSprite(this.imagesForStaticEntities.get(EntityType.PLATFORM), 
                                entity.getDimensions().getLeft() / PLATFORM_RATIO,
                                true);
                        break;
                    case ENEMY_GENERATOR:
                        image = this.imagesForStaticEntities.get(entity.getType());
                        break;
                    case POWERUP:
                        image = this.getPowerUpImage(entity);
                        break;
                    default:
                        throw new IllegalArgumentException(NOT_SUPPORTED_ENTITY_MSG);
                }
                this.convertedEntities.put(entity, 
                        new StaticDrawableEntity(image, entity, this.worldDimensions, this.sceneDimensions));
            } else {
                final Map<EntityState, Pair<Image, Integer>> spritesheets = this.imagesForDynamicEntities.get(entity.getType());
                switch (entity.getType()) {
                    case PLAYER:
                        this.convertedEntities.put(entity, 
                                new PlayerView(spritesheets, entity, this.worldDimensions, this.sceneDimensions));
                        break;
                    case ROLLING_ENEMY:
                        this.convertedEntities.put(entity, new RollingEnemyView(spritesheets, entity, 
                                                                            this.worldDimensions, this.sceneDimensions));
                        break;
                    case WALKING_ENEMY:
                        this.convertedEntities.put(entity, new WalkingEnemyView(spritesheets, entity,
                                                                            this.worldDimensions, this.sceneDimensions));
                        break;
                    default:
                        throw new IllegalArgumentException(NOT_SUPPORTED_ENTITY_MSG);
                }
            }
        }
        final DrawableEntity drawableEntity = this.convertedEntities.get(entity);
        if (drawableEntity instanceof DynamicDrawableEntity) {
            ((DynamicDrawableEntity) drawableEntity).updateSpritePosition();
        }
        return drawableEntity;
    }

    private Image getPowerUpImage(final UnmodifiableEntity powerUp) {
        switch (powerUp.getPowerUpType().get()) {
            case GOAL: 
                return this.loadImage(GOAL_SPRITE_URL);
            case EXTRA_LIFE: 
                return this.loadImage(EXTRA_LIFE_SPRITE_URL);
            case SUPER_STAR: 
                return this.loadImage(SUPER_STAR_URL);
            default: 
                return null;
       }
    }

    /**
     * removes the {@link DrawableEntity}, saved converted in the past that are now
     * unused.
     * 
     * @param entities
     *            the {@link Entity} that will never be used in the future again
     */
    @Override
    public void removeUnusedEntities(final Collection<UnmodifiableEntity> entities) {
        entities.forEach(this.convertedEntities::remove);
    }

    private void fillImagesMaps() {
        this.imagesForStaticEntities.put(EntityType.LADDER, loadImage(MODULE_LADDER_SPRITE_URL));
        this.imagesForStaticEntities.put(EntityType.PLATFORM, loadImage(MODULE_PLATFORM_SPRITE_URL));
        this.imagesForStaticEntities.put(EntityType.ENEMY_GENERATOR, loadImage(ENEMY_GENERATOR_SPRITE_URL));
        /* player images */
        final Map<EntityState, Pair<Image, Integer>> playerImages = new EnumMap<>(EntityState.class);
        playerImages.put(EntityState.IDLE, new ImmutablePair<>(loadImage(PLAYER_IDLE_SPRITE_URL), PLAYER_IDLE_FRAMES));
        playerImages.put(EntityState.MOVING_RIGHT, new ImmutablePair<>(loadImage(PLAYER_WALKING_SPRITE_URL), PLAYER_WALKING_FRAMES));
        playerImages.put(EntityState.MOVING_LEFT, new ImmutablePair<>(loadImage(PLAYER_WALKING_SPRITE_URL), PLAYER_WALKING_FRAMES));
        playerImages.put(EntityState.CLIMBING_DOWN, 
                new ImmutablePair<>(loadImage(PLAYER_CLIMBING_SPRITE_URL), PLAYER_CLIMBING_FRAMES));
        playerImages.put(EntityState.CLIMBING_UP, new ImmutablePair<>(loadImage(PLAYER_CLIMBING_SPRITE_URL), PLAYER_CLIMBING_FRAMES));
        playerImages.put(EntityState.JUMPING, new ImmutablePair<>(loadImage(PLAYER_JUMPING_SPRITE_URL), PLAYER_JUMPING_FRAMES));
        this.imagesForDynamicEntities.put(EntityType.PLAYER, playerImages);
        /* walking enemies images */
        final Map<EntityState, Pair<Image, Integer>> walkingEnemyImages = new EnumMap<>(EntityState.class);
        walkingEnemyImages.put(EntityState.IDLE, new ImmutablePair<>(loadImage(WALKING_ENEMY_IDLE_SPRITE_URL), WALKING_ENEMY_IDLE_FRAMES));
        walkingEnemyImages.put(EntityState.MOVING_RIGHT, 
                new ImmutablePair<>(loadImage(WALKING_ENEMY_WALKING_SPRITE_URL), WALKING_ENEMY_WALKING_FRAMES));
        walkingEnemyImages.put(EntityState.MOVING_LEFT, 
                new ImmutablePair<>(loadImage(WALKING_ENEMY_WALKING_SPRITE_URL), WALKING_ENEMY_WALKING_FRAMES));
        this.imagesForDynamicEntities.put(EntityType.WALKING_ENEMY, walkingEnemyImages);
        /* rolling enemies images */
        final Map<EntityState, Pair<Image, Integer>> rollingEnemyImages = new EnumMap<>(EntityState.class);
        rollingEnemyImages.put(EntityState.IDLE, 
                new ImmutablePair<>(loadImage(ROLLING_ENEMY_SPRITE_URL), ROLLING_ENEMY_IDLE_FRAMES));
        rollingEnemyImages.put(EntityState.MOVING_RIGHT, 
                new ImmutablePair<>(loadImage(ROLLING_ENEMY_SPRITE_URL), ROLLING_ENEMY_MOVING_FRAMES));
        rollingEnemyImages.put(EntityState.MOVING_LEFT, 
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
        final int nRepetitions = ((Double) timesPerModule).intValue() + 1;
        /* PixelReader to read pixel per pixel the module of the sprite */
        final PixelReader pixelReader = module.getPixelReader();
        final WritableImage image = new WritableImage(axis ? nRepetitions * width : width, !axis ? nRepetitions * height : height);
        /* PixelWriter to write pixel per pixel the sprite */
        final PixelWriter pixelWriter = image.getPixelWriter();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < nRepetitions; k++) {
                    pixelWriter.setColor(axis ? i + k * width : i, !axis ? j + k * height : j,
                            pixelReader.getColor(i, j));
                }
            }
        }
        return image;
    }
}
