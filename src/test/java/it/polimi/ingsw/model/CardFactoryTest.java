package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


/**
 * CardFactoryTest class testes {@link CardFactory} class
 *
 * @author Christian Lisi
 * @see CardFactory
 */
class CardFactoryTest {

    static CardFactory cf;

    @BeforeAll
    static void setup(){
        cf = new CardFactory();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/helper_CardFactoryTest.cvs",delimiterString = "-")
    @DisplayName("Card Factory createCard test")
    void testCreateCard(String id , String cost,String effect, String setup, String effectDescription) {
        CharacterCard card = cf.createCard(id,new Game(GameMode.EXPERT,2));
        assertEquals(card.getID(),id);
        assertEquals(card.getCost(),Integer.parseInt(cost));
        assertEquals(card.getEffectType(),effect);
        assertEquals(card.getSetup(), Objects.requireNonNullElse(setup, ""));
        assertEquals(card.getEffectDescription(),effectDescription);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/helper_CardFactoryTest.cvs", delimiterString = "-")
    @DisplayName("Card Factory getRightCardInfo (xml parsing) test")
    void testGetRightCardInfo(String id , String cost,String effect, String setup, String effectDescription){
        HashMap<String,String> card = cf.getRightCardInfo(id);
        assertEquals(card.get("ID"),id);
        assertEquals(card.get("cost"),cost);
        assertEquals(card.get("effect"),effect);
        assertEquals(card.get("setup"),(setup == null)?"":setup);
        assertEquals(card.get("effectDescription"),effectDescription);
    }
}