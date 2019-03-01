package view.menu;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The class implementation of the {@link MenuView} interface.
 */
public final class MenuViewImpl implements MenuView {
    private Optional<Stage> menu = Optional.empty();
    private boolean showedMenu = false;
    /**
     * {@inheritDoc}
     */
    @Override
    public void drawMenu(final Stage stage) throws IOException {
        final Parent root = FXMLLoader.load(this.getClass().getResource("layouts/menu.fxml"));
        stage.setScene(new Scene(root));
        this.menu = Optional.of(stage);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void showMenu() {
        if (this.menu.isPresent() && !this.showedMenu) {
            this.menu.get().show();
            this.showedMenu = true;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void hideMenu() {
        if (this.showedMenu) {
            this.menu.get().hide();
            this.menu = Optional.empty();
            this.showedMenu = false;
        }
    }
}
