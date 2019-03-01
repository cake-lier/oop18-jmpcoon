package controller;

import java.util.Collection;

import model.MovementType;
import model.World;
import view.DrawableEntity;

/**
 * a {@link GameController} for a game set in a {@link World}.
 */
public class GameControllerImpl implements GameController {

    private final World gameWorld;

    /**
     * builds a new {@link GameControllerImpl}.
     * @param gameWorld the {@link World} that will be controlled by this {@link GameControllerImpl}
     */
    public GameControllerImpl(final World gameWorld) {
        this.gameWorld = gameWorld;
    }

    @Override
    public void startGame() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pauseGame() {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveGame() {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopGame() {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInput(final InputType input) {
        this.gameWorld.movePlayer(inputToMovement(input));
    }

    @Override
    public Collection<DrawableEntity> getDrawableEntities() {
        // TODO Auto-generated method stub
        return null;
    }

    private MovementType inputToMovement(final InputType input) {
        switch (input) {
            case CLIMB:
                return MovementType.CLIMB;
            case LEFT:
                return MovementType.MOVE_LEFT;
            case RIGHT:
                return MovementType.MOVE_RIGHT;
            case UP:
                return MovementType.JUMP;
            default:
                throw new IllegalArgumentException();
        }
    }

}
