package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Region Class.
 * noEntry variable is used to manage the effect of the characters(in particular {@link it.polimi.ingsw.model.characterCard.NoEntryCardCharacterCard}
 *
 * @author Gabriele Giannotto
 */
public class IslandRegion implements Serializable {
    private boolean mother;
    private TowerColor tower;
    private int numberOfTowers;
    private final Map<PawnType, Integer> students;
    //This variable represents the region ban
    private int noEntry;

    /**
     * Class constructor.
     * It initializes  number of student(Red,Green,Blue,Pink,Yellow) to zero
     */
    public IslandRegion() {
        students = new HashMap<>();
        for (PawnType c : PawnType.values()) {
            students.put(c, 0);
        }
        mother = false;
        numberOfTowers = 0;
        tower = null;
        noEntry = 0;
    }

    /**
     * This method adds students to region
     *
     * @param addStudents type {@link Map} with keys {@link PawnType} and values {@link Integer}: students to add
     */
    public void addStudents(Map<PawnType, Integer> addStudents) {
        for (Map.Entry<PawnType, Integer> entry : addStudents.entrySet()) {
            students.put(entry.getKey(), entry.getValue() + students.get(entry.getKey()));
        }
    }

    /**
     * This method returns tower's color
     *
     * @return type {@link PawnType}: Tower's color
     */
    public TowerColor getColorTower() {
        return tower;
    }

    /**
     * This method adds towers on Region
     *
     * @param towers (int value): Tower's number
     */
    public void addTower(int towers) {
        numberOfTowers = numberOfTowers + towers;
    }

    /**
     * This method removes all towers on Region
     * This
     */
    public void removeAllTower() {
        //reset number and color tower
        tower = null;
        numberOfTowers = 0;
    }

    /**
     * This method is used to check number of towers on region
     *
     * @return (type int): number of towers
     */
    public int numberOfTowers() {
        return numberOfTowers;
    }

    /**
     * This method is used to check if tower is present on region
     *
     * @return (type bool): true if tower/towers is on region, else false
     */
    public boolean isTower() {
        return getColorTower() != null;
    }

    /**
     * This method is used to check if mother is on the region
     *
     * @return (bool type): true if mother is on island, else false
     */
    public boolean isMother() {
        return mother;
    }

    /**
     * This method is used to add mother on region
     *
     * @param mother (bool type): value of mother(true or false)
     */
    public void setMother(boolean mother) {
        this.mother = mother;
    }

    /**
     * It sets new tower's color
     *
     * @param tower (type {@link PawnType}:): tower's color
     */
    public void setTower(TowerColor tower) {
        this.tower = tower;
    }

    /**
     * It returns region student in order(from color with more students)
     *
     * @return type {@link Map} with keys {@link PawnType} and values {@link Integer}: students on board in descending order
     */
    public Map<PawnType, Integer> getAllStudentsOrder() {
        Map<PawnType, Integer> orderStudentRegion = new LinkedHashMap<>();
        students
                .entrySet()
                .stream()
                .sorted((elem1, elem2) -> elem2.getValue().compareTo(elem1.getValue()))
                .forEach(e -> orderStudentRegion.put(e.getKey(), e.getValue()));
        return orderStudentRegion;
    }

    /**
     * This method returns number of bans placed on the region
     *
     * @return (type int): number of bans
     */
    public int getNoEntry() {
        return noEntry;
    }

    /**
     * This method adds one ban on the region
     */
    public void addNoEntry() {
        noEntry++;
    }

    /**
     * This method removes one ban on the region
     */
    public void removeNoEntry() {
        noEntry--;
    }
}
