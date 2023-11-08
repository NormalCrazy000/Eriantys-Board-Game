package it.polimi.ingsw.network.client.gui.items;

import javafx.scene.layout.AnchorPane;

/**
 * This class represents Assistant card item
 */
public class AssistantCardItem extends AnchorPane {
    int value;
    int index;

    /**
     * This method creates the graphical item
     *
     * @param value type int: card's value
     * @param index type int: card's index into Assistant deck
     */
    public AssistantCardItem(int value, int index) {
        this.index = index;
        setPrefHeight(200);
        setPrefWidth(150);
        String pathImage = "'/Assets/carteAssistenti/Assistente" + value + ".png'";
        setStyle("-fx-background-image: url(" + pathImage + ");-fx-background-size: stretch");
        this.value = value;
    }

    /**
     * This method returns card's value
     *
     * @return type int: card's value
     */
    public int getValue() {
        return value;
    }

    /**
     * This method returns card's index
     *
     * @return type int: card's index
     */
    public int getIndex() {
        return index;
    }
}
