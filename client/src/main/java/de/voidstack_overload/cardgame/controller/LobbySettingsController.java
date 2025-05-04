package de.voidstack_overload.cardgame.controller;

import de.voidstack_overload.cardgame.SceneFXML;
import de.voidstack_overload.cardgame.service.LobbyService;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import java.io.IOException;

public class LobbySettingsController extends BaseController {

    @FXML
    private TextField lobbyName;

    @FXML
    private PasswordField lobbyPassword;

    @FXML
    private Slider maxPlayers;

    @FXML
    private Slider botCount;

    private final LobbyService lobbyService = new LobbyService();

    public void createLobby() {
        String name = lobbyName.getText();
        String password = lobbyPassword.getText();
        int maxPlayers = (int) this.maxPlayers.getValue();
        int botCount = (int) this.botCount.getValue();
        lobbyService.lobbyCreate(name, password, maxPlayers, botCount);
    }

    public void switchToProfile() {
        try {
            sceneManager.switchScene(SceneFXML.PROFILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
