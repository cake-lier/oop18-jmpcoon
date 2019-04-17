package view.game;

import javafx.scene.media.AudioClip;

/**
 * 
 *
 */
public enum Sounds {
    JUMP("jump"),
    ROLLING_DESTROY("rollDestroy"),
    WALKING_DESTROY("walkDestroy"),
    PLAYER_DEATH("death"),
    INVINCIBIITY("invincible"),
    POWER_UP_GOT("powerUp"),
    END_GAME("end");

    private static final String SOUNDS_PATH = "sounds/";
    private static final String SOUNDS_EXT = ".wav";

    private final AudioClip sound;

    /**
     * 
     * @param soundName
     */
    private Sounds(final String soundName) {
        this.sound = new AudioClip(ClassLoader.getSystemResource(SOUNDS_PATH + soundName + SOUNDS_EXT).toExternalForm());
    }

    /**
     * 
     * @return
     */
    public AudioClip getSound() {
        return this.sound;
    }
}
