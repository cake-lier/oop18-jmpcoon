package view.game;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import controller.InputType;
import controller.app.AppController;
import controller.game.GameController;
import controller.game.GameControllerImpl;

public class GameViewImpl implements GameView {
    private final GameController gameController;
    private final AppController appController;
    private final Stage stage;
    private final Scene scene;
    private final Group root;
    
    public GameViewImpl(final AppController appController, final Stage stage) {
        this.appController=appController;
        this.gameController=new GameControllerImpl(this.appController, this);
        this.stage=stage;
        root = new Group();
        this.scene=new Scene(root, stage.getHeight(), stage.getWidth(), Color.BLACK);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> getInput(key.getCode()));
        }
    

    @Override
    public void update() {
        // TODO Auto-generated method stub    
    }
    
    private void getInput(KeyCode key) {
        InputType it;
        switch(key) {
            case W: it=InputType.CLIMB; break;
            case A: it=InputType.LEFT; break;
            case D: it=InputType.RIGHT; break;
            case SPACE: it=InputType.UP; break;
            default: it=InputType.RIGHT; break;
        }
        gameController.processInput(it);
    }
}
