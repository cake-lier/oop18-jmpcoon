package it.unibo.jmpcoon.model.world;

/**
 * A factory for {@link World} so as to better control its instantiation. In fact, there could be only one instance of
 * {@link World} per game and the {@link it.unibo.jmpcoon.controller.game.GameController} can't use all of {@link World} methods, some are 
 * reserved for {@link it.unibo.jmpcoon.model.entities.Entity}s exclusive use only. This factory is intended for enforcing such rules. Because 
 * multiple games can be created, multiple {@link World}s can be created, but this should be done from different 
 * {@link it.unibo.jmpcoon.controller.game.GameController}s. For meeting those two different needs, for each factory, only one {@link World} can 
 * be created, but at the same time multiple factories can be created. It will be {@link it.unibo.jmpcoon.controller.game.GameController} 
 * responsibility to create no more than one {@link WorldFactory} for each game.
 */
public interface WorldFactory {
    /**
     * Creates a new instance of {@link World}. Only one instance will be produced, trying to call this method again will result
     * in an {@link IllegalStateException}. It needs a controller to be created so as to notify it when events occur.
     * @return the created {@link World}
     * @throws IllegalStateException if the {@link World} has already been created
     */
    UpdatableWorld create() throws IllegalStateException;
}
