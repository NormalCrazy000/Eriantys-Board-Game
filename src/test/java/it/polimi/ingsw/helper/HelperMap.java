package it.polimi.ingsw.helper;

import it.polimi.ingsw.model.PawnType;

import java.util.Map;

public class  HelperMap {
    /**
     * Helper function to generate map with student
     * @param map (Map<PawnType,Integer> type) map
     * @param redValue (int value) number of red student
     * @param greenValue (int value) number of green student
     * @param blueValue (int value) number of blue student
     * @param pinkValue (int value) number of pink student
     * @param yellowValue (int value) number of yellow student
     */
    public static void mapStudentGenerator(Map<PawnType,Integer> map, int redValue, int greenValue, int blueValue, int pinkValue, int yellowValue){
        map.put(PawnType.RED,redValue);
        map.put(PawnType.GREEN,greenValue);
        map.put(PawnType.BLUE,blueValue);
        map.put(PawnType.PINK,pinkValue);
        map.put(PawnType.YELLOW,yellowValue);
    }

    /**
     * Helper function to reset number of student into map(zero value for each color)
     * @param map (Map<PawnType,Integer> type) map
     */
    public static void mapStudentResetWithZeroValue(Map<PawnType,Integer> map){
        for (PawnType color: PawnType.values()){
            map.put(color,0);
        }
    }

    /**
     * Helper function to reset number of student into map(false value for each color)
     * @param map (Map<PawnType,Integer> type) map
     */
    public static void mapStudentResetWithFalseValue(Map<PawnType,Boolean> map){
        for (PawnType color: PawnType.values()){
            map.put(color,false);
        }
    }
}
