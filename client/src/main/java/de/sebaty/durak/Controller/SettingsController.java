package de.sebaty.durak.Controller;

import de.sebaty.durak.ControllerInterface;
import de.sebaty.durak.PaneResizer;
import de.sebaty.durak.fxmlNavigator;
import de.sebaty.durak.gameClient;
import de.sebaty.durak.gameLogic.Cards.Cards;
import de.sebaty.durak.gameLogic.Cards.SpecialTexture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This class holds various options like a FullScreen option, a Sound-slider which is not implemented and a theme changer
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class SettingsController implements ControllerInterface
{

    /**
     * The MenuItem which holds information about the resolution change that will be done with it. In this case its 600x400
     */
    @FXML
    private MenuItem res_1;

    /**
     * The MenuItem which holds information about the resolution change that will be done with it. In this case its 1200x800
     */
    @FXML
    private MenuItem res_2;

    /**
     * The ImageView whose picture will be set on its initial load
     */
    @FXML
    private ImageView picture;

    /**
     * The checkbox which can be ticked to go into Fullscreen
     */
    @FXML
    private CheckBox FullScreenCheckBox;

    /**
     * The MenuItem which holds information about the Theme change that will be done with it. In this case its blue
     */
    @FXML
    private MenuItem ThemeBlue_;

    /**
     * The MenuItem which holds information about the Theme change that will be done with it. In this case its red
     */
    @FXML
    private MenuItem ThemeRed_;

    /**
     * This method automatically resizes the content pane to the current stages size on its initial load
     * This method also sets a picture inside an imageview
     */
    @Override
    public void init()
    {
        Stage stage = gameClient.stage;
        if (stage.isFullScreen())
        {
            FullScreenCheckBox.setSelected(true);
        }
        Scene scene = stage.getScene();

        picture = (ImageView) scene.lookup("#picture");
        Image image = Cards.getSpecialImage(SpecialTexture.BackLowsat);
        picture.setImage(image);

        PaneResizer.resizePane(scene.getHeight(), true);
    }

    /**
     * This method will load the Home Screen
     */
    @FXML
    private void goToHome()
    {
        fxmlNavigator.loadFxml(fxmlNavigator.HOME);
    }

    /**
     * This method would change the volume of the sound effects according to the value, that was selected by the user. It is empty as there currently is no sound in the game
     */
    @FXML
    private void changeSound()
    {
        //TODO: only if sound is ever added: change volume of sound to percentage value of soundslider
    }

    /**
     * This method will set the Application to Fullscreen whether the box is ticked or not
     */
    @FXML
    private void goFullScreen()
    {
        gameClient.stage.setFullScreen(!gameClient.stage.isFullScreen());
    }

    /**
     * This method changes the resolution of the application according to the selected MenuItem
     * It does not set the resolution when in Fullscreen
     *
     * @param event     holds the information about what resolution menuItem was clicked
     */
    @FXML
    public void ChangeResolution(ActionEvent event)
    {
        if (gameClient.stage.isFullScreen())
        {
            //FIXME: doesn't really work like i thought it would, leaving this empty so it cannot be resized for now in fullscreen
        }
        else
        {
            if (event.getSource().equals(res_1))        // If event source (selected button of resolution changer) is res_1 (600 x 400) do following
            {
                gameClient.stage.setWidth(600.0);
                gameClient.stage.setHeight(400.0);
            }
            else if (event.getSource().equals(res_2))    // If event source (selected button of resolution changer) is res_2 (1200 x 800) do following
            {
                gameClient.stage.setWidth(1200.0);
                gameClient.stage.setHeight(800.0);
            }
            gameClient.stage.centerOnScreen();
        }
    }

    /**
     * This method changes the theme/style of the application according to the selected MenuItem
     *
     * @param event     holds the information about what theme/style menuItem was clicked
     */
    public void changeStyle(ActionEvent event)
    {
        if (event.getSource().equals(ThemeBlue_))        // If event source (selected button of resolution changer) is res_1 (600 x 400) do following
        {
            CSSController.changeTheme("ThemeBlue");
        }
        else if (event.getSource().equals(ThemeRed_))    // If event source (selected button of resolution changer) is res_2 (1200 x 800) do following
        {
            CSSController.changeTheme("ThemeRed");
        }
        else
        {
            CSSController.changeTheme("ThemeBlue");
        }
    }
}