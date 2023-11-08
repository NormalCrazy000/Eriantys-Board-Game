package it.polimi.ingsw.network.client.gui.items;

import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.model.TowerColor;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Ellipse;

/**
 * This class represents Tower item.
 */
public class TowerItem extends Ellipse {

    private TowerColor color;
    private GridPane grid;
    private AnchorPane anchorFather;

    /**
     * This method creates the graphical item
     *
     * @param color        type {@link TowerColor}: tower's color
     * @param grid         type {@link GridPane}: gridPane that contains student
     * @param anchorFather type {@link AnchorPane}: this represents the anchorPane father of the scene.
     */
    public TowerItem(TowerColor color, GridPane grid, AnchorPane anchorFather) {
        this.color = color;
        this.grid = grid;
        this.anchorFather = anchorFather;
        setRadiusX(20);
        setRadiusY(20);
        String pathImage = "";
        switch (color) {
            case WHITE:
                pathImage = "/Assets/whitetower.png";
                break;
            case BLACK:
                pathImage = "/Assets/blacktower.png";
                break;
            case GREY:
                pathImage = "/Assets/greytower.png";
                break;
        }
        Image image = new Image(pathImage);
        this.setFill(new ImagePattern(image));
    }

    /**
     * This method returns tower's color
     *
     * @return type {@link PawnType}: professor's color
     */
    public TowerColor getColor() {
        return color;
    }
}
