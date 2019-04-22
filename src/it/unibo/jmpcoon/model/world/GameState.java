package it.unibo.jmpcoon.model.world;

/**
 * An enum representing the current state of the game being played. It could be still going and the player can still try to reach
 * the end of the current level or the player has died and the game is over or the player reached the end of the level and has
 * won. The scope is package protected because is for the sole use of the {@link World}.
 */
enum GameState {
    /**
     * The state in which the game is still being played, the player didn't do anything to end it.
     */
    IS_GOING, 
    /**
     * The state in which the game is ended and the player didn't win the game.
     */
    GAME_OVER,
    /**
     * The state in which the game is ended and the player won the game.
     */
    PLAYER_WON;
}
