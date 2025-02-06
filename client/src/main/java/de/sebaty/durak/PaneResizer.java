package de.sebaty.durak;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.Objects;

/**
 * Resizes each pane in the active scene
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class PaneResizer
{
    /**
     * Main method to resize pane
     *
     * @param newValue the new size value
     * @param isHeight distinguish between height and width scaling
     */
    public static void resizePane(Number newValue, Boolean isHeight)
    {
        Scene scene = gameClient.stage.getScene();

        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();

        if (isHeight)
        {
            sceneHeight = (double) newValue;
        }
        else
        {
            sceneWidth = (double) newValue;
        }

        if (sceneHeight <= 0.0 || sceneWidth <= 0.0)
        {
            return;
        }

        double newFactor = sceneWidth / gameClient.stageMinWidth;
        if (newFactor > sceneHeight / gameClient.stageMinHeight)
        {
            newFactor = sceneHeight / gameClient.stageMinHeight;
        }

        double translateX = (gameClient.stageMinWidth * newFactor - gameClient.stageMinWidth) / 2.0;
        double translateY = (gameClient.stageMinHeight * newFactor - gameClient.stageMinHeight) / 2.0;

        if (gameClient.stageMinWidth * newFactor < sceneWidth)	translateX = translateX + (sceneWidth - gameClient.stageMinWidth * newFactor) / 2.0;
        if (gameClient.stageMinHeight * newFactor < sceneHeight)	translateY = translateY + (sceneHeight - gameClient.stageMinHeight * newFactor) / 2.0;


        Pane pane = null;

        if (scene.lookup("#Startup") != null)	pane = (Pane)scene.lookup("#Startup");
        if (scene.lookup("#Loading") != null)	pane = (Pane)scene.lookup("#Loading");
        if (scene.lookup("#Home") != null)		pane = (Pane)scene.lookup("#Home");
        if (scene.lookup("#Settings") != null)	pane = (Pane)scene.lookup("#Settings");
        if (scene.lookup("#GameBoard") != null)	pane = (Pane)scene.lookup("#GameBoard");
        if (scene.lookup("#Login") != null)		pane = (Pane)scene.lookup("#Login");
        if (scene.lookup("#Credits") != null)	pane = (Pane)scene.lookup("#Credits");

        Objects.requireNonNull(pane).setTranslateX(translateX);
        pane.setTranslateY(translateY);

        pane.setScaleX(newFactor);
        pane.setScaleY(newFactor);
    }
}
