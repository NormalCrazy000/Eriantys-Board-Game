package it.polimi.ingsw.model;

import it.polimi.ingsw.helper.HelperMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PlayerBoardTest class testes {@link PlayerBoard} class
 *
 * @author Maria Pia Marini
 * @see PlayerBoard
 */

class PlayerBoardTest {
    @Test
    @DisplayName("PlayerBoard constructor test")
    public void testPlayer() {
        //Setup
        PlayerBoard playerBoard = new PlayerBoard();
        Map<PawnType, Integer> students = new HashMap<>();
        Map<PawnType, Boolean> professors = new HashMap<>();
        HelperMap.mapStudentResetWithZeroValue(students);
        HelperMap.mapStudentResetWithFalseValue(professors);
        assertEquals(professors, playerBoard.getProfessors());
        assertEquals(students, playerBoard.getAllStudentsOrderDiningRoom());
        assertEquals(students, playerBoard.getAllStudentsOrderEntrance());
        assertEquals(0, playerBoard.getNumberOfTower());
        assertEquals(0, playerBoard.getCoins());
        assertEquals(null, playerBoard.getTowerColor());
        assertFalse(playerBoard.isCharacterCardProfessors());
    }

    @Test
    @DisplayName("PlayerBoard addStudentsToEntrance test")
    public void testAddPlayerToEntrance() {
        //Setup
        PlayerBoard playerBoard = new PlayerBoard();
        Map<PawnType, Integer> students = new HashMap<>();
        HelperMap.mapStudentGenerator(students, 1, 2, 3, 4, 5);
        playerBoard.addStudentsToEntrance(students);
        HelperMap.mapStudentGenerator(students, 3, 3, 3, 3, 3);
        playerBoard.addStudentsToEntrance(students);
        Map<PawnType, Integer> studentsToCheck = new HashMap<>();
        HelperMap.mapStudentGenerator(studentsToCheck, 4, 5, 6, 7, 8);

        //Check condition
        assertEquals(studentsToCheck, playerBoard.getAllStudentsOrderEntrance());
    }

    @Test
    @DisplayName("PlayerBoard addStudentsToEntrance test")
    public void testRemovePlayerFromEntrance() {
        //Setup
        PlayerBoard playerBoard = new PlayerBoard();
        Map<PawnType, Integer> students = new HashMap<>();
        HelperMap.mapStudentGenerator(students, 3, 3, 3, 3, 3);
        playerBoard.addStudentsToEntrance(students);

        //Remove student
        HelperMap.mapStudentGenerator(students, 2, 2, 2, 2, 2);
        playerBoard.removeStudentFromEntrance(students);
        Map<PawnType, Integer> studentsToCheck = new HashMap<>();
        HelperMap.mapStudentGenerator(studentsToCheck, 1, 1, 1, 1, 1);

        //Check condition
        assertEquals(studentsToCheck, playerBoard.getAllStudentsOrderEntrance());
    }

    @Test
    @DisplayName("PlayerBoard removeStudentsFromDiningRoom test")
    public void testRemoveStudentsFromDiningRoom() {
        //Setup
        PlayerBoard playerBoard = new PlayerBoard();
        Map<PawnType, Integer> students = new HashMap<>();
        HelperMap.mapStudentGenerator(students, 3, 3, 3, 3, 3);
        playerBoard.addStudentToDiningRoom(students, GameMode.EASY, new GameBoard(2));

        //Remove student
        HelperMap.mapStudentGenerator(students, 2, 2, 2, 2, 2);
        playerBoard.removeStudentsFromDiningRoom(students);
        Map<PawnType, Integer> studentsToCheck = new HashMap<>();
        HelperMap.mapStudentGenerator(studentsToCheck, 1, 1, 1, 1, 1);

        //Check condition
        assertEquals(studentsToCheck, playerBoard.getAllStudentsOrderDiningRoom());
    }

    @Test
    @DisplayName("PlayerBoard replaceStudentDiningRoomAndEntrance test")
    public void testReplaceStudentDiningRoomAndEntrance() {
        //Setup
        PlayerBoard playerBoard = new PlayerBoard();
        Map<PawnType, Integer> studentsDining = new HashMap<>();
        HelperMap.mapStudentGenerator(studentsDining, 3, 3, 3, 3, 3);
        playerBoard.addStudentToDiningRoom(studentsDining, GameMode.EASY, new GameBoard(2));


        Map<PawnType, Integer> studentsEntrance = new HashMap<>();
        HelperMap.mapStudentGenerator(studentsEntrance, 1, 1, 1, 1, 1);
        playerBoard.addStudentsToEntrance(studentsEntrance);
        playerBoard.replaceStudentDiningRoomAndEntrance(studentsDining, studentsEntrance, GameMode.EASY, new GameBoard(2));

        //Check condition
        assertEquals(studentsDining, playerBoard.getAllStudentsOrderEntrance());
        assertEquals(studentsEntrance, playerBoard.getAllStudentsOrderDiningRoom());
    }

