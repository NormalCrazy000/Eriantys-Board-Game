package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Cloud Class
 *
 * @author Gabriele Giannotto
 */
public class CloudTile implements Serializable {

    private final Map<PawnType, Integer> students;

    /**
     * Class constructor.
     * It initializes  number of student(Red,Green,Blue,Pink,Yellow) to zero
     */
    public CloudTile() {
        students = new HashMap<>();
        for (PawnType c : PawnType.values()) {
            students.put(c, 0);
        }
    }

    /**
     * It adds students(Red,Green,Blue,Pink,Yellow) to cloud
     *
     * @param addStudents type {@link Map} with keys {@link PawnType} and values {@link Integer}: students map to add
     */
    public void addStudents(Map<PawnType, Integer> addStudents) throws IllegalArgumentException {

        students.putAll(addStudents);
    }

    /**
     * It returns students on cloud
     *
     * @return type {@link Map} with keys {@link PawnType} and values {@link Integer}: cloud's students
     */
    public Map<PawnType, Integer> getAllStudents() {
        return students;
    }

    /**
     * It returns and removes cloud's students
     *
     * @return type {@link Map} with keys {@link PawnType} and values {@link Integer}: cloud's students
     */
    public Map<PawnType, Integer> removeStudentOnCloud() {
        Map<PawnType, Integer> playableStudents = new HashMap<>(students);
        students.replaceAll((k, v) -> 0);
        return playableStudents;
    }


    /**
     * It returns true if there are no students on the cloud
     */
    public boolean isEmpty() {
        return students.get(PawnType.RED) == 0 && students.get(PawnType.GREEN) == 0 && students.get(PawnType.BLUE) == 0 &&
                students.get(PawnType.YELLOW) == 0 && students.get(PawnType.PINK) == 0;
    }
}

