package it.polimi.ingsw.network.messages.sentByServer;

import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;
import it.polimi.ingsw.network.messages.Message;


/**
 *
 * @author Christian Lisi
 */
public abstract class ServerMessage extends Message {
    public ServerMessage(String message) {
        super(message);
    }

    public abstract void handle(ClientMessageHandler messageHandler);
}