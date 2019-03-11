package controller;

import java.util.Collection;

import model.Entity;
import model.MovementType;
import model.World;
import model.WorldImpl;
import utils.Pair;

/**
 * a {@link GameController} for a game set in a {@link World}.
 */
public class GameControllerImpl implements GameController {

    private final World gameWorld;

    /**
     * builds a new {@link GameControllerImpl}.
     */
    public GameControllerImpl() {
        this.gameWorld = new WorldImpl();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Entity> getEntities() {
        return this.gameWorld.getEntities();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getWorldDimensions() {
        return this.gameWorld.getDimensions();
    }

}
