package controller.game;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import controller.app.AppController;
import model.entities.EntityProperties;
import model.entities.EntityPropertiesImpl;
import model.entities.EntityShape;
import model.entities.EntityType;
import model.entities.Entity;
import model.entities.MovementType;
import model.world.World;
import model.world.WorldImpl;
import org.apache.commons.lang3.tuple.Pair;

import view.game.GameView;

/**
 * a {@link GameController} for a game set in a {@link World}.
 */
public class GameControllerImpl implements GameController {

    private static final long DELTA_UPDATE = 15;
    private static final String LEVEL_FILE = "res" + System.getProperty("file.separator") + "level1.txt";
    private static final int N_PROPERTIES = 7;

    private final World gameWorld;
    private final GameView gameView;
    private final AppController appController;
    private Optional<ScheduledThreadPoolExecutor> timer;

    /**
     * builds a new {@link GameControllerImpl}.
     * @param view the {@link GameView} relative to the game controlled by this {@link GameController}
     * @param appController the {@link AppController} relative to the app in which the game is shown
     */
    public GameControllerImpl(final GameView view, final AppController appController) {
        this.gameWorld = new WorldImpl();
        this.gameWorld.initLevel(loadLevel());
        this.gameView = Objects.requireNonNull(view);
        this.appController = Objects.requireNonNull(appController);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopGame() {
        this.pauseGame();
        this.appController.startApp();
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


    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<Double, Double> getWorldDimensions() {
        return this.gameWorld.getDimensions();
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

    private void updateWorldAndView() {
        if (this.gameWorld.isGameOver()) {
            this.gameView.showGameOver();
            this.pauseGame();
        } else if (this.gameWorld.hasPlayerWon()) {
            this.gameView.showPlayerWin();
            this.pauseGame();
        } else {
            this.gameWorld.update();
            this.gameView.update();
        }
    }

    private Collection<EntityProperties> loadLevel() {
        final List<EntityProperties> entities = new LinkedList<>();
        try {
            final List<String> lines = Files.readAllLines(Paths.get(LEVEL_FILE));
            lines.stream()
                 .filter(s -> !s.startsWith("%"))
                 .map(s -> s.split(":"))
                 .filter(v -> v.length == N_PROPERTIES)
                 .map(v -> new EntityPropertiesImpl(EntityType.valueOf(v[0]), 
                                                    EntityShape.valueOf(v[1]), 
                                                    Double.valueOf(v[2]), 
                                                    Double.valueOf(v[3]),
                                                    Double.valueOf(v[4]),
                                                    Double.valueOf(v[5]),
                                                    Double.valueOf(v[6])))
                 .forEach(entities::add);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return entities;
    }
}
