package it.polimi.ingsw.network.client.gui.items;

import it.polimi.ingsw.network.client.gui.GUI;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Ellipse;

/**
 * This class represents Mother item
 */
public class MotherItem extends Ellipse {
    private double startX;
    private double startY;
    private GridPane grid;
    private int row;
    private int column;
    private AnchorPane anchorFather;
    private GUI gui;


    /**
     * This method creates the graphical item
     *
     * @param grid         type {@link GridPane}: gridPane that contains student
     * @param column       type int: col into gridPane
     * @param row          type int: row into gridPane
     * @param anchorFather type {@link AnchorPane}: this represents the anchorPane father of the scene.
     * @param gui          type {@link GUI}: GUI object
     */
    public MotherItem(GridPane grid, int column, int row, AnchorPane anchorFather, GUI gui) {
        this.gui = gui;
        this.grid = grid;
        this.row = row;
        this.column = column;
        this.anchorFather = anchorFather;
        setRadiusX(15);
        setRadiusY(15);
        String pathImage = "/Assets/mothernature.png";
        Image image = new Image(pathImage);
        this.setFill(new ImagePattern(image));
        //Add mousePressd event
        setOnMousePressed(event -> {
            //Check if click is allowed(Click is allowed if updates are finished)
            if (gui.isCanPlay() && !anchorFather.getChildren().contains(event.getSource())) {
                //To allow move item
                setMouseTransparent(true);
                //Set the coordinates for movement
                startX = event.getSceneX() - getTranslateX();
                startY = event.getSceneY() - getTranslateY();
                anchorFather.getChildren().add((MotherItem) event.getSource());
                getGrid().getChildren().remove(event.getSource());
                anchorFather.getChildren().get(anchorFather.getChildren().size() - 1).setLayoutX(startX);
                anchorFather.getChildren().get(anchorFather.getChildren().size() - 1).setLayoutY(startY);
            }
        });
        //this event is triggered if the mother is released in a prohibited place
        setOnMouseReleased(event -> {
            if (gui.isCanPlay()) {
                setMouseTransparent(false);
                resetPosition();
            }

        });
        //This event is used to move item
        setOnMouseDragged(event -> {
            if (gui.isCanPlay()) {
                setTranslateX(event.getSceneX() - startX);
                setTranslateY(event.getSceneY() - startY);
            }
        });
        //This event is used to set that the movement is draggable
        setOnDragDetected(event -> {
            if (gui.isCanPlay()) {
                startFullDrag();
            }
        });
    }

    /**
     * This method is used to reset position in case of error
     */
    public void resetPosition() {
        setTranslateX(0);
        setTranslateY(0);
        anchorFather.getChildren().remove(this);
        grid.add(this, column, row);
    }

    /*public void changeGridPane(GridPane p, int column, int row) {
        setTranslateX(0);
        setTranslateY(0);
        anchorFather.getChildren().remove(this);
        p.add(this, column, row);
        this.grid = p;
        this.row = row;
        this.column = column;
    }
     */

    /**
     * This method is used to return grid Pane
     *
     * @return type {@link GridPane}: gridPane obejct
     */
    public GridPane getGrid() {
        return grid;
    }

}
