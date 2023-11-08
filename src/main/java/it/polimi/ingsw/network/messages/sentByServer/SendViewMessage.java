package it.polimi.ingsw.network.messages.sentByServer;

import it.polimi.ingsw.network.client.View;
import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;

public class SendViewMessage extends ServerMessage {

    private View view;

    public SendViewMessage(String message, View view) {
        super(message);
        this.view = view;
    }

    @Override
    public void handle(ClientMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }

    public View getView() {
        return view;
    }
}
