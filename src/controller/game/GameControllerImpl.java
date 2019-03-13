package controller.game;

import java.util.Collection;

import controller.InputType;
import controller.app.AppController;
import view.game.DrawableEntity;
import view.game.GameView;

/**
 * 
 */
public class GameControllerImpl implements GameController {
    private final AppController appController;
    private final GameView gameView;
    
    public GameControllerImpl(final AppController appController, final GameView gameView) {
        this.appController=appController;
        this.gameView=gameView;
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

    @Override
    public void processInput(final InputType input) {
        // TODO Auto-generated method stub

    }
    /**
     * 
     */
    @Override
    public Collection<DrawableEntity> getDrawableEntities() {
        // TODO Auto-generated method stub
        return null;
    }

}
