package view.game;

import javafx.scene.media.AudioClip;

/**
 * An enumeration associating possible events in the game to sounds.
 */
public enum Sounds {
    /**
     * A sound associated to a jump action of the {@link Player}.
     */
    JUMP("jump"),
    /**
     * A sound associated to the death of a {@link RollingEnemy}.
     */
    ROLLING_DESTROY("rollDestroy"),
    /**
     * A sound associated to the death of a {@link RollingEnemy}.
     */
    WALKING_DESTROY("walkDestroy"),
    /**
     * A sound associated to the death of the {@link Player}.
     */
    PLAYER_DEATH("death"),
    /**
     * A sound associated to an invincible {@link Player}.
     */
    INVINCIBIITY("invincible"),
    /**
     * A sound associated to the {@link Player} getting a {@link PowerUp}.
     */
    POWER_UP_GOT("powerUp"),
    /**
     * A sound associated to the end of the game.
     */
    END_GAME("end");

    private static final String SOUNDS_PATH = "sounds/";
    private static final String SOUNDS_EXT = ".wav";

    private final AudioClip sound;

    Sounds(final String soundName) {
        this.sound = new AudioClip(ClassLoader.getSystemResource(SOUNDS_PATH + soundName + SOUNDS_EXT).toExternalForm());
    }

    /**
     * Returns the sound associated to this value.
     * @return the sound associated to this enum value
     */
    public AudioClip getSound() {
        return this.sound;
    }
}
