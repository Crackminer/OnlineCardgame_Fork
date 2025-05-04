package de.voidstack_overload.cardgame.utility;

public record Lobby(String lobbyID, String lobbyName, int currentPlayerCount, int maxPlayerCount, boolean isPasswordProtected)
{
}
