package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.CharacterCard;

import java.io.Serializable;


/**
 * Add2ToMotherNatureMovementCharacterCard class
 * This class implements effect of card number 4:
 * You may move Mother Nature up to 2 additional Island than is indicated by the Assistant card you've played
 *
 * @author Christian Lisi
 */
public class Add2ToMotherNatureMovementCharacterCard extends CharacterCard implements Serializable {
    /**
     * Constructor method that sets information of card and associates object Game to card
     *
     * @param ID                type {@link String}: card's ID
     * @param cost              type {@link String}: card's cost
     * @param effectType        type {@link String}: card's type
     * @param effectDescription type {@link String}: card's effect
     */
    public Add2ToMotherNatureMovementCharacterCard(String ID, String cost, String effectType, String effectDescription) {
        setID(ID);
        setCost(Integer.parseInt(cost));
        setEffectType(effectType);
        setSetup("");
        setEffectDescription(effectDescription);
    }

    /**
     * This method actives card's effect
     */
    @Override
    public void effect() {
        getGame().getCurrentPlayer().setAdditionalMovement(2);
    }

    /**
     * This method resets card's effect.
     * It must be called at the end turn
     */
    @Override
    public void resetEffect(String nickname) {
        getGame().getPlayerByNickname(nickname).setAdditionalMovement(0);
        //Call parent resetEffect
        super.resetEffect(nickname);
    }

}
