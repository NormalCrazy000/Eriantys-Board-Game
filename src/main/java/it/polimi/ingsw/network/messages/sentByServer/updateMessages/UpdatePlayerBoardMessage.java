package it.polimi.ingsw.network.messages.sentByServer.updateMessages;


import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;
import it.polimi.ingsw.network.messages.sentByServer.ServerMessage;
import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;

public class UpdatePlayerBoardMessage extends ServerMessage {

    private String nickname;
    private SerializablePlayerBoard playerBoard;

    public String getNickname() {
        return nickname;
    }

    public SerializablePlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public UpdatePlayerBoardMessage(String message, String nickname, SerializablePlayerBoard playerBoard) {
        super(message);
        this.nickname = nickname;
        this.playerBoard = playerBoard;
    }

    @Override
    public void handle(ClientMessageHandler messageHandler) {
        messageHandler.handleUpdateMessage(this);
    }
}
