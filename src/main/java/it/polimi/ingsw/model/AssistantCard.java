package it.polimi.ingsw.model;


import java.io.Serializable;

/**
 * AssistantCard class
 *
 * @author Christain Lisi
 */
public class AssistantCard implements Serializable {

    private final int value;
    private final int movementMotherNature;
    private boolean used;


    /**
     * Constructor AssistantCard creates a new AssistantCard instance.
     *
     * @param value                of type int - the value of the card
     * @param movementMotherNature of type int - the max amount of movement that mother nature can do
     */
    public AssistantCard(int value, int movementMotherNature) {
        this.value = value;
        this.movementMotherNature = movementMotherNature;
        this.used = false;
    }

    /**
     * Method getValue returns the value of this AssistantCard object.
     *
     * @return the value (type int) of this AssistantCard object.
     */
    public int getValue() {
        return value;
    }

    /**
     * Method getMovementMotherNature returns the movementMotherNature of this AssistantCard object.
     *
     * @return the movementMotherNature (type int) of this AssistantCard object.
     */
    public int getMovementMotherNature() {
        return movementMotherNature;
    }

    /**
     * Method isUsed check if the card has been used.
     *
     * @return if the card has been used.
     */
    public boolean isUsed() {
        return used;
    }


    /**
     * Method set the card's boolean value used to true.
     */
    public void cardIsBeenUsed() {
        this.used = true;
    }
}
