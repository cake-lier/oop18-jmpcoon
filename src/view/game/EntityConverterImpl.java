package view.game;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import model.entities.EntityType;
import model.entities.PowerUpType;
import model.entities.UnmodifiableEntity;
import model.entities.EntityState;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * An implementation of {@link MemoizedEntityConverter}.
 */
public class EntityConverterImpl implements MemoizedEntityConverter {
    private static final String NOT_SUPPORTED_ENTITY_MSG = "This Entity is not supported";
    private static final double LADDER_RATIO = 0.5; // one ladder sprite is about 0.5m (height) in the world
    private static final double PLATFORM_RATIO = 0.9; // one platform sprite is about 0.9m (width) in the world

    private final Pair<Double, Double> worldDimensions;
    private final Pair<Double, Double> sceneDimensions;
    private final Map<UnmodifiableEntity, DrawableEntity> convertedEntities;
    private final Map<EntityType, Image> imagesForStaticEntities;
    private final Map<EntityType, Map<EntityState, Pair<Image, Integer>>> imagesForDynamicEntities;
    private final Map<PowerUpType, Image> imagesForPowerUps;

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
        this.imagesForPowerUps = new EnumMap<>(PowerUpType.class);
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
                        image = this.imagesForPowerUps.get(entity.getPowerUpType().get());
                        break;
                    default:
                        throw new IllegalArgumentException(NOT_SUPPORTED_ENTITY_MSG);
                }
                this.convertedEntities.put(entity, 
                                           new StaticDrawableEntity(image, entity, this.worldDimensions, this.sceneDimensions));
            } else {
                if (this.imagesForDynamicEntities.containsKey(entity.getType())) {
                    this.convertedEntities.put(entity, 
                                               new DynamicDrawableEntity(this.imagesForDynamicEntities.get(entity.getType()), 
                                                                         entity,
                                                                         this.worldDimensions,
                                                                         this.sceneDimensions));
                } else {
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
        this.fillStaticEntitiesMap();
        this.fillPowerUpsMap();
        this.fillDynamicEntitiesMap();
    }

    private void fillStaticEntitiesMap() {
        this.fillMap(StaticEntityImage.values(), 
                     this.getMapFiller(this.imagesForStaticEntities, EntityType.class, s -> this.loadImage(s.getImageUrl())));
    }

    private void fillPowerUpsMap() {
        this.fillMap(PowerUpImage.values(), 
                     this.getMapFiller(this.imagesForPowerUps, PowerUpType.class, s -> this.loadImage(s.getImageUrl())));

    }

    private void fillDynamicEntitiesMap() {
        final Map<EntityState, Pair<Image, Integer>> playerImages = new EnumMap<>(EntityState.class);
        final Map<EntityState, Pair<Image, Integer>> walkingEnemyImages = new EnumMap<>(EntityState.class);
        final Map<EntityState, Pair<Image, Integer>> rollingEnemyImages = new EnumMap<>(EntityState.class);
        /* player images */
        this.fillMap(PlayerImage.values(), 
                     this.getMapFiller(playerImages, 
                                       EntityState.class, 
                                       s -> new ImmutablePair<>(this.loadImage(s.getImageUrl()), s.getFramesNumber())));
        /* walking enemies images */
        this.fillMap(WalkingEnemyImage.values(), 
                     this.getMapFiller(walkingEnemyImages, 
                                       EntityState.class, 
                                       s -> new ImmutablePair<>(this.loadImage(s.getImageUrl()), s.getFramesNumber())));
        /* rolling enemies images */
        this.fillMap(RollingEnemyImage.values(), 
                     this.getMapFiller(rollingEnemyImages, 
                                       EntityState.class, 
                                       s -> new ImmutablePair<>(this.loadImage(s.getImageUrl()), s.getFramesNumber())));
        this.imagesForDynamicEntities.put(EntityType.WALKING_ENEMY, walkingEnemyImages);
        this.imagesForDynamicEntities.put(EntityType.PLAYER, playerImages);
        this.imagesForDynamicEntities.put(EntityType.ROLLING_ENEMY, rollingEnemyImages);
    }

    /*
     * fills a map using as key generators the values of the array
     */
    private <V extends Enum<V>> void fillMap(final V[] keys, final Consumer<V> mapFiller) {
        Arrays.asList(keys).stream().forEach(mapFiller);
    }

    /*
     * given a map, it generates the key converting from enum E to enum K and it generates the value using the function
     */
    private <K extends Enum<K>, V, E extends Enum<E>> Consumer<E> getMapFiller(final Map<K, V> mapToFill, 
                                                                               final Class<K> keyClass,
                                                                               final Function<E, V> value) {
        return e -> mapToFill.put(Enum.valueOf(keyClass, e.toString()), value.apply(e));
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
        final WritableImage image = new WritableImage(axis ? nRepetitions * width : width, 
                                                      !axis ? nRepetitions * height : height);
        /* PixelWriter to write pixel per pixel the sprite */
        final PixelWriter pixelWriter = image.getPixelWriter();
        IntStream.range(0, width)
                 .forEach(i -> 
                          IntStream.range(0, height)
                                   .forEach(j -> 
                                            IntStream.range(0, nRepetitions)
                                                     .forEach(k -> 
                                                              pixelWriter.setColor(axis ? i + k * width : i, 
                                                                                   !axis ? j + k * height : j, 
                                                                                   pixelReader.getColor(i, j)))));
        return image;
    }
}
