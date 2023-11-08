package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Player class represents the user and player of the board game
 * additionalMovement variable is used to manage the effect of the characters(in particular {@link it.polimi.ingsw.model.characterCard.Add2ToMotherNatureMovementCharacterCard}
 * controlProfessor variable is used to manage the effect of the characters(in particular {@link it.polimi.ingsw.model.characterCard.TakeControlOfProfessorCharacterCard}
 * additionalInfluence variable is used to manage the effect of the characters(in particular {@link it.polimi.ingsw.model.characterCard.TwoMoreInfluencePointCharacterCard}
 * These variables are used in case the game mode is expert and left at zero otherwise, so you can manage both modes
 *
 * @author Maria Pia Marini
 */

public class Player implements Serializable {
    private final String nickname;
    private final PlayerBoard playerboard;
    private AssistantDeck assistantDeck;
    private AssistantCard cardPlayed;
    private int additionalMovement;
    private boolean controlProfessor;
    private int additionalInfluence;
    private boolean isActionDone;

    //To check character played
    private CharacterCard characterCardPlayed;


    public Player(String nickname) {
        playerboard = new PlayerBoard();
        this.nickname = nickname;
        //These variables are used to implement character cards
        controlProfessor = false;
        isActionDone = false;
        additionalMovement = 0;
        additionalInfluence = 0;
        characterCardPlayed = null;
    }

    /**
     * This method returns the player's nickname
     *
     * @return type {@link String}: the nickname of this Player object.
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * This method selects type of assistant deck
     *
     * @param mageType type {@link MageType}: deck type
     */
    public void selectAssistantDeckType(MageType mageType) {
        assistantDeck = new AssistantDeck(mageType);
    }

    /**
     * This method selects the assistant card to play
     *
     * @param indexValueCard (type int) - AssistantCard id to select
     */
    public void selectCardFromAssistantDeck(int indexValueCard) {
        cardPlayed = assistantDeck.getCard(indexValueCard);
    }

    /**
     * This method return assistant card that was selected
     *
     * @return type {@link AssistantCard}: selected card
     */
    public AssistantCard getCardPlayed() {
        return cardPlayed;
    }

    /**
     * This method checks if assistant deck is empty
     *
     * @return (type bool): true if assistant deck is empty, else false
     */
    public boolean isAssistantDeckEmpty() {
        return assistantDeck.getAvailableDeckList().size() == 0;
    }

    /**
     * This method return PlayerBoard object associated to this Player object
     *
     * @return type {@link PlayerBoard}: PlayerBoard object associated to this Player object
     */
    public PlayerBoard getPlayerboard() {
        return playerboard;
    }

    /**
     * This method return AssistantDeck object associated to this Player object
     *
     * @return type {@link AssistantDeck}: AssistantDeck object associated to this Player object
     */
    public AssistantDeck getAssistantDeck() {
        return assistantDeck;
    }

    /**
     * This method sets variable controlProfessor
     *
     * @param controlProfessor (type bool): value to be assigned to the controlProfessor
     */
    public void setControlProfessor(boolean controlProfessor) {
        this.controlProfessor = controlProfessor;
    }

    /**
     * This method sets variable additionalMovement
     *
     * @param additionalMovement (type int): value to be assigned to the additionalMovement
     */
    public void setAdditionalMovement(int additionalMovement) {
        this.additionalMovement = additionalMovement;
    }

    /**
     * This method sets variable additionalInfluence
     *
     * @param additionalInfluence (type int): value to be assigned to the additionalInfluence
     */
    public void setAdditionalInfluence(int additionalInfluence) {
        this.additionalInfluence = additionalInfluence;
    }

    /**
     * This method returns variable controlProfessor
     *
     * @return (type bool): controlProfessor
     */
    public boolean isControlProfessor() {
        return controlProfessor;
    }

    /**
     * This method returns variable additionalMovement
     *
     * @return (type int): controlProfessor
     */
    public int getAdditionalMovement() {
        return additionalMovement;
    }

    /**
     * This method returns variable additionalInfluence
     *
     * @return (type int): additionalInfluence
     */
    public int getAdditionalInfluence() {
        return additionalInfluence;
    }


    /**
     * Attribute that is true if the player has completed his action phase
     */
    public boolean isActionDone() {
        return isActionDone;
    }

    /**
     * This method returns character played
     *
     * @return type {@link CharacterCard}: character card played
     */
    public CharacterCard getCharacterCardPlayed() {
        return characterCardPlayed;
    }

    /**
     * This method sets character card that it is played
     *
     * @param characterCardPlayed type {@link CharacterCard}: card played
     */
    public void setCharacterCardPlayed(CharacterCard characterCardPlayed) {
        this.characterCardPlayed = characterCardPlayed;
    }

    /**
     * This method is used to manage action phase
     *
     * @param actionDone type bool: true if action phase is played, else false
     */
    public void setActionDone(boolean actionDone) {
        isActionDone = actionDone;
    }
}