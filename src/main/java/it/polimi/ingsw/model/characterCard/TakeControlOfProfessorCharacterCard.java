package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.CharacterCard;

import java.io.Serializable;


/**
 * TakeControlOfProfessorCharacterCard class
 * This class implements effect of card number 2:
 * During this turn, you take control of any number of Professor even if you have the same number of Students as the player who currently controls them.
 *
 * @author Christian Lisi
 */
public class TakeControlOfProfessorCharacterCard extends CharacterCard implements Serializable {

    /**
     * Constructor method that sets information of card and associates object Game to card
     *
     * @param ID                type {@link String}: card's ID
     * @param cost              type {@link String}: card's cost
     * @param effectType        type {@link String}: card's type
     * @param effectDescription type {@link String}: card's effect
     */
    public TakeControlOfProfessorCharacterCard(String ID, String cost, String effectType, String effectDescription) {
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


        getGame().getCurrentPlayer().setControlProfessor(true);
        getGame().assignProfessor();

    }

    /**
     * This method resets card's effect.
     * It must be called at the end turn
     */
    @Override
    public void resetEffect(String nickname) {
        getGame().getPlayerByNickname(nickname).getPlayerboard().setCharacterCardProfessors(false);
        getGame().getPlayerByNickname(nickname).setControlProfessor(false);
        super.resetEffect(nickname);
    }
}
