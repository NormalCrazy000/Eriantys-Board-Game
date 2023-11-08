package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResolveIslandCharacterCardTest {
    ResolveIslandCharacterCard card;
    Game game;

    @BeforeEach
    public void setup(){
        game = new Game(GameMode.EXPERT,2);
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        player.getPlayerboard().getAllProfessorsOrder().put(PawnType.RED,true);
        player.getPlayerboard().setTowerColor(TowerColor.WHITE);

        card = new ResolveIslandCharacterCard("3","3","ResolveIsland","Choose an Island and resolve the Island as if Mother Nature had ended her movement there. Mother Nature will still move and the Island where she ends her movement will also be resolved.");
        card.setGame(game);
    }

    @AfterEach
    public void reset(){
        card = null;
        game = null;
    }

    //TODO testAction
    @Test
    @DisplayName("ResolveIslandCharacterCardTest action test")
    public void testAction(){

        TowerColor color = game.getGameBoard().getRegion(0).getColorTower();
        game.getGameBoard().getRegion(0).getAllStudentsOrder().put(PawnType.RED,6);

        assertEquals(null, color);

        card.setParameterToEffect(0);
        card.effect();

        assertEquals(TowerColor.WHITE,game.getGameBoard().getRegion(0).getColorTower());

    }
}