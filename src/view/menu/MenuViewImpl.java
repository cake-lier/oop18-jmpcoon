package view.menu;

import java.io.IOException;
import java.util.Optional;

import controller.AppController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The class implementation of the {@link MenuView} interface.
 */
public final class MenuViewImpl implements MenuView {
    private final AppController controller;
    private Optional<Stage> drawnMenu = Optional.empty();
    /**
     * Binds the menu view to the assigned controller, which is also the controller
     * of this application.
     * @param controller The application controller.
     */
    public MenuViewImpl(final AppController controller) {
        this.controller = controller;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void drawMenu(final Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader();
        loader.setController(this.controller);
        loader.setLocation(this.getClass().getResource("layouts/menu.fxml"));
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
