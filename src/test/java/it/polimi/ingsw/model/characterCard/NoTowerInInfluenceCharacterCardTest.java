package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoTowerInInfluenceCharacterCardTest {
    NoTowerInInfluenceCharacterCard card;
    Game game;

    @BeforeEach
    public void setup(){
        game = new Game(GameMode.EXPERT,2);
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        card = new NoTowerInInfluenceCharacterCard("6","3","NoTowerInInfluence","When Resolving a Conquering on an Island, Towers do not count towards influence.");
        card.setGame(game);

    }

    @AfterEach
    public void reset(){
        card = null;
        game = null;
    }

    @Test
    @DisplayName("NoTowerInInfluenceCharacterCard action test")
    public void testAction(){
        card.effect();
        assertTrue(game.isNoTowerInfluence());
    }

    @Test
    @DisplayName("NoTowerInInfluenceCharacterCard resetEffect test")
    public void testResetEffect(){
        card.effect();
        card.resetEffect("nickname");
        assertFalse(game.isNoTowerInfluence());
    }
}