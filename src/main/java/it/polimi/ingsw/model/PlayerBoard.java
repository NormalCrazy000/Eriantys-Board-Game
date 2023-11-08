package it.polimi.ingsw.model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PlayerBoard board
 * This class represents the board of the player and every player has one.
 *
 * @author Maria Pia Marini
 */

public class PlayerBoard implements Serializable {
    private final Map<PawnType, Integer> entrance;
    private int towers;
    private TowerColor towerColor;
    private final Map<PawnType, Integer> diningRoom;
    private final Map<PawnType, Boolean> professors;
    private boolean characterCardProfessors;
    private int coins;

    /**
     * Constructor PlayerBoard creates instance of PlayerBoard and set up elements:
     * professor->value = 0;
     * diningRoom Students-> value 0 for each color
     * entrance Students -> value 0 for each color
     * tower -> value 0
     * coins -> value 0
     */
    public PlayerBoard() {
        professors = new HashMap<>();
        diningRoom = new HashMap<>();
        entrance = new HashMap<>();
        characterCardProfessors = false;

        //Setup elements on PlayerBoard
        for (PawnType color : PawnType.values()) {
            professors.put(color, false);
            diningRoom.put(color, 0);
            entrance.put(color, 0);
        }
        towers = 0;
        coins = 0;
        towerColor = null;
    }

    /**
     * Method setTowerColor sets the towerColor of this Player object.
     *
     * @param towerColor of this Player object.
     */
    public void setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    /**
     * Method getTowerColor returns the towerColor of this Player object.
     *
     * @return the towerColor (type TowerColor) of this Player object.
     */
    public TowerColor getTowerColor() {
        return this.towerColor;
    }

    /**
     * Method adds inputStudents into Entrance
     *
     * @param students type {@link Map} with keys {@link PawnType} and values {@link Integer}:  students to be added
     */
    public void addStudentsToEntrance(Map<PawnType, Integer> students) {
        for (Map.Entry<PawnType, Integer> entry : students.entrySet()) {
            entrance.put(entry.getKey(), entry.getValue() + entrance.get(entry.getKey()));
        }
    }

    /**
     * Method remove inputStudents from Entrance
     *
     * @param students (Map<PawnType,Integer> type): students to be removed
     */
    public void removeStudentFromEntrance(Map<PawnType, Integer> students) {
        for (Map.Entry<PawnType, Integer> entry : students.entrySet()) {
            entrance.put(entry.getKey(), entrance.get(entry.getKey()) - entry.getValue());
        }
    }

    /**
     * Method remove inputStudents from DiningRoom
     *
     * @param students type {@link Map} with keys {@link PawnType} and values {@link Integer}: students to be removed
     */
    public void removeStudentsFromDiningRoom(Map<PawnType, Integer> students) {
        for (Map.Entry<PawnType, Integer> entry : students.entrySet()) {
            diningRoom.put(entry.getKey(), diningRoom.get(entry.getKey()) - entry.getValue());
        }
    }

    /**
     * Method reverses diningRoom's and entrance's students selected
     *
     * @param studentsDiningRoom type {@link Map} with keys {@link PawnType} and values {@link Integer}: diningRoom's students to be removed
     * @param studentsEntrance   type {@link Map} with keys {@link PawnType} and values {@link Integer}: entrance's students to be removed
     * @param gameMode           type {@link GameMode}: gameMode is used to check if coins must be added
     * @param gameBoard          type {@link GameBoard} : it is used to remove reserve coins
     */
    public void replaceStudentDiningRoomAndEntrance(Map<PawnType, Integer> studentsDiningRoom, Map<PawnType, Integer> studentsEntrance, GameMode gameMode, GameBoard gameBoard) {
        //Remove students into entrance and diningRoom
        removeStudentsFromDiningRoom(studentsDiningRoom);
        removeStudentFromEntrance(studentsEntrance);
        //Replace students
        //TODO
        addStudentToDiningRoom(studentsEntrance, gameMode, gameBoard);
        addStudentsToEntrance(studentsDiningRoom);
        //studentsEntrance.forEach((key, value) -> diningRoom.put(key, value + diningRoom.get(key)));
        //studentsDiningRoom.forEach((key, value) -> entrance.put(key, value + entrance.get(key)));
    }

