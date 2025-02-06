package de.sebaty.durak;

import de.sebaty.durak.Controller.MainController;

/**
 * This class is The giver of all navigation that will be used to change the overlay/gui dynamically
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class fxmlNavigator
{
    /**
     * Resembles the fxml-file to load for the Main Screen
     */
    public static final String MAIN = "Main.fxml";

    /**
     * Resembles the fxml-file to load for the Startup Screen
     */
    public static final String STARTUP = "StartupScreen.fxml";

    /**
     * Resembles the fxml-file to load for the Loading Screen
     */
    public static final String LOADING = "LoadingScreen.fxml";

    /**
     * Resembles the fxml-file to load for the Login Screen
     */
    public static final String LOGIN = "loginScreen.fxml";

    /**
     * Resembles the fxml-file to load for the Home Screen
     */
    public static final String HOME = "HomeScreen.fxml";

    /**
     * Resembles the fxml-file to load for the Game Screen
     */
    public static final String GAME = "GameBoard.fxml";

    /**
     * Resembles the fxml-file to load for the Settings Screen
     */
    public static final String SETTINGS = "Settings.fxml";

    /**
     * Resembles the fxml-file to load for the Credits Screen
     */
    public static final String CREDITS = "Credits.fxml";

    /**
     * The Master Controller that holds every fxml
     */
    private static MainController mainController_;

    /**
     * This method sets the Master Controller
     *
     * @param mainController    The Master Controller
     */
    public static void setMainController(MainController mainController)
    {
        fxmlNavigator.mainController_ = mainController;
    }

    /**
     * This method sets the Master Controllers Panes child to the specified fxml-file
     *
     * @param fxml  The fxml-file that shall be loaded
     */
    public static void loadFxml(String fxml)
    {
        mainController_.setFxml(fxml);
    }
}
