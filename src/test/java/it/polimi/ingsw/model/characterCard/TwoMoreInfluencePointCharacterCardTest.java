package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwoMoreInfluencePointCharacterCardTest {
    TwoMoreInfluencePointCharacterCard card;
    Game game;

    @BeforeEach
    public void setup(){
        game = new Game(GameMode.EXPERT,2);
        card = new TwoMoreInfluencePointCharacterCard("8","2","TwoMoreInfluencePoint","During the influence calculation this turn, you count as having 2 more influence");
        card.setGame(game);

        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);

    }

    @AfterEach
    public void reset(){
        card = null;
        game = null;
    }

    @Test
    @DisplayName("TakeControlOfProfessorCharacterCardTest action test")
    public void testAction(){
        card.effect();
        assertEquals(2,game.getPlayerByNickname("nickname").getAdditionalInfluence());
    }

    @Test
    @DisplayName("TakeControlOfProfessorCharacterCardTest resetEffect test")
    public void testResetEffect(){
        card.effect();
        card.resetEffect("nickname");
        assertEquals(0,game.getPlayerByNickname("nickname").getAdditionalInfluence());
    }
}