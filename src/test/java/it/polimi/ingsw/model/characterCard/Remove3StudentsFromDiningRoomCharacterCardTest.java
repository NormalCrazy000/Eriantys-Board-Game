package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.helper.HelperMap;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Remove3StudentsFromDiningRoomCharacterCardTest {
    Remove3StudentsFromDiningRoomCharacterCard card;
    Game game;

    @BeforeEach
    public void setup(){
        game = new Game(GameMode.EXPERT,3);
        Player first = new Player("first");
        Player second = new Player("second");
        Player third = new Player("third");
        game.addNewPlayerIntoGame(first);
        game.addNewPlayerIntoGame(second);
        game.addNewPlayerIntoGame(third);

        Map<PawnType,Integer> students_firstPlayer = new HashMap<>();
        Map<PawnType,Integer> students_secondPlayer = new HashMap<>();
        Map<PawnType,Integer> students_thirdPlayer = new HashMap<>();
        HelperMap.mapStudentGenerator(students_firstPlayer,4,1,0,4,6);
        HelperMap.mapStudentGenerator(students_secondPlayer,3,1,0,5,4);
        HelperMap.mapStudentGenerator(students_thirdPlayer,2,3,0,2,3);


        game.getPlayerByNickname("first").getPlayerboard().addStudentToDiningRoom(students_firstPlayer,game.getGameMode(),game.getGameBoard());
        game.getPlayerByNickname("second").getPlayerboard().addStudentToDiningRoom(students_secondPlayer,game.getGameMode(),game.getGameBoard());
        game.getPlayerByNickname("third").getPlayerboard().addStudentToDiningRoom(students_thirdPlayer,game.getGameMode(),game.getGameBoard());

        game.assignProfessor();

        card = new Remove3StudentsFromDiningRoomCharacterCard("12","3","Remove3StudentsFromDiningRoom","","Choose a type of Student: every player (including yourself) must return 3 Students of that type from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as many Students as they have.");
        card.setGame(game);
    }

    @AfterEach
    public void reset(){
        card = null;
        game = null;
    }

    @Test
    @DisplayName("Remove3StudentsFromDiningRoomCharacterCard action test")
    public void testAction(){

        // Tested with color red
        card.setParameterToEffect(PawnType.RED);

        card.effect();

        assertEquals( 1, game.getPlayerByNickname("first").getPlayerboard().getAllStudentsOrderDiningRoom().get(PawnType.RED));
        assertEquals( 0, game.getPlayerByNickname("second").getPlayerboard().getAllStudentsOrderDiningRoom().get(PawnType.RED));
        assertEquals( 0, game.getPlayerByNickname("third").getPlayerboard().getAllStudentsOrderDiningRoom().get(PawnType.RED));


        //check professor
        assertEquals(true, game.getPlayerByNickname("first").getPlayerboard().checkColorProfessor(PawnType.RED));
        assertEquals(true, game.getPlayerByNickname("first").getPlayerboard().checkColorProfessor(PawnType.YELLOW));
        assertEquals(false, game.getPlayerByNickname("first").getPlayerboard().checkColorProfessor(PawnType.PINK));
        assertEquals(false, game.getPlayerByNickname("first").getPlayerboard().checkColorProfessor(PawnType.BLUE));
        assertEquals(false, game.getPlayerByNickname("first").getPlayerboard().checkColorProfessor(PawnType.GREEN));

        assertEquals(false, game.getPlayerByNickname("second").getPlayerboard().checkColorProfessor(PawnType.RED));
        assertEquals(false, game.getPlayerByNickname("second").getPlayerboard().checkColorProfessor(PawnType.YELLOW));
        assertEquals(true, game.getPlayerByNickname("second").getPlayerboard().checkColorProfessor(PawnType.PINK));
        assertEquals(false, game.getPlayerByNickname("second").getPlayerboard().checkColorProfessor(PawnType.BLUE));
        assertEquals(false, game.getPlayerByNickname("second").getPlayerboard().checkColorProfessor(PawnType.GREEN));

        assertEquals(false, game.getPlayerByNickname("third").getPlayerboard().checkColorProfessor(PawnType.RED));
        assertEquals(false, game.getPlayerByNickname("third").getPlayerboard().checkColorProfessor(PawnType.YELLOW));
        assertEquals(false, game.getPlayerByNickname("third").getPlayerboard().checkColorProfessor(PawnType.PINK));
        assertEquals(false, game.getPlayerByNickname("third").getPlayerboard().checkColorProfessor(PawnType.BLUE));
        assertEquals(true, game.getPlayerByNickname("third").getPlayerboard().checkColorProfessor(PawnType.GREEN));


    }
}