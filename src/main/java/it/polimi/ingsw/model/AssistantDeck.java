package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * AssistantDeck class
 *
 * @author Christian Lisi
 */
public class AssistantDeck implements Serializable {


    private final ArrayList<AssistantCard> deckList;
    private final MageType mageType;


    /**
     * Constructor AssistantDeck creates a new AssistantDeck instance and populate it.
     *
     * @param mageType of type {@link MageType} - the type of mage player have chosen in the first phase of the game.
     */
    public AssistantDeck(MageType mageType) {

        this.deckList = new ArrayList<>();
        this.mageType = mageType;

        int j = 1;


        for (int i = 0; i < 10; i++) {
            AssistantCard newCard = new AssistantCard(i + 1, j);
            deckList.add(newCard);


            if ((i + 1) % 2 == 0) {
                j++;
            }

        }
    }


    /**
     * Method getCard return the card with the value chosen by the player and toggle the used boolean to true.
     *
     * @param selectedValue - the value of the AssistantCard requested by the player.
     * @return the card of type {@link AssistantCard} with the value requested by the player.
     */
    public AssistantCard getCard(int selectedValue) {

        AssistantCard chosenCard = null;
        for (AssistantCard card : getAvailableDeckList()) {
            if (card.getValue() == selectedValue) {
                chosenCard = card;
            }
        }
        assert chosenCard != null;
        chosenCard.cardIsBeenUsed();
        return chosenCard;
    }


    /**
     * Method getMageType returns the mageType of the assistant deck.
     *
     * @return the mageType value.
     */
    public MageType getMageType() {
        return mageType;
    }


    /**
     * Method getDeckList returns the list of cards in the assistant deck.
     *
     * @return the list of cards in the deck as an ArrayList of {@link AssistantCard}.
     */
    public ArrayList<AssistantCard> getDeckList() {
        return deckList;
    }


    /**
     * Method getDeckList returns the list of cards in the assistant deck that have not been used by the player.
     *
     * @return the list of playable cards in the deck as an ArrayList of {@link AssistantCard}.
     */
    public ArrayList<AssistantCard> getAvailableDeckList() {
        return (ArrayList<AssistantCard>) deckList.stream().filter(card -> !card.isUsed()).collect(Collectors.toList());
    }


    /**
     * Method getDiscardPileList returns the list of cards in the assistant deck that have been used by the player.
     *
     * @return the list of used cards in the deck as an ArrayList of {@link AssistantCard}.
     */
    public ArrayList<AssistantCard> getDiscardPileList() {
        return (ArrayList<AssistantCard>) deckList.stream().filter(AssistantCard::isUsed).collect(Collectors.toList());
    }


}
