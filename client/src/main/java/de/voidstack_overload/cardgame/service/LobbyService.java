package de.voidstack_overload.cardgame.service;

import de.voidstack_overload.cardgame.connection.ResponseEntity;
import de.voidstack_overload.cardgame.dto.request.LobbyCreateRequest;
import de.voidstack_overload.cardgame.dto.response.LobbyResponse;
import de.voidstack_overload.cardgame.utility.JsonBuilder;

public class LobbyService extends BaseService {

    public ResponseEntity<LobbyResponse> lobbyCreate(String lobbyName, String password, int maxPlayers, int botCount) {
        LobbyCreateRequest request = new LobbyCreateRequest(lobbyName, password, maxPlayers, botCount);
        return getConnectionManager().sendRequest(request);
    }

//    public void lobbyCreate(String lobbyName, String password, int maxPlayers, int botAmount) {

//        JsonBuilder jsonBuilder = new JsonBuilder();
//        jsonBuilder.add("type", "LOBBY_CREATE");
//        jsonBuilder.add("lobbyName", lobbyName);
//        jsonBuilder.add("password", password);
//        jsonBuilder.add("maxPlayers", maxPlayers);
//        jsonBuilder.add("botCount", botAmount);
//        sendMessage(jsonBuilder.toString());
//    }
//
//    public void lobbyJoin(String lobbyID, String password) {
//        JsonBuilder jsonBuilder = new JsonBuilder();
//        jsonBuilder.add("type", "LOBBY_JOIN");
//        jsonBuilder.add("lobbyID", lobbyID);
//        jsonBuilder.add("lobbyPassword", password);
//        sendMessage(jsonBuilder.toString());
//    }
//
//    public void lobbyLeave() {
//        JsonBuilder jsonBuilder = new JsonBuilder();
//        jsonBuilder.add("type", "LOBBY_LEAVE");
//        sendMessage(jsonBuilder.toString());
//    }
//
//    public void lobbyUpdate(String lobbyName, String lobbyPassword, int maxPlayers, int botAmount) {
//        JsonBuilder jsonBuilder = new JsonBuilder();
//        jsonBuilder.add("type", "LOBBY_UPDATE");
//        jsonBuilder.add("lobbyName", lobbyName);
//        jsonBuilder.add("lobbyPassword", lobbyPassword);
//        jsonBuilder.add("maxPlayers", maxPlayers);
//        jsonBuilder.add("botCount", botAmount);
//        sendMessage(jsonBuilder.toString());
//    }
//
//    public void lobbyList() {
//        JsonBuilder jsonBuilder = new JsonBuilder();
//        jsonBuilder.add("type", "LOBBY_LIST");
//        sendMessage(jsonBuilder.toString());
//    }
}
