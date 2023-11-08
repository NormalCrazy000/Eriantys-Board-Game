package it.polimi.ingsw.model;

import it.polimi.ingsw.helper.HelperMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IslandRegionTest {
    private IslandRegion region;
    private Map<PawnType,Integer> students;

    @BeforeEach
    public void setup(){
        region = new IslandRegion();
        students = new HashMap<>();
    }

    @AfterEach
    public void reset(){
        region = null;
        students = null;
    }

    @Test
    @DisplayName("IslandRegion constructor test")
    public void testIslandRegion(){
        HelperMap.mapStudentResetWithZeroValue(students);
        assertEquals(students,region.getAllStudentsOrder());
        assertFalse(region.isMother());
        assertEquals(0,region.numberOfTowers());
        assertNull(region.getColorTower());
    }

    @Test
    @DisplayName("IslandRegion addTower test")
    public void testAddTower(){
        region.addTower(5);
        assertEquals(5,region.numberOfTowers());
    }

    @Test
    @DisplayName("IslandRegion removeAllTower test")
    public void testRemoveAllTower(){
        region.addTower(5);
        assertEquals(5,region.numberOfTowers());
        region.removeAllTower();
        assertEquals(0, region.numberOfTowers());
        assertNull(region.getColorTower());
    }

    @ParameterizedTest
    @EnumSource(TowerColor.class) // passing all 12 months
    @DisplayName("IslandRegion getColorTower test")
    void testGetTower(TowerColor color) {
        region.setTower(color);
        assertEquals(color,region.getColorTower());
    }

    @ParameterizedTest
    @CsvSource(value = {"true","false"})
    @DisplayName("IslandRegion isMother test")
    public void testIsMother(boolean value){
        region.setMother(value);
        assertEquals(value,region.isMother());
    }

    @ParameterizedTest
    @EnumSource(TowerColor.class)
    @DisplayName("IslandRegion isTower test")
    public void testIsTower(TowerColor color){
        region.setTower(color);
        assertTrue(region.isTower());
    }

    @Test
    @DisplayName("IslandRegion isTower null test")
    public void testNotIsTower(){
        assertFalse(region.isTower());
    }

    @ParameterizedTest
    @CsvSource(value = {"5:6:7:8:9"},delimiter = ':')
    @DisplayName("IslandRegion getAllStudentsOrder test(order)")
    public void testGetAllStudentsOnRegion(int redValue, int greenValue, int blueValue, int pinkValue, int yellowValue){
        HelperMap.mapStudentGenerator(students,redValue,greenValue,blueValue,pinkValue,yellowValue);
        region.addStudents(students);
        Map<PawnType, Integer> orderStudents = region.getAllStudentsOrder();
        ArrayList<Integer> orderValueStudents = new ArrayList<>(orderStudents.values());
        ArrayList<PawnType> orderKeyStudents = new ArrayList<>(orderStudents.keySet());
        //Check order value
        assertEquals(yellowValue, orderValueStudents.get(0));
        assertEquals(pinkValue, orderValueStudents.get(1));
        assertEquals(blueValue, orderValueStudents.get(2));
        assertEquals(greenValue, orderValueStudents.get(3));
        assertEquals(redValue, orderValueStudents.get(4));
        //Check order key
        assertEquals(PawnType.YELLOW,orderKeyStudents.get(0));
        assertEquals( PawnType.PINK,orderKeyStudents.get(1));
        assertEquals( PawnType.BLUE,orderKeyStudents.get(2));
        assertEquals(PawnType.GREEN,orderKeyStudents.get(3));
        assertEquals(PawnType.RED,orderKeyStudents.get(4));
    }
    //TODO
    @Test
    @DisplayName("IslandRegion NoEntry test")
    public void testGetNoEntry(){
        int oldNoEntry = region.getNoEntry();
        region.addNoEntry();
        int newNoEntry = region.getNoEntry();
        assertEquals(oldNoEntry+1,newNoEntry);
    }

    @Test
    @DisplayName("IslandRegion addNoEntry test")
    public void testAddNoEntry(){
        int oldNoEntry = region.getNoEntry();
        region.addNoEntry();
        int newNoEntry = region.getNoEntry();
        assertEquals(oldNoEntry+1,newNoEntry);
    }

    @Test
    @DisplayName("IslandRegion removeNoEntry test")
    public void testRemoveNoEntry(){
        region.addNoEntry();
        int oldNoEntry = region.getNoEntry();
        region.removeNoEntry();
        int newNoEntry = region.getNoEntry();
        assertEquals(oldNoEntry-1,newNoEntry);
    }
}