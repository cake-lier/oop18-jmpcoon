package view.menu;

import java.io.IOException;
import controller.app.AppController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The class implementation of the {@link Menu} interface.
 */
public final class MenuImpl implements Menu {
    private final AppController controller;
    private final Stage stage;
    private boolean drawn;
    private boolean showed;
    /**
     * Binds this menu to the instance who has to be the controller of the menu, which is
     * the controller of the application. Furthermore, it acquires the {@link Stage} in
     * which to draw the menu.
     * @param controller The controller.
     * @param stage The stage in which to draw the menu.
     */
    public MenuImpl(final AppController controller, final Stage stage) {
        this.controller = controller;
        this.stage = stage;
        this.drawn = false;
        this.showed = false;
    }
    /**
     * Wrapper method to delegate to the application controller the job of starting the
     * game. This is made because every FXML file use only one controller and the
     * application controller couldn't be weigh down with other functionalities which are
     * view's relevance.
     */
    @FXML
    private void startGame() {
        this.controller.startGame();
    }
    /**
     * Wrapper method to delegate to the application controller the job of exiting from
     * the app. This is made because every FXML file use only one controller and the
     * application controller couldn't be weigh down with functionalities which are view's
     * relevance.
     */
    @FXML
    private void exitApp() {
        this.controller.exitApp();
    }
    /**
     * {@inheritDoc}
     * It loads the ".fxml" associated file and sets as JavaFX controller this specific
     * instance of menu, then draws it.
     */
    @Override
    public void drawMenu() {
        final FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        loader.setLocation(ClassLoader.getSystemResource("layouts/menu.fxml"));
        try {
            this.stage.setScene(new Scene(loader.load()));
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        this.drawn = true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void showMenu() {
        if (this.drawn && !this.showed) {
            this.stage.show();
            this.showed = true;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void hideMenu() {
        if (this.showed) {
            this.stage.hide();
            this.drawn = false;
            this.showed = false;
        }
    }
}
