package de.voidstack_overload.cardgame.controller;

import de.voidstack_overload.cardgame.SceneFXML;
import de.voidstack_overload.cardgame.SceneManager;
import de.voidstack_overload.cardgame.connection.ConnectionManager;
import de.voidstack_overload.cardgame.connection.MessageType;
import de.voidstack_overload.cardgame.connection.ResponseEntity;
import de.voidstack_overload.cardgame.dto.request.LobbyGetRequest;
import de.voidstack_overload.cardgame.dto.response.LobbyListResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Blend;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

public class LobbyListScreenController extends BaseController {
    @FXML
    private ListView listView;
    @FXML
    private GridPane masterPane;

    @Override
    public void setSceneManager(SceneManager sceneManager)
    {
        super.setSceneManager(sceneManager);

        ResponseEntity resp = ConnectionManager.getInstance().sendRequest(new LobbyGetRequest(MessageType.LOBBY_LIST));

        System.out.println(resp);

        if (resp != null && resp.getBody() != null)
        {
            LobbyListResponse lobbyResponse = (LobbyListResponse) resp.getBody();

            for (int i = 0; i < lobbyResponse.lobbyList().length; i++)
            {
                //StackPane stackPane = new StackPane();

                GridPane gridPane = new GridPane();
                gridPane.setAlignment(Pos.CENTER);
                gridPane.setHgap(50);
                gridPane.setStyle("Lobby");

                Label lobbyName = new Label();
                lobbyName.setText(lobbyResponse.lobbyList()[i].lobbyName());
                lobbyName.getStyleClass().setAll("lobbyListLabel");
                lobbyName.setMinWidth(100);

                Label currentPlayers = new Label();
                currentPlayers.setText(String.format("%d", lobbyResponse.lobbyList()[i].currentPlayerCount()));
                currentPlayers.getStyleClass().setAll("lobbyListLabel");
                currentPlayers.setMinWidth(100);

                Label maxPlayers = new Label();
                maxPlayers.setText(String.format("/%d", lobbyResponse.lobbyList()[i].maxPlayerCount()));
                maxPlayers.getStyleClass().setAll("lobbyListLabel");
                maxPlayers.setMinWidth(100);

                gridPane.add(lobbyName, 0, 1);
                gridPane.add(currentPlayers, 1, 1);
                gridPane.add(maxPlayers, 2, 1);

                ImageView imageView = new ImageView("textures/lock.png");
                imageView.setFitWidth(100);
                imageView.setFitHeight(50);

                imageView.setEffect(new Blend());
                if (!lobbyResponse.lobbyList()[i].isPasswordProtected())
                {
                    imageView.setImage(null);
                }
                gridPane.add(imageView, 3, 1);

                Button joinButton = new Button();
                joinButton.setText("Join Lobby");
                joinButton.getStyleClass().setAll("normalButton");
                joinButton.setMinWidth(100);
                joinButton.setPrefWidth(100);

                if (lobbyResponse.lobbyList()[i].isPasswordProtected())
                {
                    joinButton.setOnAction((ActionEvent evt) -> {
                        //TODO: Add Password protected Lobby Screen for entering the Lobbies password
                       });
                }
                else
                {
                    joinButton.setOnAction((ActionEvent evt) -> {
                        //TODO: Join Lobby now
                    });
                }

                gridPane.add(joinButton, 4, 1);

                listView.getItems().add(gridPane);
            }
        }
    }

    public void switchToMenu() {
        try {
            sceneManager.switchScene(SceneFXML.PROFILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
