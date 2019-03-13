package controller.app;

import view.View;

/**
 * Class implementation of the {@link AppController}.
 */
public final class AppControllerImpl implements AppController {
    private final View view;
    /**
     * Acquires a new {@link View} to call when it's needed to display something.
     * @param view The view element responsible for the application.
     */
    public AppControllerImpl(final View view) {
        this.view = view;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void startApp() {
        this.view.displayMenu();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void exitApp() {
        System.exit(0);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        this.view.displayGame();
    }
}
