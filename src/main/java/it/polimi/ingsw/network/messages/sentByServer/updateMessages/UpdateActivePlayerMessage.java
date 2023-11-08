package it.polimi.ingsw.network.messages.sentByServer.updateMessages;

import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;
import it.polimi.ingsw.network.messages.sentByServer.ServerMessage;

public class UpdateActivePlayerMessage extends ServerMessage {

    String nickname;

    public UpdateActivePlayerMessage(String message,String nickname) {
        super(message);
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void handle(ClientMessageHandler messageHandler) {
        messageHandler.handleUpdateMessage(this);
    }
}
