package it.polimi.ingsw.network.client.gui.items;

import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.network.client.gui.GUI;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;


/**
 * This class represents Student item.
 * This item can move from its position. It is used where item must be moved
 */
public class StudentMoveItem extends StudentItem {
    private double startX;
    private double startY;
    private GUI gui;

    /**
     * This method creates the graphical item
     *
     * @param color        type {@link PawnType}: student's color
     * @param grid         type {@link GridPane}: gridPane that contains student
     * @param column       type int: col into gridPane
     * @param row          type int: row into gridPane
     * @param anchorFather type {@link AnchorPane}: this represents the anchorPane father of the scene.
     */
    public StudentMoveItem(PawnType color, GridPane grid, int column, int row, AnchorPane anchorFather, GUI gui) {
        super(color, grid, column, row, anchorFather);
        this.gui = gui;
        //This call is used to add movement event to student
        setupMove();
    }

    /**
     * This method adds movement to the item
     */
    public void setupMove() {
        //Add mousePressd event
        setOnMousePressed(event -> {
            //Check if click is allowed(Click is allowed if updates are finished)
            if (gui.isCanPlay() && !getFather().getChildren().contains(event.getSource())) {
                //To allow move item
                setMouseTransparent(true);
                //Set the coordinates for movement
                startX = event.getSceneX() - getTranslateX();
                startY = event.getSceneY() - getTranslateY();
                getFather().getChildren().add((StudentMoveItem) event.getSource());
                getGrid().getChildren().remove(event.getSource());
                getFather().getChildren().get(getFather().getChildren().size() - 1).setLayoutX(startX);
                getFather().getChildren().get(getFather().getChildren().size() - 1).setLayoutY(startY);
            }
        });
        //this event is triggered if the student is released in a prohibited place
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
        getFather().getChildren().remove(this);
        getGrid().add(this, getColumn(), getRow());
    }
}
