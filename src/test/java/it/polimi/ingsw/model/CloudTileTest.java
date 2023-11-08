package it.polimi.ingsw.model;

import it.polimi.ingsw.helper.HelperMap;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CloudTileTest {
    private CloudTile cloud;
    private Map<PawnType,Integer> students;

    @BeforeEach
    public void setup(){
        cloud = new CloudTile();
        students = new HashMap<>();
    }

    @AfterEach
    public void reset(){
        cloud = null;
        students = null;
    }

    @Test
    @DisplayName("CloudTile constructor test")
    public void testCloudTile(){
        HelperMap.mapStudentResetWithZeroValue(students);
        assertEquals(students,cloud.getAllStudents());
    }

    @ParameterizedTest
    @CsvSource(value = {"1:2:3:4:5"},delimiter = ':')
    @DisplayName("CloudTile addStudents test")
    public void testAddStudents(int redValue, int greenValue, int blueValue, int pinkValue, int yellowValue){
        HelperMap.mapStudentGenerator(students,redValue,greenValue,blueValue,pinkValue,yellowValue);
        cloud.addStudents(students);
        assertEquals(students,cloud.getAllStudents());
    }

    @ParameterizedTest
    @CsvSource(value = {"1:2:3:4:5"},delimiter = ':')
    @DisplayName("CloudTile NumberCloudStudents test")
    public void testCheckNumberCloudStudents(int redValue, int greenValue, int blueValue, int pinkValue, int yellowValue){
        HelperMap.mapStudentGenerator(students,redValue,greenValue,blueValue,pinkValue,yellowValue);
        cloud.addStudents(students);
        assertEquals(students,cloud.getAllStudents());
    }

    @ParameterizedTest
    @CsvSource(value = {"1:2:3:4:5"},delimiter = ':')
    @DisplayName("CloudTile removeStudentOnCloud test")
    public void testGetAllStudents(int redValue, int greenValue, int blueValue, int pinkValue, int yellowValue){
        HelperMap.mapStudentGenerator(students,redValue,greenValue,blueValue,pinkValue,yellowValue);
        cloud.addStudents(students);
        assertEquals(students, cloud.removeStudentOnCloud());
        HelperMap.mapStudentResetWithZeroValue(students);
        assertEquals(students,cloud.getAllStudents());
    }


    @ParameterizedTest
    @CsvSource(value = {"0:0:0:0:0"},delimiter = ':')
    @DisplayName("CloudTile removeStudentOnCloud test")
    public void testIsEmpty(int redValue, int greenValue, int blueValue, int pinkValue, int yellowValue){
        HelperMap.mapStudentGenerator(students,redValue,greenValue,blueValue,pinkValue,yellowValue);
        cloud.addStudents(students);
        assertEquals(students, cloud.removeStudentOnCloud());
        HelperMap.mapStudentResetWithZeroValue(students);
        assertEquals(students,cloud.getAllStudents());
        assertEquals(true,cloud.isEmpty());
    }


}