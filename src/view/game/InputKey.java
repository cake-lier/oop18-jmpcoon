package view.game;

import controller.game.InputType;

import com.google.common.base.Optional;

/**
 * The keyboard keys that control the {@link model.entities.Player}.
 */
public enum InputKey {
    /**
     * Player climbs up.
     */
    W(Optional.of(InputType.CLIMB_UP)),
    /**
     * Player goes left.
     */
    A(Optional.of(InputType.LEFT)),
    /**
     * Player goes right.
     */
    D(Optional.of(InputType.RIGHT)),
    /**
     * Player climbs down a {@link model.entities.Ladder}.
     */
    S(Optional.of(InputType.CLIMB_DOWN)),
    /**
     * Player climbs up a {@link model.entities.Ladder}.
     */
    SPACE(Optional.of(InputType.UP)),
    /**
     * Pause the game.
     */
    ESCAPE(Optional.absent());

    private final Optional<InputType> correspondingInputType;

    InputKey(final Optional<InputType> correspondingInputType) {
        this.correspondingInputType = correspondingInputType;
    }

    /**
     * Converts the {@link InputKey} on which it's called to a {@link InputType}.
     * @return the corresponding {@link InputType}
     */
    public Optional<InputType> convert() {
        return this.correspondingInputType;
    }
}
