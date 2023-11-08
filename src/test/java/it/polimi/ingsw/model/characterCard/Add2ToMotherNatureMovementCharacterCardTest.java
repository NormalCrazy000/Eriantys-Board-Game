package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Add2ToMotherNatureMovementCharacterCardTest {
    Add2ToMotherNatureMovementCharacterCard card;
    Game game;

    @BeforeEach
    public void setup(){
        game = new Game(GameMode.EXPERT,2);
        card = new Add2ToMotherNatureMovementCharacterCard("4","1","Add2ToMotherNatureMovement","You may move Mother Nature up to 2 additional Island than is indicated by the Assistant card you've played.");
        card.setGame(game);
    }

    @AfterEach
    public void reset(){
        card = null;
        game = null;
    }

    @Test
    @DisplayName("Add2ToMotherNatureMovementCharacterCard action test")
    public void testAction(){
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        card.effect();
        assertEquals(2,player.getAdditionalMovement());
    }

    @Test
    @DisplayName("Add2ToMotherNatureMovementCharacterCard resetEffect test")
    public void testResetEffect(){
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        card.effect();
        card.resetEffect("nickname");
        assertEquals(0,player.getAdditionalMovement());
    }

}