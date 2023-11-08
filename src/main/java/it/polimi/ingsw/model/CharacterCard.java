package it.polimi.ingsw.model;


import java.io.Serializable;

/**
 * CharacterCard abstract class is a container used for storing all the card's information from the relative XML file.
 *
 * @author Christian Lisi
 */
public abstract class CharacterCard implements Serializable {

    String ID;
    int cost;
    String effectType;
    String setup;
    String effectDescription;
    Game game;

    /**
     * This method returns card's setup
     *
     * @return type {@link String}: card's setup
     */
    public String getSetup() {
        return this.setup;
    }

    /**
     * This method returns card's ID
     *
     * @return type {@link String}: card's ID
     */
    public String getID() {
        return ID;
    }

    /**
     * This method returns card's type
     *
     * @return type {@link String}: card's type
     */
    public String getEffectType() {
        return effectType;
    }

    /**
     * This method returns card's description
     *
     * @return type {@link String}: card's description
     */
    public String getEffectDescription() {
        return effectDescription;
    }

    /**
     * This method adds money on card
     */
    public void addMoneyCost() {
        cost++;
    }

    /**
     * This method returns card's cost
     *
     * @return type int : card's cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * This method sets card's ID
     *
     * @param ID type {@link String}: card's ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * This method sets card's cost
     *
     * @param cost type {@link String}: card's cost
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * This method sets card's description
     *
     * @param effectDescription type {@link String}: card's description
     */
    public void setEffectDescription(String effectDescription) {
        this.effectDescription = effectDescription;
    }

    /**
     * This method sets card's type
     *
     * @param effectType type {@link String}: card's type
     */
    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    /**
     * This method sets card's setup
     *
     * @param setup type {@link String}: card's setup
     */
    public void setSetup(String setup) {
        this.setup = setup;
    }

    /**
     * This method returns Game object associated at card
     *
     * @return type {@link Game}: Game object
     */
    public Game getGame() {
        return game;
    }

    /**
     * This method sets Game object associated at card
     *
     * @param game type {@link Game}: Game object
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * This method represents card's effect.
     * This abstract method will be implemented for each cards
     */
    public abstract void effect();

    /**
     * This method sets the card played in the object game to null.
     */
    public void resetEffect(String nickname) {
        getGame().getPlayerByNickname(nickname).setCharacterCardPlayed(null);
    }
}
