package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameMode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoEntryCardCharacterCardTest {
    NoEntryCardCharacterCard card;
    Game game;

    @BeforeEach
    public void setup(){
        game = new Game(GameMode.EXPERT,2);
        card = new NoEntryCardCharacterCard("5","2","NoEntryCard","In setup, put the 4 No Entry Tiles on this card.","Place a No Entry on an island of your choice. The first time Mother Nature ends her movement there, put the No Entry tile back onto this card DO NOT calculate influence on that Island, or place any Towers.");
        card.setGame(game);
    }

    @AfterEach
    public void reset(){
        card = null;
        game = null;

    }

    @Test
    @DisplayName("NoEntryCardCharacterCard action test")
    public void testAction(){

        game.getCharacterDeck().getDeckList()[0] = card;

        card.setParameterToEffect(0);
        card.effect();
        assertEquals(1,game.getGameBoard().getRegion(0).getNoEntry());
        assertEquals(3,card.getNoEntry());
    }

    @Test
    @DisplayName("NoEntryCardCharacterCard addNoEntry test")
    public void testAddNoEntry(){
        card.addNoEntry();
        assertEquals(5,card.getNoEntry());
    }

    @Test
    @DisplayName("NoEntryCardCharacterCard getNoEntry test")
    public void testGetNoEntry(){
        assertEquals(4,card.getNoEntry());
    }
}