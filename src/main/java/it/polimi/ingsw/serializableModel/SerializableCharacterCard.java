package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Game;

import java.io.Serializable;

/**
 * Serializable class that contains the information needed by the view.
 * Light copy of the {@link CharacterCard}.
 */
public class SerializableCharacterCard implements Serializable {

    private String ID;
    private int cost;
    private String effectType;
    private String setup;
    private String effectDescription;


    public SerializableCharacterCard(CharacterCard characterCard){
        this.ID = characterCard.getID();
        this.cost = characterCard.getCost();
        this.effectType = characterCard.getEffectType();
        this.setup = characterCard.getSetup();
        this.effectDescription = characterCard.getEffectDescription();
    }

    public String getID() {
        return ID;
    }

    public int getCost() {
        return cost;
    }

    public String getEffectType() {
        return effectType;
    }

    public String getSetup() {
        return setup;
    }

    public String getEffectDescription() {
        return effectDescription;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public void setEffectDescription(String effectDescription) {
        this.effectDescription = effectDescription;
    }
}
