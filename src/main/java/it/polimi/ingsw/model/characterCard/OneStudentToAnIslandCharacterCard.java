package it.polimi.ingsw.model.characterCard;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.client.cli.componentPrinter.GameBoardPrinter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * OneStudentToAnIslandCharacterCard class
 * This class implements effect of card number 1:
 * In setup, draw 4 Students and place them on this card.
 * Take 1 Student from this card and place it on an island of yor choice. Then, draw a new Student from the Bag and place it on this card.
 *
 * @author Christian Lisi
 */
public class OneStudentToAnIslandCharacterCard extends CharacterCard implements Serializable {
    Map<PawnType, Integer> studentsCard;
    int indexRegion;
    PawnType studColor;
    HashMap<PawnType, Integer> studToAdd = new HashMap<>();

    /**
     * Constructor method that sets information of card and associates object Game to card
     *
     * @param ID                type {@link String}: card's ID
     * @param cost              type {@link String}: card's cost
     * @param effectType        type {@link String}: card's type
     * @param setup             type {@link String}: card's type
     * @param effectDescription type {@link String}: card's effect
     * @param game type {@link Game}: object Game that represents entire game
     */
    public OneStudentToAnIslandCharacterCard(String ID, String cost, String effectType, String setup, String effectDescription, Game game) {
        setID(ID);
        setCost(Integer.parseInt(cost));
        setEffectType(effectType);
        setSetup(setup);
        setEffectDescription(effectDescription);
        setGame(game);
        //Set up number of students on card
        studentsCard = getGame().getGameBoard().getStudentsFromBag(4);
    }

    /**
     * This method sets parameter that will be used to active effect
     *
     * @param indexRegion type int : Region where student will go
     * @param studColor   type {@link Map} with keys {@link PawnType} and values {@link Integer}: student to add
     */
    public void setParameterToEffect(int indexRegion, PawnType studColor) {
        this.indexRegion = indexRegion;
        this.studColor = studColor;
        studToAdd.put(studColor, 1);
    }

    /**
     * This method removes students on card
     *
     * @param studColor the color of the students the player want to move to an island
     */
    private void removeStudentsCard(PawnType studColor) {

         int x = studentsCard.get(studColor)-1;
        this.studentsCard.put(studColor, x);
    }

    /**
     * This method adds students on card from the bag of students of the game
     */
    private void addStudentsCard() {

        if(getGame().getGameBoard().getStudentsBag().values().stream().mapToInt(Integer::intValue).sum() == 0){
            getGame().setLastTurn(true);
        }else {
            Map<PawnType, Integer> stud = getGame().getGameBoard().getStudentsFromBag(1);
            studentsCard.put(
                    //get the Pawn type
                    stud.entrySet().iterator().next().getKey(),
                    // add a +1 to that pawn type in studentsCard
                    studentsCard.get(stud.entrySet().iterator().next().getKey()) + 1
            );
        }

    }

    /**
     * This method returns students on card
     *
     * @return type  {@link Map} with keys {@link PawnType} and values {@link Integer}: card's students
     */
    public Map<PawnType, Integer> getStudentsCard() {
        return studentsCard;
    }

    /**
     * This method actives card's effect
     */
    @Override
    public void effect() {

        GameBoardPrinter printer = new GameBoardPrinter();

        printer.printRegion(getGame().getGameBoard().getRegion(getRegion() ), getRegion()  + 1);
        removeStudentsCard(getStudColor());
        getGame().getGameBoard().getRegion(getRegion() ).addStudents(getStudToAdd());
        //add new students on card
        addStudentsCard();
        printer.printRegion(getGame().getGameBoard().getRegion(getRegion() ), getRegion()  + 1);
    }

    /**
     * Thsi method returns students on Card
     * @return type Map with key {@link PawnType} and values {@link Integer}: students on Card
     */
    public HashMap<PawnType, Integer> getStudToAdd() {
        return studToAdd;
    }

    /**
     * This method is used to return index island
     * @return type int: island's index
     */
    public int getRegion() {
        return indexRegion;
    }

    /**
     * This method is used to return selected color student
     * @return type {@link PawnType}: selected color student
     */
    public PawnType getStudColor() {
        return studColor;
    }
}
