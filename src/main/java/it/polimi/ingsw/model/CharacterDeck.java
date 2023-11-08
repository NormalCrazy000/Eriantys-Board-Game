package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Random;

/**
 * CharacterDeck class
 *
 * @author Christian Lisi
 */
public class CharacterDeck implements Serializable {

    private final CharacterCard[] deckList = new CharacterCard[3];

    /**
     * Constructor CharacterDeck creates a character deck with 3 random {@link CharacterCard}.
     *
     * @param game of type Game {@link GameBoard} to set up CharacterCard.
     *             Some cards need the gameBoard to initialize their state
     */
    public CharacterDeck(Game game) {
        final int[] randomID = new Random().ints(1, 12).distinct().limit(3).toArray();

        CardFactory cf = new CardFactory();

        for (int i = 0; i < randomID.length; i++) {
            this.deckList[i] = cf.createCard((cf.getRightCardInfo("" + randomID[i])).get("ID"), game);
        }
    }

    /**
     * Method getCardByID returns the chosen card in the deckList based on the id provided by the player.
     *
     * @param chosenId of type String - the id chosen by the player.
     * @return the card of type {@link CharacterCard} with id=chosenId.
     * @throws NullPointerException if there aren't cards with the chosen id in deckList.
     */
    public CharacterCard getCardByID(String chosenId) throws NullPointerException {
        for (CharacterCard c : deckList) {
            if (c.getID().equals(chosenId)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Method getDeckList returns the list of cards in the character deck
     *
     * @return the list of cards in the deck as an Array of {@link CharacterCard}
     */
    public CharacterCard[] getDeckList() {
        return deckList;
    }
}
