package de.sebaty.durak;

import javafx.scene.Node;

import java.util.ArrayList;

public class NodeResizer
{

    public static double oldSceneWidth;
    public static double oldSceneHeight;

    public static void resizeNodeList(double sceneWidth, double sceneHeight, ArrayList<Node> nodeList, Boolean isGettingRescaled)
    {
        for (Node node:nodeList)
        {
            resizeNode(sceneWidth, sceneHeight, node, isGettingRescaled);
        }
    }

    public static void resizeNode(double sceneWidth, double sceneHeight, Node node, Boolean isGettingRescaled)
    {
        double newFactor = sceneWidth / gameClient.stageMinWidth;
        if (newFactor > sceneHeight / gameClient.stageMinHeight)
        {
            newFactor = sceneHeight / gameClient.stageMinHeight;
        }

        double oW = node.getBoundsInLocal().getWidth();
        double oH = node.getBoundsInLocal().getHeight();



        if (oW <= 0.0 || oH <= 0.0)
        {
            return;
        }

        if (isGettingRescaled)
        {
            rescaleObject(node, newFactor);
        }

        double newX = ((node.getLayoutX() + oW / 2.0) * (sceneWidth / oldSceneWidth)) - oW / 2.0;
        double newY = ((node.getLayoutY() + oH / 2.0) * (sceneHeight / oldSceneHeight)) - oH / 2.0;

        if (newX > 0.0)
        {
            node.setLayoutX(newX);
        }

        if (newY > 0.0)
        {
            node.setLayoutY(newY);
        }

        // TODO: Create a working scaling algorithm
        /*double oldFactor = oldSceneWidth / gameClient.stageMinWidth_;
        if (oldFactor > oldSceneHeight / gameClient.stageMinHeight_)
        {
            oldFactor = oldSceneHeight / gameClient.stageMinHeight_;
        }



        if (isGettingRescaled)
        {
            rescaleObject(node, newFactor);
        }
        double oldX = node.getLayoutX();
        double oldY = node.getLayoutY();

        double newX = oldX + (sceneWidth - oldSceneWidth) / 2.0;
        double newY = oldY  + (sceneHeight - oldSceneHeight) / 2.0;

        newX = sceneWidth / 2.0 + (newX - sceneWidth / 2.0) * (newFactor / oldFactor);
        newY = sceneHeight / 2.0 + (newY - sceneHeight / 2.0) * (newFactor / oldFactor);

        System.out.printf("%f %f\n", oldSceneWidth, oldSceneHeight);
        System.out.printf("%f %f\n", sceneWidth, sceneHeight);
        System.out.printf("%f %f\n", oldX, oldY);
        System.out.printf("%f %f\n\n", newX, newY);

        System.out.printf("%f %f\n", newFactor, oldFactor);
        System.out.printf("%f \n", newFactor / oldFactor);

        if (newX > 0.0)
        {
            node.setLayoutX(newX);
        }

        if (newY > 0.0)
        {
            node.setLayoutY(newY);
        }*/
    }

    public static void rescaleObject(Node node, double factor)
    {
        node.setScaleX(factor);
        node.setScaleY(factor);
    }

}
