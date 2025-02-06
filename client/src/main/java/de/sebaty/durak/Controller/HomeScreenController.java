package de.sebaty.durak.Controller;

import de.sebaty.durak.ControllerInterface;
import de.sebaty.durak.PaneResizer;
import de.sebaty.durak.fxmlNavigator;
import de.sebaty.durak.gameClient;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Loads all fxml elements for the home screen, e.g. Settings, Play, Quit, Credits etc.
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class HomeScreenController implements ControllerInterface
{
    /**
     * This method will load the Settings Screen
     */
    @FXML
    private void goToSettings()
    {
        fxmlNavigator.loadFxml(fxmlNavigator.SETTINGS);
    }

    /**
     * This method will load the Credits Screen
     */
    @FXML
    private void goToCredits()
    {
        fxmlNavigator.loadFxml(fxmlNavigator.CREDITS);
    }

    /**
     * This method will load the Login Screen
     */
    @FXML
    private void goToLogin()
    {
        fxmlNavigator.loadFxml(fxmlNavigator.LOGIN);
    }

    /**
     * This method will shut down the application when the Button Quit was pressed
     */
    @FXML
    private void shutDown()
    {
        gameClient.shutDown();
    }

    /**
     * This method automatically resizes the content pane to the current stages size on its initial load
     */
    @Override
    public void init()
    {
        Stage stage = gameClient.stage;
        Scene scene = stage.getScene();
        PaneResizer.resizePane(scene.getHeight(), true);
    }
}
