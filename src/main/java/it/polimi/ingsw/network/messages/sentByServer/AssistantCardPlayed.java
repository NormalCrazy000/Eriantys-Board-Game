package it.polimi.ingsw.network.messages.sentByServer;

import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;

public class AssistantCardPlayed extends ServerMessage {

    String nextActivePlayer;


    public AssistantCardPlayed(String message, String nextActivePlayer) {
        super(message);
        this.nextActivePlayer = nextActivePlayer;
    }

    public String getNextActivePlayer() {
        return nextActivePlayer;
    }

    @Override
    public void handle(ClientMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
