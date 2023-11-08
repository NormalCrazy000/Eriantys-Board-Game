package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PawnType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Take1StudentToDiningRoomCharacterCard class
 * This class implements effect of card number 11:
 * In setup, draw 4 Students and place them on this card.
 * Take 1 Student from this card and place it in your Dining Room. Then, draw a new Student from the bag and place it on this card.
 *
 * @author Christian Lisi
 */
public class Take1StudentToDiningRoomCharacterCard extends CharacterCard implements Serializable {
    Map<PawnType, Integer> studentsCard;
    PawnType studentColor;

    /**
     * Constructor method that sets information of card and associates object Game to card
     *
     * @param ID                type {@link String}: card's ID
     * @param cost              type {@link String}: card's cost
     * @param effectType        type {@link String}: card's type
     * @param setup             type {@link String}: card's type
     * @param effectDescription type {@link String}: card's effect
     */
    public Take1StudentToDiningRoomCharacterCard(String ID, String cost, String effectType, String setup, String effectDescription, Game game) {
        setID(ID);
        setCost(Integer.parseInt(cost));
        setEffectType(effectType);
        setSetup(setup);
        setEffectDescription(effectDescription);
        setGame(game);
        studentsCard = getGame().getGameBoard().getStudentsFromBag(4);
    }

    /**
     * This method sets parameter that will be used to active effect
     *
     * @param student type {@link PawnType}: student's color  to be added on dining room
     */
    public void setParameterToEffect(PawnType student) {
        this.studentColor = student;
    }

    /**
     * This method removes students on card
     *
     * @param color type {@link PawnType} with keys {@link PawnType} and values {@link Integer}: student to remove
     */
    private void removeStudentsCard(PawnType color) {
        studentsCard.put(color, studentsCard.get(color) - 1);
    }

    /**
     * This method adds students on card
     */
    private void addStudentsCard() {

        //check if bag is empty, if so set last turn
        if (getGame().getGameBoard().getStudentsBag().values().stream().mapToInt(Integer::intValue).sum() == 0) {
            getGame().setLastTurn(true);
        } else {
            //if not get students
            Map<PawnType, Integer> studToAdd = getGame().getGameBoard().getStudentsFromBag(1);
            studentsCard.put(
                    //get the Pawn type
                    studToAdd.keySet().iterator().next(),
                    // add a +1 to that pawn type in studentsCard
                    studentsCard.get(studToAdd.keySet().iterator().next()) + 1
            );
        }
    }

    /**
     * This method actives card's effect
     */
    @Override
    public void effect() {

        removeStudentsCard(studentColor);
        HashMap<PawnType, Integer> stud = new HashMap<>();
        stud.put(studentColor, 1);

        getGame().getCurrentPlayer().getPlayerboard().addStudentToDiningRoom(stud, getGame().getGameMode(), getGame().getGameBoard());
        addStudentsCard();

        //Call assignProfessor because the influence can be changed
        getGame().assignProfessor();
    }


    /**
     * This method returns students on card
     *
     * @return type {@link Map} with keys {@link PawnType} and values {@link Integer}: card's students
     */
    public Map<PawnType, Integer> getStudentsCard() {
        return studentsCard;
    }
}
