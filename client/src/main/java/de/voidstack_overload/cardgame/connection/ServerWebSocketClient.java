package de.voidstack_overload.cardgame.connection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.voidstack_overload.cardgame.connection.handler.AuthenticationResponseHandler;
import de.voidstack_overload.cardgame.connection.handler.LobbyListHandler;
import de.voidstack_overload.cardgame.connection.handler.LobbyResponseHandler;
import de.voidstack_overload.cardgame.connection.handler.ServerResponseHandler;
import de.voidstack_overload.cardgame.logging.StandardLogger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ServerWebSocketClient extends WebSocketClient {

    private final StandardLogger logger;
    private final List<ServerResponseHandler> responseHandlers;
    private CompletableFuture<ResponseEntity<?>> pendingRequest;

    public ServerWebSocketClient(URI serverUri) {
        super(serverUri);
        this.logger = new StandardLogger("Client");
        this.responseHandlers = Arrays.asList(
                new AuthenticationResponseHandler(),
                new LobbyResponseHandler(),
                new LobbyListHandler()
        );
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.log("Connected to server");
    }

    @Override
    public void onMessage(String message) {
        logger.log("Nachricht erhalten: " + message);
        try {
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();
            MessageDispatcher dispatcher = new MessageDispatcher(responseHandlers);
            Optional<ResponseEntity<?>> response = dispatcher.dispatch(json);
            if (pendingRequest != null && response.isPresent()) {
                pendingRequest.complete(response.get());
            }
        } catch (Exception e) {
            logger.log("Fehler beim Verarbeiten der Nachricht: " + e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        ConnectionManager.getInstance().disconnect();
        logger.log("Disconnected: " + reason);
    }

    @Override
    public void onError(Exception e) {
        logger.log("Error: " + e.getMessage());
    }

    public void onTransmit(String json, CompletableFuture<ResponseEntity<?>> future) {
        this.pendingRequest = future;
        this.send(json);
    }
}
