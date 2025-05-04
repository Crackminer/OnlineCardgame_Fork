package de.voidstack_overload.cardgame.connection.handler;

import com.google.gson.JsonObject;
import de.voidstack_overload.cardgame.connection.ResponseEntity;
import de.voidstack_overload.cardgame.dto.response.LobbyListResponse;
import de.voidstack_overload.cardgame.utility.GsonUtil;

public class LobbyListHandler extends TypedResponseHandler
{

    public LobbyListHandler()
    {
        super("LOBBY");
    }

    @Override
    public ResponseEntity<LobbyListResponse> handleMessage(JsonObject message)
    {
        return switch(messageType) {
            case LOBBY_LIST_REPLY -> {
                LobbyListResponse lobbyResponse = GsonUtil.toObject(message, LobbyListResponse.class);
                logger.log("Lobby list gotten.");
                yield ResponseEntity.ok(lobbyResponse);
            }
            default ->
            {
                logger.log("Lobby List not available with this commands.");
                yield null;
            }
        };
    }
}