    /**
     * Method adds inputStudents into diningRoom and removes them from entrance.Add also coins if GameMode is GameMode.Expert
     *
     * @param students  type {@link Map} with keys {@link PawnType} and values {@link Integer}:: students to be added
     * @param gameMode  type {@link GameMode}: gameMode is used to check if coins must be added
     * @param gameBoard type {@link GameBoard} : it is used to remove reserve coins
     */
    public void addStudentToDiningRoomAndRemoveStudentsFromEntrance(Map<PawnType, Integer> students, GameMode gameMode, GameBoard gameBoard) {
        //Remove students into entrance
        removeStudentFromEntrance(students);

        //Add students into diningRoom
        addStudentToDiningRoom(students, gameMode, gameBoard);

    }

    /**
     * Method adds inputStudents into diningRoom.Add also coins if GameMode is GameMode.Expert.
     * it checks if adding students into dining room meets the following thresholds:
     * Add a coin each time position 3, 6 or 9 is reached
     *
     * @param students  type {@link Map} with keys {@link PawnType} and values {@link Integer}: students to be added
     * @param gameMode  type {@link GameMode}: gameMode is used to check if coins must be added
     * @param gameBoard type {@link GameBoard} : it is used to remove reserve coins
     */
    public void addStudentToDiningRoom(Map<PawnType, Integer> students, GameMode gameMode, GameBoard gameBoard) {
        //Check if gameMode is equal GameMode.EXPERT
        if (gameMode.equals(GameMode.EXPERT)) {
            //Add coins into playerBoard and remove them into GameBoard
            for (Map.Entry<PawnType, Integer> e : diningRoom.entrySet()) {
                //check if there are students to add by color
                if (students.get(e.getKey()) != null && students.get(e.getKey()) > 0) {
                    //This variable tracks number of students
                    int newStudentsIntoDiningRoom = e.getValue();
                    //cycle for each student
                    for (int i = 1; i <= students.get(e.getKey()); i++) {
                        newStudentsIntoDiningRoom++;
                        //checks whether position 3, 6 or 9 has been reached
                        if (newStudentsIntoDiningRoom == 3 || newStudentsIntoDiningRoom == 6 || newStudentsIntoDiningRoom == 9) {
                            //check if there are coins in the game board
                            if (gameBoard.getCoins() > 0) {
                                //add coins to the player and remove them from the game board
                                gameBoard.removeCoins(1);
                                addCoins(1);
                            }
                        }
                    }
                }
            }
        }
        //Add students into diningRoom
        for (Map.Entry<PawnType, Integer> entry : students.entrySet()) {
            diningRoom.put(entry.getKey(), entry.getValue() + diningRoom.get(entry.getKey()));
        }
    }

    /**
     * This method adds professor on board
     *
     * @param professorType type {@link PawnType}: professor's color to be added
     */
    public void addProfessor(PawnType professorType) {
        professors.put(professorType, true);
    }

    /**
     * This method removes professor on board
     *
     * @param professorType type {@link PawnType}: professor's color to be removed
     */
    public void removeProfessor(PawnType professorType) {
        professors.put(professorType, false);
    }

    /**
     * This method returns number of professors on board
     *
     * @return (type int): number of professors on board
     */
    public int checkNumberProfessors() {
        int numberOfProfessor = 0;
        for (Map.Entry<PawnType, Boolean> entry : professors.entrySet()) {
            if (entry.getValue() == true) {
                numberOfProfessor++;
            }
        }
        return numberOfProfessor;
    }

    /**
     * This method checks if color professor color is on board
     *
     * @param professorType ({@link MageType} type): color professor
     * @return (bool type): true if professor is on board, else false
     */
    public boolean checkColorProfessor(PawnType professorType) {
        return professors.get(professorType);
    }

