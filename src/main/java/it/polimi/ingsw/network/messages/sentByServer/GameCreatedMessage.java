package it.polimi.ingsw.network.messages.sentByServer;

import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;

public class GameCreatedMessage extends ServerMessage{

//    private static final long serialVersionUID = 1113799434508676095L;

    public GameCreatedMessage(String message) {
        super(message);
    }

    @Override
    public void handle(ClientMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
