package controller.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import model.entities.EntityProperties;
import model.entities.EntityPropertiesImpl;
import model.entities.EntityShape;
import model.entities.EntityType;
import model.entities.Entity;
import model.world.World;
import model.world.WorldImpl;
import org.apache.commons.lang3.tuple.Pair;

import view.game.GameView;

/**
 * a {@link GameController} for a game set in a {@link World}.
 */
public class GameControllerImpl implements GameController {

    private static final long DELTA_UPDATE = 15;
    private static final String LEVEL_FILE = "res" + System.getProperty("file.separator") + "level1.lev";
    private static final int N_PROPERTIES = 7;

    private final World gameWorld;
    private final GameView gameView;
    private ScheduledThreadPoolExecutor timer;
    private boolean running;

    /**
     * builds a new {@link GameControllerImpl}.
     * @param view the {@link GameView} relative to the game controlled by this {@link GameController}
     */
    public GameControllerImpl(final GameView view) {
        this.gameWorld = new WorldImpl();
        this.gameWorld.initLevel(this.loadLevel());
        this.gameView = Objects.requireNonNull(view);
        this.timer = this.createTimer();
        this.running = false;
    }

    private ScheduledThreadPoolExecutor createTimer() {
        // Runtime.getRuntime().availableProcessors() + 1 is the size of the pool of threads
        final int threadPoolSize = Runtime.getRuntime().availableProcessors() + 1;
        final ScheduledThreadPoolExecutor t = new ScheduledThreadPoolExecutor(threadPoolSize);
        t.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        return t;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        if (!this.running) {
            this.timer.scheduleWithFixedDelay(() -> this.updateWorldAndView(), DELTA_UPDATE, DELTA_UPDATE, TimeUnit.MILLISECONDS);
            this.running = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void togglePauseGame() {
        if (this.running) {
            this.stopGame();
            /* prepares a new timer for when the game will be restarted */
            this.timer = this.createTimer();
        } else {
            this.startGame();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveGame() {
        throw new UnsupportedOperationException("Game saving functionalities have not been implemented yet"); 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopGame() {
        if (this.running) {
            this.timer.shutdown();
            this.running = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInput(final InputType input) {
        if (this.running) {
            this.gameWorld.movePlayer(input.getAssociatedMovementType());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentScore() {
        return this.gameWorld.getCurrentScore();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Entity> getAliveEntities() {
        return this.gameWorld.getAliveEntities();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Entity> getDeadEntities() {
        return this.gameWorld.getDeadEntities();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getWorldDimensions() {
        return this.gameWorld.getDimensions();
    }

    private void updateWorldAndView() {
        if (this.gameWorld.isGameOver()) {
            this.gameView.showGameOver();
            this.stopGame();
        } else if (this.gameWorld.hasPlayerWon()) {
            this.gameView.showPlayerWin();
            this.stopGame();
        } else {
            this.gameWorld.update();
            this.gameView.update();
        }
    }

    private List<EntityProperties> loadLevel() {
        final List<EntityProperties> entities = new LinkedList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(LEVEL_FILE))) {
            final int n = in.readInt();
            for (int i = 0; i < n; i++) {
                final Object obj = in.readObject();
                if (obj instanceof EntityProperties) {
                    entities.add((EntityProperties) obj);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return entities;
    }
}
