package controller;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import model.Entity;
import model.MovementType;
import model.World;
import model.WorldImpl;
import utils.Pair;
import view.game.GameView;

/**
 * a {@link GameController} for a game set in a {@link World}.
 */
public class GameControllerImpl implements GameController {

    private static final long DELTA_UPDATE = 15;

    private final World gameWorld;
    private final GameView gameView;
    private Optional<ScheduledThreadPoolExecutor> timer;

    /**
     * builds a new {@link GameControllerImpl}.
     * @param view the {@link GameView} relative to the game controlled by this {@link GameController}
     */
    public GameControllerImpl(final GameView view) {
        this.gameWorld = new WorldImpl();
        this.gameView = Objects.requireNonNull(view);
        // TODO: could just initialize timer to empty, and then at first run() call it would be created,
        // but in previous tests that caused problems. So, consider leaving it like this or creating a private
        // function to recall every time necessary
        // Runtime.getRuntime().availableProcessors() + 1 is the size of the pool of threads 
        this.timer = Optional.of(new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() + 1));
        this.timer.get().setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        if (!this.timer.isPresent()) {
            // Runtime.getRuntime().availableProcessors() + 1 is the size of the pool of threads 
            this.timer = Optional.of(new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() + 1));
            this.timer.get().setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        }
        if (this.timer.isPresent()) {
            this.timer.get().scheduleWithFixedDelay(() -> updateWorldAndView(), DELTA_UPDATE, DELTA_UPDATE, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pauseGame() {
        if (this.timer.isPresent()) {
            this.timer.get().shutdown();
            this.timer = Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveGame() {
        throw new UnsupportedOperationException("Game saving functionalities have not been implemented yet"); 
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

    private void updateWorldAndView() {
        this.gameWorld.update();
        this.gameView.update();
    }

}
