package de.sebaty.durak.Controller;

import de.sebaty.durak.gameClient;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * The type Css controller.
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class CSSController
{

    /**
     * Controls loading of css themes to the main stage.
     *
     * @param theme the css theme to load. Find css themes at resources/styles/
     */
    public static void changeTheme(String theme)
    {
        String themeFile_;

        Stage stage = gameClient.stage;
        Scene scene = stage.getScene();


        if (theme.equals("ThemeBlue"))
        {
            themeFile_ = Objects.requireNonNull(CSSController.class.getClassLoader().getResource("styles/cardGameStyle_blue.css")).toExternalForm();
        }
        else if (theme.equals("ThemeRed"))
        {
            themeFile_ = Objects.requireNonNull(CSSController.class.getClassLoader().getResource("styles/cardGameStyle_red.css")).toExternalForm();
        }
        else
        {
            themeFile_ = Objects.requireNonNull(CSSController.class.getClassLoader().getResource("styles/cardGameStyle.css")).toExternalForm();
        }

        Pane Main = (Pane) scene.lookup("#fxmlHolder");
        Main.getStylesheets().clear();
        Main.getStylesheets().add(themeFile_);
        stage.setScene(scene);
        stage.show();
    }
}
