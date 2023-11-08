package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.PawnType;

import java.io.Serializable;


/**
 * ColorNoInfluenceCharacterCard class
 * This class implements effect of card number 3:
 * Choose a color of Student: during the influence calculation this turn, that color adds no influence.
 *
 * @author Christian Lisi
 */
public class ColorNoInfluenceCharacterCard extends CharacterCard implements Serializable {
    PawnType color;

    /**
     * Constructor method that sets information of card and associates object Game to card
     *
     * @param ID                type {@link String}: card's ID
     * @param cost              type {@link String}: card's cost
     * @param effectType        type {@link String}: card's type
     * @param effectDescription type String: card's effect
     */
    public ColorNoInfluenceCharacterCard(String ID, String cost, String effectType, String effectDescription) {
        setID(ID);
        setCost(Integer.parseInt(cost));
        setEffectType(effectType);
        setSetup("");
        setEffectDescription(effectDescription);
    }

    /**
     * This method sets parameter that will be used to active effect
     *
     * @param color type {@link PawnType}: color locked
     */
    public void setParameterToEffect(PawnType color) {
        this.color = color;
    }

    /**
     * This method resets card's effect.
     * It must be called at the end turn
     */
    @Override
    public void resetEffect(String nickname) {
        getGame().setColorDenied(null);
        super.resetEffect(nickname);
    }

    /**
     * This method actives card's effect
     */
    @Override
    public void effect() {
        getGame().setColorDenied(color);
    }
}
