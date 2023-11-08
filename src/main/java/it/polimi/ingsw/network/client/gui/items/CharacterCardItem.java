package it.polimi.ingsw.network.client.gui.items;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.characterCard.*;
import it.polimi.ingsw.network.client.gui.GUI;
import it.polimi.ingsw.network.client.gui.Phase;
import it.polimi.ingsw.network.messages.sentByClient.CharacterCard.TakeControlOfProfessorCardMessage;
import it.polimi.ingsw.network.messages.sentByClient.CharacterCard.UseCharacterCardMessage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * This class represents CharacterCard item
 */
public class CharacterCardItem extends AnchorPane {
    CharacterCard card;

    /**
     * This method creates the graphical item
     *
     * @param card type {@link String}: Character's card
     */
    public CharacterCardItem(CharacterCard card, GUI gui) {
        this.card = card;
        setPrefHeight(200);
        setPrefWidth(150);
        String pathImage = "'/Assets/cartePersonaggio/Card" + card.getID() + ".jpg'";
        setStyle("-fx-background-image: url(" + pathImage + ");-fx-background-size: stretch");
        Tooltip t = new Tooltip(card.getEffectDescription());
        Tooltip.install(this, t);

        setOnMouseClicked(mouseEvent -> {
            //Check if card can be played
            if (gui.isCanPlay() && gui.isCanPlayCharacter() && gui.getPhase() != Phase.ASSISTANT && gui.getPhase() != Phase.OTHER) {
                gui.setCanPlay(false);
                gui.setCanPlayCharacter(false);
                //Check if player has money to play card
                if (gui.getClientSocket().getView().getMyPlayerBoard().getCoins() < card.getCost()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "You don't have enough coin for this card", ButtonType.OK);
                    alert.showAndWait();
                    gui.setCanPlay(true);
                    gui.setCanPlayCharacter(true);
                    return;
                }
                gui.setCardPlayed(card);
                String pathCharacterController = null;
                //Check type card
                if (card instanceof Add2ToMotherNatureMovementCharacterCard) {
                    gui.setAdditionalMovement(2);
                    gui.getClientSocket().send(new UseCharacterCardMessage("Character Card", card));
                } else if (card instanceof ColorNoInfluenceCharacterCard) {
                    pathCharacterController = "/fxml/character/ColorNoInfluenceCharacterCard.fxml";

                } else if (card instanceof Exchange2StudentsCharacterCard) {
                    pathCharacterController = "/fxml/character/Exchange2StudentsCharacterCard.fxml";

                } else if (card instanceof NoEntryCardCharacterCard) {

                    pathCharacterController = "/fxml/character/NoEntryCardCharacterCard.fxml";
                } else if (card instanceof NoTowerInInfluenceCharacterCard) {
                    gui.getClientSocket().send(new UseCharacterCardMessage("No tower influence", card));

                } else if (card instanceof OneStudentToAnIslandCharacterCard) {
                    pathCharacterController = "/fxml/character/OneStudentToAnIslandCharacterCard.fxml";
                } else if (card instanceof Remove3StudentsFromDiningRoomCharacterCard) {
                    pathCharacterController = "/fxml/character/Remove3StudentsFromDiningRoomCharacterCard.fxml";
                } else if (card instanceof Replace3StudentsInEntranceCharacterCard) {
                    pathCharacterController = "/fxml/character/Replace3StudentsInEntranceCharacterCard.fxml";
                } else if (card instanceof ResolveIslandCharacterCard) {
                    pathCharacterController = "/fxml/character/ResolveIslandCharacterCard.fxml";
                } else if (card instanceof Take1StudentToDiningRoomCharacterCard) {
                    pathCharacterController = "/fxml/character/Take1StudentToDiningRoomCharacterCard.fxml";

                } else if (card instanceof TakeControlOfProfessorCharacterCard) {
                    gui.getClientSocket().send(new TakeControlOfProfessorCardMessage("Take control of prof card", (TakeControlOfProfessorCharacterCard) card));
                } else if (card instanceof TwoMoreInfluencePointCharacterCard) {
                    gui.getClientSocket().send(new UseCharacterCardMessage("Two more influence point message", card));
                }
                if (pathCharacterController != null) {
                    Stage stage = new Stage();
                    stage.setScene(gui.createScene(pathCharacterController));
                    //Add the function that will be called when the scene is closed
                    stage.setOnCloseRequest(windowEvent -> {
                        gui.setCanPlayCharacter(true);
                        gui.setCanPlay(true);
                    });

                    stage.sizeToScene();
                    stage.show();
                } else {
                    synchronized (gui.getController()) {
                        try {
                            gui.getController().wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    gui.setCanPlay(true);
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You can't play card", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }

    /**
     * This method returns card's ID
     *
     * @return type int: card's value
     */
    public String getID() {
        return card.getID();
    }
}
