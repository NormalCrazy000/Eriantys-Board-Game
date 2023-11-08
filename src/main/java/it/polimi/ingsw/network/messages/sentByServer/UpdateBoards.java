package it.polimi.ingsw.network.messages.sentByServer;

import it.polimi.ingsw.network.client.messageHandler.ClientMessageHandler;
import it.polimi.ingsw.serializableModel.SerializableGameBoard;
import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;

public class UpdateBoards extends ServerMessage{

    SerializableGameBoard gameBoard;
    SerializablePlayerBoard playerBoard;

    public UpdateBoards(String message, SerializablePlayerBoard playerBoard, SerializableGameBoard gameBoard) {
        super(message);
        this.gameBoard = gameBoard;
        this.playerBoard = playerBoard;
    }


    public SerializableGameBoard getGameBoard() {
        return gameBoard;
    }

    public SerializablePlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    @Override
    public void handle(ClientMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
