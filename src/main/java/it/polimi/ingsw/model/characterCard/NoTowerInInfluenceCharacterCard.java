package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.CharacterCard;

import java.io.Serializable;


/**
 * NoTowerInInfluenceCharacterCard class
 * This class implements effect of card number 6:
 * When Resolving a Conquering on an Island, Towers do not count towards influence.
 *
 * @author Christian Lisi
 */
public class NoTowerInInfluenceCharacterCard extends CharacterCard implements Serializable {
    /**
     * Constructor method that sets information of card and associates object Game to card
     *
     * @param ID                type {@link String}: card's ID
     * @param cost              type {@link String}: card's cost
     * @param effectType        type {@link String}: card's type
     * @param effectDescription type {@link String}: card's effect
     */
    public NoTowerInInfluenceCharacterCard(String ID, String cost, String effectType, String effectDescription) {
        setID(ID);
        setCost(Integer.parseInt(cost));
        setEffectType(effectType);
        setSetup("");
        setEffectDescription(effectDescription);
    }

    /**
     * This method resets card's effect.
     * It must be called at the end turn
     */
    @Override
    public void resetEffect(String nickname) {
        getGame().setNoTowerInfluence(false);
        super.resetEffect(nickname);
    }

    /**
     * This method actives card's effect
     */
    @Override
    public void effect() {
        getGame().setNoTowerInfluence(true);
    }
}
