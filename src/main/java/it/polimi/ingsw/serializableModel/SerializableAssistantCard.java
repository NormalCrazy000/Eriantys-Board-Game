package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.AssistantCard;

import java.io.Serializable;

/**
 * Serializable class that contains the information needed by the view.
 * Light copy of the {@link AssistantCard}.
 */
public class SerializableAssistantCard implements Serializable {

    private final int value;
    private final int movementMotherNature;
    private boolean used;


    public SerializableAssistantCard(AssistantCard assistantCard){
        this.value = assistantCard.getValue();
        this.movementMotherNature = assistantCard.getMovementMotherNature();
        this.used = assistantCard.isUsed();
    }


    public int getValue() {
        return value;
    }

    public int getMovementMotherNature() {
        return movementMotherNature;
    }

    public boolean isUsed() {
        return used;
    }


    public void setUsed(boolean used) {
        this.used = used;
    }
}
