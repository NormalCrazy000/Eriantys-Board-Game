package it.polimi.ingsw.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


/**
 * CharacterDeckTest class testes {@link CharacterDeck} class
 *
 * @author Christian Lisi
 * @see CharacterDeck
 */
class CharacterDeckTest {

    static CharacterDeck deck;

    @BeforeAll
    static void setup(){
        Game game = new Game(GameMode.EXPERT,2);
        deck = new CharacterDeck(game);
    }

    @Test
    @DisplayName("Character Deck constructor test")
    void testCharacterDeck(){
        assertNotNull(deck);
        assertEquals(3, deck.getDeckList().length);
        Arrays.stream(deck.getDeckList()).forEach(Assertions::assertNotNull);
    }

    @Test
    @DisplayName("Character Deck getCardByID (when NullPointerException is thrown) test")
    void whenExceptionThrown_testGetCardByID(){
        NullPointerException thrown = Assertions.assertThrows(NullPointerException.class,
                ()-> deck.getCardByID("14").getID(),"NullPointerException was expected");
    }
}