package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TakeControlOfProfessorCharacterCardTest {
    TakeControlOfProfessorCharacterCard card;
    Game game;

    @BeforeEach
    public void setup(){
        game = new Game(GameMode.EXPERT,2);
        Player first = new Player("first");
        Player second = new Player("second");
        game.addNewPlayerIntoGame(first);
        game.addNewPlayerIntoGame(second);
        card = new TakeControlOfProfessorCharacterCard("2","2","TakeControlOfProfessor","During this turn, you take control of any number of Professor even if you have the same number of Students as the player who currently controls them.");
        card.setGame(game);

    }

    @AfterEach
    public void reset(){
        card = null;
        game = null;
    }

    @Test
    @DisplayName("TakeControlOfProfessorCharacterCardTest action test")
    public void testAction(){

        HashMap<PawnType,Integer> stud_first = new HashMap<>();
        stud_first.put(PawnType.RED,1);

        game.getPlayerByNickname("first").getPlayerboard().addStudentToDiningRoom(stud_first,game.getGameMode(),game.getGameBoard());

        HashMap<PawnType,Integer> stud_second = new HashMap<>();
        stud_second.put(PawnType.BLUE,1);

        game.getPlayerByNickname("second").getPlayerboard().addStudentToDiningRoom(stud_second,game.getGameMode(),game.getGameBoard());

        game.assignProfessor();

        for(PawnType color : PawnType.values()){
            if(color == PawnType.RED){
                assertEquals(true, game.getPlayerByNickname("first").getPlayerboard().checkColorProfessor(color));
            }else {
                assertEquals(false, game.getPlayerByNickname("first").getPlayerboard().checkColorProfessor(color));
            }
        }

        for(PawnType color : PawnType.values()){
            if(color == PawnType.BLUE ){
                assertEquals(true, game.getPlayerByNickname("second").getPlayerboard().checkColorProfessor(color));
            } else {
                assertEquals(false, game.getPlayerByNickname("second").getPlayerboard().checkColorProfessor(color));
            }
        }

        card.effect();

        for(PawnType color : PawnType.values()){
            if(color== PawnType.BLUE){
                assertEquals(false, game.getPlayerByNickname("first").getPlayerboard().checkColorProfessor(color));
            } else{
                assertEquals(true, game.getPlayerByNickname("first").getPlayerboard().checkColorProfessor(color));
            }
        }

        for(PawnType color : PawnType.values()){
            if(color == PawnType.BLUE ){
                assertEquals(true, game.getPlayerByNickname("second").getPlayerboard().checkColorProfessor(color));
            } else {
                assertEquals(false, game.getPlayerByNickname("second").getPlayerboard().checkColorProfessor(color));
            }
        }

    }

    @Test
    @DisplayName("TakeControlOfProfessorCharacterCardTest resetEffect test")
    public void testResetEffect(){

        card.effect();
        card.resetEffect("first");
        assertNull( game.getPlayerByNickname("first").getCharacterCardPlayed());

    }
}