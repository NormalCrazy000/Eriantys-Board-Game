package it.polimi.ingsw.model;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PlayerTest class testes {@link Player} class
 *
 * @author Maria Pia Marini
 * @see Player
 */

public class PlayerTest {

    @Test
    @DisplayName("Player constructor test")
    public void testPlayer() {
        //Setup
        Player player = new Player("nickname1");
        assertNotNull(player.getPlayerboard());
        assertEquals("nickname1", player.getNickname());
        assertFalse(player.isControlProfessor());
        assertFalse(player.isActionDone());
        assertEquals(0, player.getAdditionalMovement());
        assertEquals(0, player.getAdditionalInfluence());
        assertNull(player.getCharacterCardPlayed());
    }

    @Test
    @DisplayName("Player isAssistantDeckEmpty test(True)")
    public void testIsAssistantDeckEmptyTrue() {
        //Setup
        Player player = new Player("nickname1");
        player.selectAssistantDeckType(MageType.ELDER_MAGICIAN);
        for (int i = 0; i < 10; i++) {
            player.selectCardFromAssistantDeck(i + 1);
        }
        //Check condition
        assertEquals(true, player.isAssistantDeckEmpty());

    }

    @Test
    @DisplayName("Player isAssistantDeckEmpty test(False)")
    public void testIsAssistantDeckEmptyFalse() {
        //Setup
        Player player = new Player("nickname1");
        player.selectAssistantDeckType(MageType.ELDER_MAGICIAN);
        //Check condition
        assertEquals(false, player.isAssistantDeckEmpty());

    }

    @Test
    @DisplayName("Player selectAssistantDeckType test")
    public void testSelectAssistantDeckType() {
        //Setup
        Player player = new Player("nickname1");
        player.selectAssistantDeckType(MageType.ELDER_MAGICIAN);
        //Check condition
        assertEquals(MageType.ELDER_MAGICIAN, player.getAssistantDeck().getMageType());

    }

    @Test
    @DisplayName("Player selectCardFromAssistant test")
    public void testSelectCardFromAssistantDeck() {
        //Setup
        Player player = new Player("nickname1");
        player.selectAssistantDeckType(MageType.ELDER_MAGICIAN);
        player.selectCardFromAssistantDeck(1);
        //Check condition
        assertEquals(1, player.getCardPlayed().getValue());

    }
    @Test
    @DisplayName("Player setActionDone test")
    public void testSetActionDone() {
        //Setup
        Player player = new Player("nickname1");
        player.setActionDone(true);
        //Check condition
        assertTrue(player.isActionDone());

    }
}
