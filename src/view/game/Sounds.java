package view.game;

import com.google.common.base.Optional;

import javafx.scene.media.AudioClip;
import model.world.CollisionEvent;

/**
 * An enumeration associating possible game events to their correlated sound.
 */
public enum Sounds {
    /**
     * A sound associated to a jump action of the {@link Player}.
     */
    JUMP("jump", Optional.absent()),
    /**
     * A sound associated to the death of a {@link RollingEnemy}.
     */
    ROLLING_DESTROY("rollDestroy", Optional.of(CollisionEvent.ROLLING_ENEMY_KILLED)),
    /**
     * A sound associated to the death of a {@link WalkingEnemy}.
     */
    WALKING_DESTROY("walkDestroy", Optional.of(CollisionEvent.WALKING_ENEMY_KILLED)),
    /**
     * A sound associated to the death of the {@link Player}.
     */
    PLAYER_DEATH("death", Optional.absent()),
    /**
     * A sound associated to the {@link Player} getting the {@link PowerUp} that makes invincible.
     */
    INVINCIBIITY("invincible", Optional.of(CollisionEvent.INVINCIBILITY_HIT)),
    /**
     * A sound associated to the {@link Player} getting a generic {@link PowerUp}.
     */
    POWER_UP_GOT("powerUp", Optional.of(CollisionEvent.POWER_UP_HIT)),
    /**
     * A sound associated to the {@link Player} winning and terminating the game.
     */
    END_GAME("end", Optional.absent());

    private static final String SOUNDS_PATH = "sounds/";
    private static final String SOUNDS_EXT = ".mp3";

    private final AudioClip sound;
    private final Optional<CollisionEvent> associatedEvent;

    Sounds(final String soundName, final Optional<CollisionEvent> associatedEvent) {
        this.sound = new AudioClip(ClassLoader.getSystemResource(SOUNDS_PATH + soundName + SOUNDS_EXT).toExternalForm());
        this.associatedEvent = associatedEvent;
    }

    /**
     * Gets the sound associated to the value of the enumeration.
     * @return an {@link AudioClip} of the sound associated with the enumeration value
     */
    public AudioClip getSound() {
        return this.sound;
    }

    /**
     * Gets the event which should trigger the playing of this sound, if present.
     * @return an {@link Optional} of the event which has this specific sound, if present, an {@link Optional#absent()}
     * otherwise
     */
    public Optional<CollisionEvent> getAssociatedEvent() {
        return this.associatedEvent;
    }
}
