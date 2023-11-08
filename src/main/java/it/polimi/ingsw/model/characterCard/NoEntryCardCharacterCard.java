package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.IslandRegion;

import java.io.Serializable;


/**
 * NoEntryCardCharacterCard class
 * This class implements effect of card number 5:
 * In setup, put the 4 No Entry Tiles on this card.
 * Place a No Entry on an island of your choice.
 * The first time Mother Nature ends her movement there, put the No Entry tile back onto this card DO NOT calculate influence on that Island, or place any Towers.
 *
 * @author Christian Lisi
 */
public class NoEntryCardCharacterCard extends CharacterCard implements Serializable {
    int noEntry;
    int indexIsland;

    /**
     * Constructor method that sets information of card and associates object Game to card
     *
     * @param ID                type {@link String}: card's ID
     * @param cost              type {@link String}: card's cost
     * @param effectType        type {@link String}: card's type
     * @param setup             type {@link String}: card's type
     * @param effectDescription type {@link String}: card's effect
     */
    public NoEntryCardCharacterCard(String ID, String cost, String effectType, String setup, String effectDescription) {
        setID(ID);
        setCost(Integer.parseInt(cost));
        setEffectType(effectType);
        setSetup(setup);
        setEffectDescription(effectDescription);
        //Setup noEntry on card
        noEntry = 4;
    }

    /**
     * This method sets parameter that will be used to active effect
     *
     * @param indexIsland type {@link IslandRegion}: indexIsland to be blocked
     */
    public void setParameterToEffect(int indexIsland) {
        this.indexIsland = indexIsland;
    }

    /**
     * This method actives card's effect
     */
    @Override
    public void effect() {
        //add no entry on region
        getGame().getGameBoard().getRegion(indexIsland).addNoEntry();
        ((NoEntryCardCharacterCard) getGame().getCharacterDeck().getCardByID("5")).removeNoEntry();

    }

    /**
     * This method adds NoEntry on card
     */
    public void addNoEntry() {
        noEntry++;
    }

    /**
     * This method removes noEntry on card
     */
    private void removeNoEntry() {
        noEntry--;
    }

    /**
     * This method returns noEntry on card
     *
     * @return type int: noEntry value
     */
    public int getNoEntry() {
        return noEntry;
    }
}
