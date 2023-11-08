package it.polimi.ingsw.network.messages.sentByServer;

import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;

public class StartPlanningMessage extends ServerMessage{

    private String player;


    public StartPlanningMessage(String message, String player) {
        super(message);
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public void handle(ClientMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
