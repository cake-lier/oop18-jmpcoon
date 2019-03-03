package view.menu;

import java.io.IOException;
import java.util.Optional;

import controller.app.AppController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The class implementation of the {@link Menu} interface.
 */
public final class MenuImpl implements Menu {
    private final AppController controller;
    private Optional<Stage> drawnMenu = Optional.empty();
    /**
     * Binds the menu view to the assigned controller, which is also the controller
     * of this application.
     * @param controller The application controller.
     */
    public MenuImpl(final AppController controller) {
        this.controller = controller;
    }
    /**
     * Wrapper method to delegate to the application controller the job of starting the game. 
     * This is made because every FXML file use only one controller and the application controller
     * couldn't be weigh down with other functionalities which are view's relevance.
     */
    @FXML
    private void startGame() {
        this.controller.startGameController();
    }
    /**
     * Wrapper method to delegate to the application controller the job of exiting from the app. 
     * This is made because every FXML file use only one controller and the application controller
     * couldn't be weigh down with functionalities which are view's relevance.
     */
    @FXML
    private void exitApp() {
        this.controller.exitApp();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void drawMenu(final Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        loader.setLocation(ClassLoader.getSystemResource("layouts/menu.fxml"));
        final Parent root = loader.load();
        stage.setScene(new Scene(root));
        this.drawnMenu = Optional.of(stage);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void showMenu() {
        if (this.drawnMenu.isPresent() && !this.drawnMenu.get().isShowing()) {
            this.drawnMenu.get().show();
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void hideMenu() {
        if (this.drawnMenu.isPresent() && this.drawnMenu.get().isShowing()) {
            this.drawnMenu.get().hide();
            this.drawnMenu = Optional.empty();
        }
    }
}
