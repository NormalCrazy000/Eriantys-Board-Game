package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.IslandRegion;

import java.io.Serializable;


/**
 * ResolveIslandCharacterCard class
 * This class implements effect of card number 3:
 * Choose an Island and resolve the Island as if Mother Nature had ended her movement there.
 * Mother Nature will still move and the Island where she ends her movement will also be resolved.
 *
 * @author Christian Lisi
 */
public class ResolveIslandCharacterCard extends CharacterCard implements Serializable {
    int indexRegion;

    /**
     * Constructor method that sets information of card and associates object Game to card
     *
     * @param ID                type {@link String}: card's ID
     * @param cost              type {@link String}: card's cost
     * @param effectType        type {@link String}: card's type
     * @param effectDescription type {@link String}: card's effect
     */
    public ResolveIslandCharacterCard(String ID, String cost, String effectType, String effectDescription) {
        setID(ID);
        setCost(Integer.parseInt(cost));
        setEffectType(effectType);
        setSetup("");
        setEffectDescription(effectDescription);
    }

    /**
     * This method sets parameter that will be used to active effect
     *
     * @param indexRegion type {@link IslandRegion}: indexRegion to be blocked
     */
    public void setParameterToEffect(int indexRegion) {
        this.indexRegion = indexRegion;
    }

    /**
     * This method actives card's effect
     */
    @Override
    public void effect() {
        getGame().checkTowerInfluence(getGame().getGameBoard().getRegion(indexRegion));
        getGame().getGameBoard().checkIslandAggregation();
    }

}
