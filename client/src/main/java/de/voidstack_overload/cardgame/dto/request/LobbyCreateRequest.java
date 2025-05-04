package de.voidstack_overload.cardgame.dto.request;

import de.voidstack_overload.cardgame.connection.MessageType;

public class LobbyCreateRequest extends BaseRequest {

    private final String lobbyName;
    private final String lobbyPassword;
    private final int maxPlayers;
    private final int botCount;

    public LobbyCreateRequest(String lobbyName, String lobbyPassword, int maxPlayers, int botCount) {
        super(MessageType.LOBBY_CREATE);
        this.lobbyName = lobbyName;
        this.lobbyPassword = lobbyPassword;
        this.maxPlayers = maxPlayers;
        this.botCount = botCount;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public String getLobbyPassword() {
        return lobbyPassword;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getBotCount() {
        return botCount;
    }
}