    @Test
    @DisplayName("PlayerBoard addStudentToDiningRoomAndRemoveStudentsFromEntrance test")
    public void testAddStudentToDiningRoomAndRemoveStudentsFromEntrance() {
        //Setup
        PlayerBoard playerBoard = new PlayerBoard();
        Map<PawnType, Integer> studentsEntrance = new HashMap<>();
        HelperMap.mapStudentGenerator(studentsEntrance, 1, 1, 1, 1, 1);
        playerBoard.addStudentsToEntrance(studentsEntrance);
        playerBoard.addStudentToDiningRoomAndRemoveStudentsFromEntrance(studentsEntrance, GameMode.EASY, new GameBoard(2));

        //Check condition
        assertEquals(studentsEntrance, playerBoard.getAllStudentsOrderDiningRoom());
        HelperMap.mapStudentResetWithZeroValue(studentsEntrance);
        assertEquals(studentsEntrance, playerBoard.getAllStudentsOrderEntrance());

    }

    @Test
    @DisplayName("PlayerBoard addStudentToDiningRoom test(EasyMode)")
    public void testAddStudentToDiningRoomEasyMode() {
        //Setup
        PlayerBoard playerBoard = new PlayerBoard();
        Map<PawnType, Integer> studentsDining = new HashMap<>();
        HelperMap.mapStudentGenerator(studentsDining, 1, 1, 1, 1, 1);
        playerBoard.addStudentsToEntrance(studentsDining);
        GameBoard gameBoard = new GameBoard(2);
        int oldCoinPlayer = playerBoard.getCoins();
        int oldCoinGameBoard = gameBoard.getCoins();

        playerBoard.addStudentToDiningRoom(studentsDining, GameMode.EASY, gameBoard);

        //Check condition
        assertEquals(studentsDining, playerBoard.getAllStudentsOrderDiningRoom());
        assertEquals(oldCoinGameBoard, gameBoard.getCoins());
        assertEquals(oldCoinPlayer, playerBoard.getCoins());
    }
    @Test
    @DisplayName("PlayerBoard addStudentToDiningRoom test(ExpertMode no coin)")
    public void testAddStudentToDiningRoomExpertModeNoCoin() {
        //Setup
        PlayerBoard playerBoard = new PlayerBoard();
        Map<PawnType,Integer> studentsDining = new HashMap<>();
        HelperMap.mapStudentGenerator(studentsDining,1,1,1,1,1);
        playerBoard.addStudentsToEntrance(studentsDining);
        GameBoard gameBoard = new GameBoard(2);
        int oldCoinPlayer = playerBoard.getCoins();
        int oldCoinGameBoard = gameBoard.getCoins();

        playerBoard.addStudentToDiningRoom(studentsDining,GameMode.EXPERT,gameBoard);

        //Check condition
        assertEquals(studentsDining,playerBoard.getAllStudentsOrderDiningRoom());
        assertEquals(oldCoinGameBoard,gameBoard.getCoins());
        assertEquals(oldCoinPlayer,playerBoard.getCoins());


    }

    @Test
    @DisplayName("PlayerBoard addStudentToDiningRoom test(ExpertMode Coin)")
    public void testAddStudentToDiningRoomExpertModeCoin() {
        //Setup
        PlayerBoard playerBoard = new PlayerBoard();
        Map<PawnType,Integer> studentsDining = new HashMap<>();
        HelperMap.mapStudentGenerator(studentsDining,3,6,1,1,1);
        playerBoard.addStudentsToEntrance(studentsDining);
        GameBoard gameBoard = new GameBoard(2);
        int oldCoinPlayer = playerBoard.getCoins();
        int oldCoinGameBoard = gameBoard.getCoins();

        playerBoard.addStudentToDiningRoom(studentsDining,GameMode.EXPERT,gameBoard);

        //Check condition
        assertEquals(studentsDining,playerBoard.getAllStudentsOrderDiningRoom());
        assertEquals(oldCoinGameBoard-3,gameBoard.getCoins());
        assertEquals(oldCoinPlayer+3,playerBoard.getCoins());
    }

    @Test
    @DisplayName("PlayerBoard checkNumberProfessors test")
    public void testCheckNumberProfessors() {
        //Setup
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.addProfessor(PawnType.RED);
        //Check condition
        assertEquals(1,playerBoard.checkNumberProfessors());

    }
    @Test
    @DisplayName("PlayerBoard checkNumberProfessors test(zero professor)")
    public void testCheckNumberProfessorsZero() {
        //Setup
        PlayerBoard playerBoard = new PlayerBoard();
        //Check condition
        assertEquals(0,playerBoard.checkNumberProfessors());

    }

}