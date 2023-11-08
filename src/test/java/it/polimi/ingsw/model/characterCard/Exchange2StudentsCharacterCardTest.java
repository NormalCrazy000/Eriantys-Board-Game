package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.helper.HelperMap;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Exchange2StudentsCharacterCardTest {
    Exchange2StudentsCharacterCard card;
    Game game;

    @BeforeEach
    public void setup() {
        game = new Game(GameMode.EXPERT, 2);
        card = new Exchange2StudentsCharacterCard("10", "1", "Exchange2Students", "You may exchange up to 2 Students between your Entrance and your Dining Room.");
        card.setGame(game);
    }

    @AfterEach
    public void reset() {
        card = null;
        game = null;

    }

    @Test
    @DisplayName("Exchange2StudentsCharacterCardController action test")
    public void testAction() {
        Player player = new Player("nickname");
        game.addNewPlayerIntoGame(player);
        Map<PawnType, Integer> studentsDiningRoom, studentsEntrance;
        studentsDiningRoom = new HashMap<>();
        studentsEntrance = new HashMap<>();
        HelperMap.mapStudentGenerator(studentsDiningRoom, 2, 2, 0, 2, 2);
        HelperMap.mapStudentGenerator(studentsEntrance, 2, 2, 2, 2, 0);
        player.getPlayerboard().addStudentsToEntrance(studentsEntrance);
        player.getPlayerboard().addStudentToDiningRoom(studentsDiningRoom, GameMode.EASY, game.getGameBoard());
        Map<PawnType, Integer> studentsDiningRoomReplace, studentsToEntranceReplace;
        studentsDiningRoomReplace = new HashMap<>();
        studentsToEntranceReplace = new HashMap<>();
        HelperMap.mapStudentGenerator(studentsDiningRoomReplace, 0, 0, 0, 0, 2);
        HelperMap.mapStudentGenerator(studentsToEntranceReplace, 0, 0, 2, 0, 0);
        card.setParameterToEffect(studentsDiningRoomReplace, studentsToEntranceReplace);
        card.effect();

        assertEquals(studentsDiningRoom, player.getPlayerboard().getAllStudentsOrderEntrance());
        assertEquals(studentsEntrance, player.getPlayerboard().getAllStudentsOrderDiningRoom());
    }

}