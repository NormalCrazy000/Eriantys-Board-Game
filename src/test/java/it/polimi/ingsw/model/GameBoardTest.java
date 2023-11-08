package it.polimi.ingsw.model;

import it.polimi.ingsw.helper.HelperMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    private Map<PawnType,Integer> students;

    @BeforeEach
    public void setup(){
        //gameBoard = new GameBoard(2);
        //linkedHashMap to maintain order
        students = new LinkedHashMap<>();
    }

    @AfterEach
    public void reset(){
        students = null;
    }

    @ParameterizedTest
    @CsvSource(value = {"2","3","4"})
    @DisplayName("GameBoard constructor test")
    public void testGameBoard(int numberPlayers){
        GameBoard gameBoardConstructor = new GameBoard(numberPlayers);
        IslandRegion region;
        assertEquals(12, gameBoardConstructor.checkNumberOfRegions());
        assertEquals(120, gameBoardConstructor.checkNumberOfStudentsOnBag());
        assertEquals(numberPlayers, gameBoardConstructor.checkNumberOfClouds());
        //Check setup student number(2 for each color)
        HelperMap.mapStudentGenerator(students,2,2,2,2,2);
        Map<PawnType,Integer> studentOfRegions = new HashMap<>();
        HelperMap.mapStudentResetWithZeroValue(studentOfRegions);
        int indexMother = 0;
        int indexRegionInFrontMother = 0;
        for(int i = 0; i < 12;i++){
            region = gameBoardConstructor.getRegion(i);
            if(region.isMother()){
                indexMother=i;
            }
            for(PawnType color:PawnType.values()){
                studentOfRegions.replace(color,studentOfRegions.get(color) + region.getAllStudentsOrder().get(color));
            }
        }
        assertEquals(students,studentOfRegions);
        //Check position mother and region in front of mother.
        HelperMap.mapStudentResetWithZeroValue(students);
        for(int i = 0; i < 12;i++){
           if(Math.abs(i-indexMother)==6) indexRegionInFrontMother = i;
        }
        assertEquals(students,gameBoardConstructor.getRegion(indexMother).getAllStudentsOrder());
        assertEquals(students,gameBoardConstructor.getRegion(indexRegionInFrontMother).getAllStudentsOrder());
        assertEquals(indexMother,gameBoardConstructor.getIndexMother());
    }
    //TODO
    @ParameterizedTest
    @CsvFileSource(resources = "/helper_GameBoardTest.cvs",delimiter = ':')
    @DisplayName("GameBoard getStudentsFromBag test")
    public void testGetStudentsFromBag(int numberStudentBag,int numberOfPlayers){
        GameBoard gameBoard = new GameBoard(numberOfPlayers);
        int numberBagBefore = gameBoard.checkNumberOfStudentsOnBag();
        Map<PawnType,Integer> studentFromBag =  gameBoard.getStudentsFromBag(numberStudentBag);
        assertEquals(numberBagBefore - numberStudentBag, gameBoard.checkNumberOfStudentsOnBag());
        assertEquals(numberStudentBag, studentFromBag.values().stream().mapToInt(e -> e).sum());
    }

    @ParameterizedTest
    @CsvSource(value = {"2","3","4"})
    @DisplayName("GameBoard isStudentsBagEmpty test")
    public void testIsStudentsBagEmpty(int numberOfPlayers){
        GameBoard gameBoard = new GameBoard(numberOfPlayers);
        Map<PawnType,Integer> studentFromBag =  gameBoard.getStudentsFromBag(120);
        assertTrue(gameBoard.isStudentsBagEmpty());
    }

    @ParameterizedTest
    @CsvSource(value = {"0:2","1:2","2:2","3:2","4:2","5:2","6:2","7:2","8:2","9:2","10:2","11:2","0:3","1:3","2:3","3:3","4:3","5:3","6:3","7:3","8:3","9:3","10:3","11:3","0:4","1:4","2:4","3:4","4:4","5:4","6:4","7:4","8:4","9:4","10:4","11:4"},delimiter = ':')
    @DisplayName("GameBoard getRegion test")
    public void testGetIslandRegion(int indexRegion,int numberOfPlayers){
        GameBoard gameBoard = new GameBoard(numberOfPlayers);
        IslandRegion region= gameBoard.getRegion(indexRegion);
        Map<PawnType,Integer> studentIntoRegion = region.getAllStudentsOrder();
        students = gameBoard.getStudentsFromBag(7);
        region.addStudents(students);
        //Add into students element of studentIntoRegion
        for(PawnType color:PawnType.values()){
            students.put(color,students.get(color) + studentIntoRegion.get(color));
        }
        assertEquals(students,region.getAllStudentsOrder());
    }

    @ParameterizedTest
    @CsvSource(value = {"2:0","2:1","3:0","3:1","3:2","4:0","4:1","4:2","4:3"},delimiter = ':')
    @DisplayName("GameBoard getCloud test")
    public void testGetCloudTile(int numberPlayers,int indexCloud){
        GameBoard gameBoardTestCloud = new GameBoard(numberPlayers);
        CloudTile cloud = gameBoardTestCloud.getCloud(indexCloud);
        students = gameBoardTestCloud.getStudentsFromBag(7);
        cloud.addStudents(students);
        assertEquals(students,cloud.removeStudentOnCloud());
    }

    @ParameterizedTest
    @CsvSource(value = {"2","3","4"})
    @DisplayName("GameBoard islandAggregation tower test")
    public void testCheckIslandAggregation(int numberOfPlayers){
        GameBoard gameBoard = new GameBoard(numberOfPlayers);
        for(int i = 0; i<gameBoard.checkNumberOfRegions(); i++){
            if(i%2==0){
                gameBoard.getRegion(i).setTower(TowerColor.BLACK);
            }else{
                gameBoard.getRegion(i).setTower(TowerColor.WHITE);
            }
        }
        gameBoard.getRegion(0).setTower(TowerColor.GREY);
        gameBoard.getRegion(1).setTower(TowerColor.GREY);
        gameBoard.getRegion(0).addTower(1);
        gameBoard.getRegion(1).addTower(1);
        Map<PawnType,Integer> studentsOnRegion0 = gameBoard.getStudentsFromBag(5);
        Map<PawnType,Integer> studentsOnRegion1 = gameBoard.getStudentsFromBag(5);
        gameBoard.getRegion(0).addStudents(studentsOnRegion0);
        gameBoard.getRegion(1).addStudents(studentsOnRegion1);
        gameBoard.getRegion(1).setMother(true);
        Map<PawnType,Integer> studentsOnRegions0And1 = new LinkedHashMap<>();
        HelperMap.mapStudentResetWithZeroValue(studentsOnRegions0And1);
        studentsOnRegions0And1.replaceAll((k,v)->gameBoard.getRegion(0).getAllStudentsOrder().get(k) + gameBoard.getRegion(1).getAllStudentsOrder().get(k));
        //TODO
//        try{
            gameBoard.checkIslandAggregation();
//        }catch (MinRegionAggregationException e){
//            fail();
//        }
        assertEquals(11, gameBoard.checkNumberOfRegions());
        assertTrue(gameBoard.getRegion(0).isMother());
        assertSame(TowerColor.GREY,gameBoard.getRegion(0).getColorTower());
        assertEquals(studentsOnRegions0And1,gameBoard.getRegion(0).getAllStudentsOrder());
        assertEquals(2,gameBoard.getRegion(0).numberOfTowers());
        assertEquals(0,gameBoard.getRegion(1).numberOfTowers());

    }

    @ParameterizedTest
    @CsvSource(value = {"2","3","4"})
    @DisplayName("GameBoard islandAggregation nothing tower into regions test")
    public void testNullTowerIntoRegionIslandAggregation(int numberOfPlayers){
        GameBoard gameBoard = new GameBoard(numberOfPlayers);
//        try{
            gameBoard.checkIslandAggregation();
//        }catch (MinRegionAggregationException e){
//            fail();
//        }
        assertEquals(12, gameBoard.checkNumberOfRegions());
    }


    @ParameterizedTest
    @CsvSource(value = {"2:13","3:13","4:13","2:1","3:1","4:1"},delimiter = ':')
    @DisplayName("GameBoard addMotherOnRegion test")
    public void testAddMotherOnRegion(int numberOfPlayers,int movement){
        GameBoard gameBoard = new GameBoard(numberOfPlayers);
        int indexMotherOld = gameBoard.getIndexMother();
        gameBoard.moveMotherNature(movement);

        int newIndexMother = indexMotherOld + movement;
        while(newIndexMother>=gameBoard.checkNumberOfRegions()){
            newIndexMother = newIndexMother - gameBoard.checkNumberOfRegions();
        }
        assertEquals(newIndexMother,gameBoard.getIndexMother());
        assertTrue(gameBoard.getRegion(newIndexMother).isMother());
        for(int i = 0; i<gameBoard.checkNumberOfRegions(); i++){
            if(i!=newIndexMother){
                assertFalse(gameBoard.getRegion(i).isMother());
            }
        }
    }

    @Test
    @DisplayName("GameBoard addPlayerIntoBag test")
    public void testAddPlayerIntoBag(){
        GameBoard gameBoard = new GameBoard(2);
        Map<PawnType,Integer> studentsFromBag = gameBoard.getStudentsFromBag(10);
        int oldNumbersStudentsIntoBag = gameBoard.checkNumberOfStudentsOnBag();
        Map<PawnType,Integer> studentsToBag = new HashMap<>();
        HelperMap.mapStudentGenerator(studentsFromBag,2,2,2,2,2);
        gameBoard.addPlayerIntoBag(studentsFromBag);
        assertEquals(oldNumbersStudentsIntoBag + 10,gameBoard.checkNumberOfStudentsOnBag());
    }

    @Test
    @DisplayName("GameBoard addCoins test")
    public void testAddCoins(){
        GameBoard gameBoard = new GameBoard(2);
        int oldMoney = gameBoard.getCoins();
        gameBoard.addCoins(5);
        assertEquals(oldMoney + 5,gameBoard.getCoins());
    }

    @Test
    @DisplayName("GameBoard removeCoins test")
    public void testRemoveCoins(){
        GameBoard gameBoard = new GameBoard(2);
        int oldMoney = gameBoard.getCoins();
        gameBoard.removeCoins(5);
        assertEquals(oldMoney - 5,gameBoard.getCoins());
    }

    //TODO getMoney
}