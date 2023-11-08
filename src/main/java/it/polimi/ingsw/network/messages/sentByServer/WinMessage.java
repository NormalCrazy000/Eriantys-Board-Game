package it.polimi.ingsw.network.messages.sentByServer;

import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;


public class WinMessage extends ServerMessage {
    String winnerNickname;

    public WinMessage(String message,String winnerNickname) {
        super(message);
        this.winnerNickname=winnerNickname;
    }

    public String getWinnerNickname() {
        return winnerNickname;
    }

    @Override
    public void handle(ClientMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
