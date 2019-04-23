package it.unibo.jmpcoon.controller.game;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import it.unibo.jmpcoon.controller.SaveFile;
import it.unibo.jmpcoon.model.entities.EntityProperties;
import it.unibo.jmpcoon.model.entities.MovementType;
import it.unibo.jmpcoon.model.entities.UnmodifiableEntity;
import it.unibo.jmpcoon.model.world.UpdatableWorld;
import it.unibo.jmpcoon.model.world.WorldFactoryImpl;
import it.unibo.jmpcoon.view.game.GameView;

/**
 * A {@link GameController} for a game set in a {@link it.unibo.jmpcoon.model.world.World}.
 */
public class GameControllerImpl implements GameController {
    private static final String INCOMPATIBLE_FILE_MSG = "The file read isn't compatible";
    private static final long DELTA_UPDATE = 15;
    private static final int JUMP_DELAY = 2;
    private static final URL LEVEL_FILE = ClassLoader.getSystemResource("level.lev");

    private UpdatableWorld gameWorld;
    private final GameView gameView;
    private ScheduledThreadPoolExecutor timer;
    private boolean running;
    private boolean jumped;
    private int timeFromLastJump;

    /**
     * Builds a new {@link GameControllerImpl}.
     * @param view the {@link GameView} relative to the game controlled by this {@link GameController}
     */
    public GameControllerImpl(final GameView view) {
        this.gameWorld = new WorldFactoryImpl().create();
        this.gameWorld.initLevel(this.loadLevel());
        this.gameView = Objects.requireNonNull(view);
        this.timer = this.createTimer();
        this.running = false;
        this.jumped = false;
        this.timeFromLastJump = JUMP_DELAY - 1;
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
    public void saveGame(final int saveFileIndex) throws FileNotFoundException, IOException {
        try (ObjectOutputStream out 
                 = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(SaveFile.values()[saveFileIndex]
                                                                                                .getSavePath())))) {
            out.writeObject(this.gameWorld);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadGame(final int saveFileIndex) throws IOException, IllegalArgumentException {
        try (ObjectInputStream in 
                 = new ObjectInputStream(new BufferedInputStream(new FileInputStream(SaveFile.values()[saveFileIndex]
                                                                                             .getSavePath())))) {
            this.gameWorld = (UpdatableWorld) in.readObject();
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(INCOMPATIBLE_FILE_MSG);
        }
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
    public int getCurrentScore() {
        return this.gameWorld.getCurrentScore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlayerLives() {
        return this.gameWorld.getPlayerLives();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<UnmodifiableEntity> getAliveEntities() {
        return this.gameWorld.getAliveEntities();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<UnmodifiableEntity> getDeadEntities() {
        return this.gameWorld.getDeadEntities();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getWorldDimensions() {
        return this.gameWorld.getDimensions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Queue<GameEvent> getCurrentEvents() {
        final Queue<GameEvent> events = new ConcurrentLinkedQueue<>();
        if (this.jumped && this.timeFromLastJump == 0) {
            events.offer(GameEvent.JUMP);
        }
        this.gameWorld.getCurrentEvents()
                      .forEach(event -> Arrays.asList(GameEvent.values())
                                              .stream()
                                              .filter(v -> v.getAssociatedCollisionEvent().isPresent())
                                              .forEach(v -> {
                                                  if (v.getAssociatedCollisionEvent().get() == event) {
                                                      events.offer(v);
                                                  }
                                              }));
        return events;
    }

    private ScheduledThreadPoolExecutor createTimer() {
        // Runtime.getRuntime().availableProcessors() + 1 is the size of the pool of threads
        final int threadPoolSize = Runtime.getRuntime().availableProcessors() + 1;
        final ScheduledThreadPoolExecutor t = new ScheduledThreadPoolExecutor(threadPoolSize);
        t.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        return t;
    }

    private void updateWorldAndView() {
        if (this.gameWorld.isGameOver()) {
            this.gameView.showGameOver();
            this.stopGame();
        } else if (this.gameWorld.hasPlayerWon()) {
            this.gameView.showPlayerWin();
            this.stopGame();
        } else {
            this.jumped = false;
            this.gameView.getInputs()
                         .stream()
                         .map(i -> i.getAssociatedMovementType())
                         .map(m -> new ImmutablePair<>(m, this.gameWorld.movePlayer(m)))
                         .filter(p -> p.getLeft() == MovementType.JUMP && p.getRight())
                         .forEach(b -> this.jumped = true);
            if (!this.jumped) {
                this.timeFromLastJump = JUMP_DELAY - 1;
            } else {
                this.timeFromLastJump = (this.timeFromLastJump + 1) % JUMP_DELAY;
            }
            this.gameWorld.update();
            this.gameView.update();
        }
    }

    private List<EntityProperties> loadLevel() {
        final List<EntityProperties> entities = new LinkedList<>();
        try (ObjectInputStream in = new ObjectInputStream(LEVEL_FILE.openStream())) {
            final int n = in.readInt();
            for (int i = 0; i < n; i++) {
                final Object obj = in.readObject();
                if (obj instanceof EntityProperties) {
                    entities.add((EntityProperties) obj);
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
        return entities;
    }
}
