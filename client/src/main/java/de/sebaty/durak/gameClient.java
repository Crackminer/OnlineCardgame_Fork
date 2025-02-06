package de.sebaty.durak;

import java.io.IOException;

import de.sebaty.durak.Controller.*;
import de.sebaty.durak.Network.ClientNetwork;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This is the main method to control the game client
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.pablo.bernard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class gameClient extends Application
{
    /**
     * The main stage for the GUI
     */
    public static Stage stage;
    /**
     * The constant stageMinWidth_.
     */
    public static final double stageMinWidth = 600.0;
    /**
     * The constant stageMinHeight_.
     */
    public static final double stageMinHeight = 400.0;

    /**
     * Stage title.
     *
     * @param title the title
     */
    public void stageTitle(String title)
    {
        stage.setTitle(title);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Starts the application
     * Sets the geometry of the stage, titles and loads the scene
     * On Linux Systems Javafx has problems showing the icon symbol. Hence, the method distinguishes between OS for setting the applications icon
     *
     * @param stage Main stage to show
     */
    @Override
    public void start(Stage stage)
    {
        gameClient.stage = stage;
        gameClient.stage.setResizable(true);
        gameClient.stage.setMinWidth(stageMinWidth);
        gameClient.stage.setMinHeight(stageMinHeight);
        stageTitle("Cardgame");
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win"))
        {
            //This is windows os
            gameClient.stage.getIcons().add(new Image("textures/game_symbol.png", 32, 32, true, true, false)); // does not work on linux version
        }
        else if (os.contains("osx"))
        {
            //this is apple
            gameClient.stage.getIcons().add(new Image("textures/game_symbol.png", 32, 32, true, true, false)); // does not work on linux version
        }
        else if (os.contains("nix") || os.contains("aix") || os.contains("nux"))
        {
            //this is any linux/unix/*aix os
            System.out.println("I am working on any Unix, Linux or *AIX OS");
        }

        setScene(new Scene(loadMainPane()));
        fxmlNavigator.loadFxml(fxmlNavigator.STARTUP);
        setEventListener();

        gameClient.stage.show();
    }

    /**
     * Loads the main pane
     *
     * @return the newly generated Pane
     */
    public Pane loadMainPane()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlNavigator.MAIN));
            Pane mainPane = loader.load();
            MainController mainController = loader.getController();
            fxmlNavigator.setMainController(mainController);

            PauseTransition pause = new PauseTransition(Duration.millis(4000));
            pause.setOnFinished
                    (
                            pauseFinishedEvent ->
                            {
                                try
                                {
                                    fxmlNavigator.loadFxml(fxmlNavigator.LOADING);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                    );
            //pause.play();
            return mainPane;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return new Pane();
    }

    /**
     * Sets the main scene.
     *
     * @param scene The main scene of the stage.
     */
    public static void setScene(Scene scene)
    {
        stage.setScene(scene);
    }

    /**
     * Eventlistener listening to window size properties to enable scaled resizing of the window.
     */
    private void setEventListener()
    {
        Scene scene = stage.getScene();
        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> changeSize(newSceneWidth, false));
        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> changeSize(newSceneHeight, true));
    }

    /**
     * Method to change the size of the window.
     *
     * @param newValue New Size of the windows
     * @param isHeight Distinguish between height and width values.
     */
    private void changeSize(Number newValue, Boolean isHeight)
    {
        PaneResizer.resizePane(newValue, isHeight);
    }

    /**
     * Shuts down the application
     */
    public static void shutDown()
    {
        ClientNetwork.stopConnection();
        stage.close();
        System.exit(0);
    }

    /**
     * Stops the application in case the user pushes "x" button of the window.
     */
    @Override
    public void stop()
    {
        ClientNetwork.stopConnection();
        stage.close();
    }
}
