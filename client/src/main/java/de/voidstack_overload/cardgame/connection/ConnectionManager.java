package de.voidstack_overload.cardgame.connection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.voidstack_overload.cardgame.logging.StandardLogger;
import de.voidstack_overload.cardgame.dto.request.BaseRequest;
import de.voidstack_overload.cardgame.utility.GsonUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


public class ConnectionManager {
    private static ConnectionManager INSTANCE;
    protected final StandardLogger LOGGER;
    private ServerWebSocketClient client;
    private String serverUri;
    private boolean isConnected;

    private ConnectionManager() {
        isConnected = false;
        LOGGER = new StandardLogger("Client");
    }

    public static ConnectionManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionManager();
        }
        return INSTANCE;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public void connect(String serverUri) {
        if (isConnected || serverUri.isEmpty()) return;

        try {
            this.serverUri = serverUri;
            this.client = new ServerWebSocketClient(new URI(serverUri));
            this.client.connectBlocking();
            isConnected = true;
        } catch (URISyntaxException | InterruptedException e) {
            LOGGER.log("Failed to connect: " + e.getMessage());
        }
    }

    public <T> ResponseEntity<T> sendRequest(BaseRequest requestBody) {
        if (client == null || !client.isOpen()) {
            connect(serverUri);
        }
        if (client.isOpen()) {
            CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();
            this.client.onTransmit(GsonUtil.toJson(requestBody), future);

            try {
                return (ResponseEntity<T>) future.get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                LOGGER.log("Fehler bei Anfrage: " + e.getMessage());
                return ResponseEntity.error("Zeitüberschreitung oder Verbindungsfehler");
            }
        } else {
            LOGGER.log("Connection not established.");
            return ResponseEntity.error("Nicht mit Server verbunden");
        }
    }

    public void disconnect() {
        isConnected = false;
        if (client == null || !client.isOpen()) return;
        client.close();
        LOGGER.log("Disconnected from server");
    }

    private void handleServerMessage(String message) {
        try {
            JsonObject jsonNode = JsonParser.parseString(message).getAsJsonObject();
            String type = jsonNode.get("type").getAsString();
            String errorMessage;
            switch (type) {
//                case "ACCOUNT_LOGIN_ACCEPT":
//                    LOGGER.log("Login erfolgreich! Benutzername: " + jsonNode.get("username").getAsString());
//                    Platform.runLater(() -> {
//                        if (loginController != null) {
//                            loginController.acceptLogin();
//                        }
//                    });
//                    break;
//
//                case "ACCOUNT_LOGIN_DENY":
//                    errorMessage = jsonNode.get("errorMessage").getAsString();
//                    LOGGER.log("Login fehlgeschlagen: " + errorMessage);
//                    Platform.runLater(() -> {
//                        if (loginController != null) {
//                            loginController.showError(errorMessage);
//                        }
//                    });
//                    break;
//
//                case "ACCOUNT_REGISTER_ACCEPT":
//                    LOGGER.log("Registrierung erfolgreich! Benutzername: " + jsonNode.get("username").getAsString());
//                    break;
//
//                case "ACCOUNT_REGISTER_DENY":
//                    errorMessage = jsonNode.get("errorMessage").getAsString();
//                    LOGGER.log("Registrierung fehlgeschlagen: " + errorMessage);
//                    Platform.runLater(() -> {
//                        if (registrationController != null) {
//                            registrationController.showError(errorMessage);
//                        }
//                    });
//                    break;

                case "LOBBY_CREATE_ACCEPT":
                    LOGGER.log("Lobby erstellt! Lobby-ID: " + jsonNode.get("lobbyID").getAsString());
                    break;

                case "LOBBY_CREATE_DENY":
                    LOGGER.log("Lobby konnte nicht erstellt werden.");
                    break;

                case "LOBBY_JOIN_ACCEPT":
                    LOGGER.log("Lobby beigetreten.");
                    break;

                case "LOBBY_JOIN_DENY":
                    LOGGER.log("Lobby-Beitritt fehlgeschlagen: " + jsonNode.get("errorMessage").getAsString());
                    break;

                case "LOBBY_LIST_REPLY":
                    LOGGER.log("Lobbys erhalten.");
                    for (var lobbyElement : jsonNode.getAsJsonArray("lobbyList")) {
                        JsonObject lobby = lobbyElement.getAsJsonObject();
                        LOGGER.log("ID: " + lobby.get("lobbyID").getAsString() +
                                ", Name: " + lobby.get("lobbyName").getAsString() +
                                ", Spieler: " + lobby.get("currentPlayerCount").getAsInt() + "/" +
                                lobby.get("maxPlayerCount").getAsInt() +
                                ", Passwortgeschützt: " + lobby.get("isPasswordProtected").getAsBoolean());
                    }
                    break;

                case "GAME_STATE":
                    LOGGER.log("Spielstatus aktualisiert! Aktiver Spieler: " + jsonNode.get("activePlayer").getAsString());
                    break;

                case "UNKNOWN_CASE":
                case "ERROR":
                case "INSUFFICIENT_PERMISSIONS":
                    LOGGER.log("Serverfehler: " + jsonNode.get("errorMessage").getAsString());
                    break;

                default:
                    LOGGER.log("Unbekannter Nachrichtentyp: " + type);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log("Fehler beim Verarbeiten der Nachricht: " + e.getMessage());
        }
    }
}
