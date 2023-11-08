package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;


/**
 * AssistantCardTest class testes {@link AssistantCard} class
 *
 * @author Christian Lisi
 * @see AssistantCard
 */
class AssistantCardTest {

    AssistantDeck deck;
    AssistantCard card;


    @BeforeEach
    void setup(){
        deck = new AssistantDeck(MageType.ELDER_MAGICIAN);
    }


    @ParameterizedTest
    @CsvSource (value = {"1","2","3","4","5","6","7","8","9","10"})
    @DisplayName("Assistant Card getValue test")
    void testGetValue(int value) {
        card = deck.getCard(value);
        assertEquals(card.getValue(),value);
    }

    @ParameterizedTest
    @CsvSource (value = {"1:1","2:1","3:2","4:2","5:3","6:3","7:4","8:4","9:5","10:5"},delimiter = ':')
    @DisplayName("Assistant Card getMovementMotherNature test")
    void testGetMovementMotherNature(int value,int movement) {
        card = deck.getCard(value);
        assertEquals(card.getMovementMotherNature(),movement);
    }
}