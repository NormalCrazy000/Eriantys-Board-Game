package it.polimi.ingsw.network.messages.sentByServer.updateMessages;

import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;
import it.polimi.ingsw.network.messages.sentByServer.ServerMessage;
import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;

public class UpdateEnemyPlayerBoard extends ServerMessage {

    SerializablePlayerBoard playerBoard;
    String nickname;

    public UpdateEnemyPlayerBoard(String message,SerializablePlayerBoard playerBoard, String nickname) {
        super(message);
        this.playerBoard = playerBoard;
        this.nickname = nickname;
    }

    public SerializablePlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void handle(ClientMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
