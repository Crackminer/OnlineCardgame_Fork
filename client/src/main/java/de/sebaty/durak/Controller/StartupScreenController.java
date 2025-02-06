package de.sebaty.durak.Controller;

import de.sebaty.durak.ControllerInterface;
import de.sebaty.durak.fxmlNavigator;
import de.sebaty.durak.gameLogic.Cards.Cards;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * The startup screen of the card game showing 4 different cards.
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class StartupScreenController implements ControllerInterface
{
    /**
     * The Content-holding pane
     */
    @FXML
    private Pane Startup;

    /**
     * This method will set the 4 different Cards into the pane
     * Nothing is resized here as this pane is the starting pane and is always the same height and width
     */
    @Override
    public void init()
    {
        ImageView iv1 = new ImageView();
        Image image = Cards.getImage(36); //Ace of Diamonds
        iv1.setImage(image);
        iv1.setCache(true);
        iv1.setLayoutX(205.0);
        iv1.setLayoutY(150.0);
        iv1.setPreserveRatio(true);
        iv1.setFitWidth(75);
        iv1.setRotate(330.0);
        Startup.getChildren().add(iv1);

        ImageView iv2 = new ImageView();
        image = Cards.getImage(49); //Ace of Hearts
        iv2.setImage(image);
        iv2.setCache(true);
        iv2.setLayoutX(245.0);
        iv2.setLayoutY(135.0);
        iv2.setPreserveRatio(true);
        iv2.setFitWidth(75);
        iv2.setRotate(350.0);
        Startup.getChildren().add(iv2);

        ImageView iv3 = new ImageView();
        image = Cards.getImage(23); //Ace of Clubs
        iv3.setImage(image);
        iv3.setCache(true);
        iv3.setLayoutX(285.0);
        iv3.setLayoutY(135.0);
        iv3.setPreserveRatio(true);
        iv3.setFitWidth(75);
        iv3.setRotate(10.0);
        Startup.getChildren().add(iv3);

        ImageView iv4 = new ImageView();
        image = Cards.getImage(62); //Ace of Spades
        iv4.setImage(image);
        iv4.setCache(true);
        iv4.setLayoutX(325.0);
        iv4.setLayoutY(150.0);
        iv4.setPreserveRatio(true);
        iv4.setFitWidth(75);
        iv4.setRotate(30.0);
        Startup.getChildren().add(iv4);
    }

    /**
     * This method will load the Loading Screen
     */
    @FXML
    private void goToLoading()
    {
        fxmlNavigator.loadFxml(fxmlNavigator.LOADING);
    }
}
