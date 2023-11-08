package it.polimi.ingsw.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


/**
 * AssistantDeckTest class testes {@link AssistantDeck} class
 *
 * @author Christian Lisi
 * @see AssistantDeck
 */
class AssistantDeckTest {

    static AssistantDeck deck;

    @BeforeAll
    static void setup (){
        deck = new AssistantDeck(MageType.ELDER_MAGICIAN);
    }


    @Test
    @DisplayName("Assistant Deck constructor test")
    void testAssistantDeck(){
        assertNotNull(deck);
        assertEquals(10, deck.getDeckList().size());
        deck.getDeckList().forEach(Assertions::assertNotNull);
    }


    @ParameterizedTest
    @CsvSource(value = {"1:1", "2:1", "3:2", "4:2", "5:3", "6:3", "7:4", "8:4", "9:5", "10:5"},delimiter = ':')
    @DisplayName("Assistant Deck getCard test")
    void testGetCard(int value,int movement) {
        AssistantCard card = deck.getCard(value);
        assertEquals(card.getValue(),value);
        assertEquals(card.getMovementMotherNature(),movement);
    }


    @Test
    @DisplayName("Assistant Deck getMageType test")
    void testGetMageType() {
        assertEquals(deck.getMageType(),MageType.ELDER_MAGICIAN);
    }


    @Test
    @DisplayName("Assistant Deck getAvailableDeckList test")
    void testGetAvailableDeckList() {
        AssistantDeck deck = new AssistantDeck(MageType.ELDER_MAGICIAN);
        deck.getCard(1).cardIsBeenUsed();
        deck.getCard(2).cardIsBeenUsed();
        assertEquals(deck.getAvailableDeckList(),deck.getDeckList().stream().filter(card-> !card.isUsed()).collect(Collectors.toList()));
        assertEquals(8,deck.getAvailableDeckList().size());
    }

    @Test
    @DisplayName("Assistant Deck getDiscardPileList test")
    void testGetDiscardPileList(){
        AssistantDeck deck = new AssistantDeck(MageType.ELDER_MAGICIAN);
        deck.getCard(1).cardIsBeenUsed();
        deck.getCard(2).cardIsBeenUsed();
        assertEquals(deck.getDiscardPileList(),deck.getDeckList().stream().filter(AssistantCard::isUsed).collect(Collectors.toList()));
        assertEquals(2,deck.getDiscardPileList().size());
    }

}