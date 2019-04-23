package it.unibo.jmpcoon.view.game;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import it.unibo.jmpcoon.model.entities.EntityState;
import it.unibo.jmpcoon.model.entities.EntityType;
import it.unibo.jmpcoon.model.entities.PowerUpType;
import it.unibo.jmpcoon.model.entities.UnmodifiableEntity;

/**
 * An implementation of {@link MemoizedEntityConverter}.
 */
public class MemoizedEntityConverterImpl implements MemoizedEntityConverter {
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
     * builds a new {@link MemoizedEntityConverterImpl}.
     * @param worldDimensions the dimensions of the world in which the {@link it.unibo.jmpcoon.model.entities.Entity} to convert lives
     * @param sceneDimensions the dimensions of the scene in which the {@link DrawableEntity} produced will be put
     */
    public MemoizedEntityConverterImpl(final Pair<Double, Double> worldDimensions, final Pair<Double, Double> sceneDimensions) {
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
                if (entity.getType() == EntityType.POWERUP 
                        && entity.getPowerUpType().isPresent()
                        && this.imagesForPowerUps.containsKey(entity.getPowerUpType().get())) {
                    image = this.imagesForPowerUps.get(entity.getPowerUpType().get());
                } else if (this.imagesForStaticEntities.containsKey(entity.getType())) {
                    if (entity.getType() == EntityType.LADDER) {
                        image = replicateSprite(this.imagesForStaticEntities.get(EntityType.LADDER), 
                                                entity.getDimensions().getRight() / LADDER_RATIO,
                                                false);
                    } else if (entity.getType() == EntityType.PLATFORM) {
                        image = replicateSprite(this.imagesForStaticEntities.get(EntityType.PLATFORM), 
                                                entity.getDimensions().getLeft() / PLATFORM_RATIO,
                                                true);
                    } else {
                        image = this.imagesForStaticEntities.get(entity.getType());
                    }
                } else {
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
     * removes the {@link DrawableEntity}, saved converted in the past that are now unused.
     * @param entities the {@link it.unibo.jmpcoon.model.entities.Entity} that will never be used in the future again
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
        this.fillMap(this.imagesForStaticEntities, 
                       StaticEntityImage.values(), 
                       e -> e.getAssociatedEntityType(),
                       e -> this.loadImage(e.getImageUrl()));
    }

    private void fillPowerUpsMap() {
        this.fillMap(this.imagesForPowerUps, 
                PowerUpImage.values(), 
                e -> e.getAssociatedPowerUpType(),
                e -> this.loadImage(e.getImageUrl()));
    }

    private void fillDynamicEntitiesMap() {
        final Map<EntityState, Pair<Image, Integer>> playerImages = new EnumMap<>(EntityState.class);
        final Map<EntityState, Pair<Image, Integer>> walkingEnemyImages = new EnumMap<>(EntityState.class);
        final Map<EntityState, Pair<Image, Integer>> rollingEnemyImages = new EnumMap<>(EntityState.class);
        /* player images */
        this.fillMap(playerImages, 
                       PlayerImage.values(),
                       e -> e.getAssociatedEntityState(),
                       e -> this.createPair(e));
        /* walking enemies images */
        this.fillMap(walkingEnemyImages, 
                       WalkingEnemyImage.values(),
                       e -> e.getAssociatedEntityState(),
                       e -> this.createPair(e));
        /* rolling enemies images */
        this.fillMap(rollingEnemyImages, 
                       RollingEnemyImage.values(),
                       e -> e.getAssociatedEntityState(),
                       e -> this.createPair(e));
        this.imagesForDynamicEntities.put(EntityType.WALKING_ENEMY, walkingEnemyImages);
        this.imagesForDynamicEntities.put(EntityType.PLAYER, playerImages);
        this.imagesForDynamicEntities.put(EntityType.ROLLING_ENEMY, rollingEnemyImages);
    }

    /*
     * given a mapToFill and an array of values, fills the map using the functions given to generate the keys and 
     * the values starting from thevalues.
     */
    private <K, V, E> void fillMap(final Map<K, V> mapToFill, final E[] generators, 
                                                     final Function<E, K> keyGetter, final Function<E, V> valueGetter) {
        Arrays.asList(generators).forEach(e -> mapToFill.put(keyGetter.apply(e), valueGetter.apply(e)));
    }

    private <E extends SpriteSheetInformationGetter> Pair<Image, Integer> createPair(final E getter) {
        return new ImmutablePair<>(this.loadImage(getter.getImageUrl()), getter.getFramesNumber());
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
