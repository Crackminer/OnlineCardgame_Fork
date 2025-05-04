package de.voidstack_overload.cardgame.dto.request;

import de.voidstack_overload.cardgame.connection.MessageType;

public class LobbyGetRequest extends BaseRequest
{
    public LobbyGetRequest(MessageType type)
    {
        super(type);
    }
}
