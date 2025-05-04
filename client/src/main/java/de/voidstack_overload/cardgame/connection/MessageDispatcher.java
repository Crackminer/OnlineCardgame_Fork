package de.voidstack_overload.cardgame.connection;

import com.google.gson.JsonObject;
import de.voidstack_overload.cardgame.connection.handler.ServerResponseHandler;
import de.voidstack_overload.cardgame.logging.StandardLogger;
import java.util.List;
import java.util.Optional;

public class MessageDispatcher {
    private final List<ServerResponseHandler> handlers;
    private final StandardLogger logger;

    public MessageDispatcher(List<ServerResponseHandler> handlers) {
        this.handlers = handlers;
        logger = new StandardLogger("Client");
    }

    public Optional<ResponseEntity<?>> dispatch(JsonObject message) {
        String type = message.get("type").getAsString();

        for (ServerResponseHandler handler : handlers) {
            if (handler.canHandle(type)) {
                var stuff = handler.handleMessage(message);
                if (stuff != null)
                {
                    return Optional.of(stuff);
                }
            }
        }

        logger.log("Unbekannter Nachrichtentyp: " + type);
        return Optional.empty();
    }
}
