package view.game;

import javafx.scene.media.AudioClip;

/**
 * An enumeration associating possible game events to their correlated sound.
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
     * A sound associated to the death of a {@link WalkingEnemy}.
     */
    WALKING_DESTROY("walkDestroy"),
    /**
     * A sound associated to the death of the {@link Player}.
     */
    PLAYER_DEATH("death"),
    /**
     * A sound associated to the {@link Player} getting the {@link PowerUp} that makes invincible.
     */
    INVINCIBIITY("invincible"),
    /**
     * A sound associated to the {@link Player} getting a generic {@link PowerUp}.
     */
    POWER_UP_GOT("powerUp"),
    /**
     * A sound associated to the {@link Player} winning and terminating the game.
     */
    END_GAME("end");

    private static final String SOUNDS_PATH = "sounds/";
    private static final String SOUNDS_EXT = ".wav";

    private final AudioClip sound;

    Sounds(final String soundName) {
        this.sound = new AudioClip(ClassLoader.getSystemResource(SOUNDS_PATH + soundName + SOUNDS_EXT).toExternalForm());
    }

    /**
     * Gets the sound associated to the value of the enumeration.
     * @return an {@link AudioClip} of the sound associated with the enumeration value
     */
    public AudioClip getSound() {
        return this.sound;
    }
}