    /**
     * This method sets number of towers on board
     *
     * @param numberOfTower (type int): number of towers to set
     */
    public void setNumberOfTower(int numberOfTower) {
        towers = numberOfTower;
    }

    /**
     * This method returns number of towers on board
     *
     * @return (type int): number of towers on board
     */
    public int getNumberOfTower() {
        return towers;
    }

    /**
     * This method adds towers on board
     *
     * @param numberOfTowers: number of towers to add
     */
    public void addTower(int numberOfTowers) {
        towers = towers + numberOfTowers;
    }

    /**
     * This method removes towers on board
     *
     * @param numberOfTowers: number of towers to remove
     */
    public void removeTower(int numberOfTowers) {
        towers = towers - numberOfTowers;
    }

    /**
     * This method return map with entrance's student
     * This map is sorted by th number of students in descending order
     *
     * @return type {@link Map} with keys {@link PawnType} and values {@link Integer}: entrance's students
     */
    public Map<PawnType, Integer> getAllStudentsOrderEntrance() {
        Map<PawnType, Integer> orderStudentEntrance = new LinkedHashMap<>();
        entrance
                .entrySet()
                .stream()
                .sorted((elem1, elem2) -> elem2.getValue().compareTo(elem1.getValue()))
                .forEach(e -> orderStudentEntrance.put(e.getKey(), e.getValue()));
        return orderStudentEntrance;
    }

    /**
     * This method return map with diningRoom's student
     * This map is sorted by th number of students in descending order
     *
     * @return type {@link Map} with keys {@link PawnType} and values {@link Integer}: diningRoom's students
     */
    public Map<PawnType, Integer> getAllStudentsOrderDiningRoom() {
        Map<PawnType, Integer> orderStudentDiningRoom = new LinkedHashMap<>();
        diningRoom
                .entrySet()
                .stream()
                .sorted((elem1, elem2) -> elem2.getValue().compareTo(elem1.getValue()))
                .forEach(e -> orderStudentDiningRoom.put(e.getKey(), e.getValue()));
        return orderStudentDiningRoom;
    }

    /**
     * This method return map with entrance's student
     *
     * @return type {@link Map} with keys {@link PawnType} and values {@link Integer}: entrance's students
     */
    public Map<PawnType, Integer> getAllStudentsEntrance() {
        return entrance;
    }

    /**
     * This method returns professor on dining room in order
     *
     * @return type Map with keys {@link PawnType} and values {@link Boolean}: professor on dining room in order
     */
    public Map<PawnType, Boolean> getAllProfessorsOrder() {
        Map<PawnType, Boolean> orderProfessors = new LinkedHashMap<>();
        diningRoom
                .entrySet()
                .stream()
                .sorted((elem1, elem2) -> elem2.getValue().compareTo(elem1.getValue()))
                .forEach(e -> orderProfessors.put(e.getKey(), e.equals(false)));
        return orderProfessors;
    }

    /**
     * This method returns professor on dining room
     *
     * @return type Map with keys {@link PawnType} and values {@link Boolean}: professor on dining room
     */
    public Map<PawnType, Boolean> getProfessors() {
        return professors;
    }

    /**
     * This method adds coins into board
     *
     * @param numberOfCoins (type int): number of coins to add
     */
    public void addCoins(int numberOfCoins) {
        coins = coins + numberOfCoins;
    }

    /**
     * This method is used to check characterCard
     *
     * @return type bool: characterCardProfessors value
     */
    public boolean isCharacterCardProfessors() {
        return characterCardProfessors;
    }

    /**
     * This method is used to set up characterCard
     *
     * @param characterCardProfessors type bool: characterCardProfessors value
     */
    public void setCharacterCardProfessors(boolean characterCardProfessors) {
        this.characterCardProfessors = characterCardProfessors;
    }

    /**
     * This method removes coins into board
     *
     * @param numberOfCoins (type int): number of coins to remove
     */
    public void removeCoins(int numberOfCoins) {
        coins = coins - numberOfCoins;
    }

    /**
     * This method returns number of coins on board
     *
     * @return (type int): number of coins
     */
    public int getCoins() {
        return coins;
    }
}
