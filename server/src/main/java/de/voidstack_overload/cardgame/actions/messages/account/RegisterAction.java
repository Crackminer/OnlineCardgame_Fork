package de.voidstack_overload.cardgame.actions.messages.account;

import com.google.gson.JsonObject;
import de.voidstack_overload.cardgame.database.DataBaseHandler;
import de.voidstack_overload.cardgame.messages.OutgoingMessageType;
import de.voidstack_overload.cardgame.network.Response;
import de.voidstack_overload.cardgame.network.User;
import de.voidstack_overload.cardgame.network.UserManager;
import de.voidstack_overload.cardgame.utility.JsonBuilder;
import de.voidstack_overload.cardgame.utility.ResponseBuilder;
import org.java_websocket.WebSocket;

public class RegisterAction extends AccountAction {
    @Override
    public Response execute(JsonObject json, WebSocket connection) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        String username = json.get("username").getAsString();
        String password = json.get("password").getAsString();
        if(DataBaseHandler.INSTANCE.isRegisteredUser(username)) {
            jsonBuilder.add("errorMessage", "Username is already in use");
            return ResponseBuilder.build(OutgoingMessageType.ACCOUNT_REGISTER_DENY, jsonBuilder);
        }
        jsonBuilder.add("username", username);
        jsonBuilder.add("password", password);
        DataBaseHandler.INSTANCE.registerUser(username, password);
        UserManager.INSTANCE.addUser(new User(connection, username, password));
        return ResponseBuilder.build(OutgoingMessageType.ACCOUNT_REGISTER_ACCEPT, jsonBuilder);
    }
}
