package de.sebaty.durak.Controller;

import de.sebaty.durak.ControllerInterface;
import de.sebaty.durak.PaneResizer;
import de.sebaty.durak.fxmlNavigator;
import de.sebaty.durak.gameClient;
import de.sebaty.durak.gameLogic.Cards.Cards;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Loading screen with progress bar that shows the status of caching all card images.
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class LoadingScreenController implements ControllerInterface
{
    /**
     * The Progress bar value.
     */
    double progressBarValue;
    /**
     * The card image number.
     */
    int imageNumber;
    /**
     * This method automatically resizes the content pane to the current stages size on its initial load
     * This method also starts the progress Bar which shows the progress of caching all card-images
     */
    @Override
    public void init()
    {
        Stage stage = gameClient.stage;
        Scene scene = stage.getScene();

        ProgressBar progressBar = (ProgressBar) scene.lookup("#LoadingBar");
        progressBar.setProgress(0);

        progressBarValue = 0.0;
        imageNumber = 0;

        PauseTransition pause = new PauseTransition(Duration.millis(1));
        pause.setOnFinished
                (
                        e ->
                        {
                            if (imageNumber < Cards.getImageBufferSize())
                            {
                                Cards.getImage(imageNumber);
                                progressBarValue = 1.0 * imageNumber / Cards.getImageBufferSize();
                                progressBar.setProgress(progressBarValue);
                                imageNumber++;
                                pause.playFromStart();
                            }
                            else
                            {
                                fxmlNavigator.loadFxml(fxmlNavigator.HOME);
                            }
                        }
                );
        pause.play();

        PaneResizer.resizePane(scene.getHeight(), true);
    }
}
