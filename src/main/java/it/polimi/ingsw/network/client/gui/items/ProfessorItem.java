package it.polimi.ingsw.network.client.gui.items;

import it.polimi.ingsw.model.PawnType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Ellipse;

/**
 * This class represents Professor item
 */
public class ProfessorItem extends Ellipse {
    private PawnType color;
    private GridPane grid;
    private AnchorPane anchorFather;

    /**
     * This method creates the graphical item
     *
     * @param color        type {@link PawnType}: professor's color
     * @param grid         type {@link GridPane}: gridPane that contains student
     * @param anchorFather type {@link AnchorPane}: this represents the anchorPane father of the scene.
     */
    public ProfessorItem(PawnType color, GridPane grid, AnchorPane anchorFather) {
        this.color = color;
        this.grid = grid;
        this.anchorFather = anchorFather;
        setRadiusX(25);
        setRadiusY(25);
        String pathImage = "";
        switch (color) {
            case YELLOW:
                pathImage = "/Assets/yellowstud.png";
                break;
            case RED:
                pathImage = "/Assets/redstud.png";
                break;
            case GREEN:
                pathImage = "/Assets/greenstud.png";
                break;
            case BLUE:
                pathImage = "/Assets/bluestud.png";
                break;
            case PINK:
                pathImage = "/Assets/purplestud.png";
                break;
        }
        Image image = new Image(pathImage);
        this.setFill(new ImagePattern(image));
    }

    /**
     * This method returns professor's color
     *
     * @return type {@link PawnType}: professor's color
     */
    public PawnType getColor() {
        return color;
    }
}
