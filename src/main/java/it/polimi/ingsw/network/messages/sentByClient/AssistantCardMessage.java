package it.polimi.ingsw.network.messages.sentByClient;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.server.ServerMessageHandler;

public class AssistantCardMessage extends ClientMessage{

    AssistantCard assistantCard;

    public AssistantCardMessage(String message,AssistantCard assistantCard) {
        super(message);
        this.assistantCard = assistantCard;
    }


    public AssistantCard getAssistantCard() {
        return assistantCard;
    }

    @Override
    public void handle(ServerMessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
