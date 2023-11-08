package it.polimi.ingsw.network.client.cli.TurnPhases.CharacterCardsViews;

import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.model.characterCard.Remove3StudentsFromDiningRoomCharacterCard;
import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.client.cli.TurnPhases.*;
import it.polimi.ingsw.network.messages.sentByClient.CharacterCard.Remove3StudentsFromDiningRoomCardMessage;
import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;
import it.polimi.ingsw.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Map;
import java.util.Scanner;

public class Remove3StudentsFromDiningRoomView extends Phase {


    @Override
    public void makeAction(CLI cli) {

        Scanner scanner = new Scanner(System.in);
        int choice;
        PawnType color = null;


        Remove3StudentsFromDiningRoomCharacterCard card = (Remove3StudentsFromDiningRoomCharacterCard) cli.getCardPlayed();


        System.out.println("Choose a type of Student: every player (including yourself) must return 3 Students of that type from their Dining Room to the bag.\n" +
                " * If any player has fewer than 3 Students of that type, return as many Students as they have.");


        System.out.println("\nChose a students color\n" +
                "\t[1] RED"+
                "\t[2] GREEN"+
                "\t[3] BLUE"+
                "\t[4] YELLOW"+
                "\t[5] PINK");

        do{
            System.out.print("> ");
            choice = scanner.nextInt();

            if(choice <0 || choice > 5){
                System.out.println(Constants.ANSI_RED+"Invalid number"+Constants.ANSI_RESET);
            }

        }while(choice <0 || choice > 5);


        switch (choice){
            case 1:
                color = PawnType.RED;
                break;
            case 2:
                color = PawnType.GREEN;
                break;
            case 3:
                color = PawnType.BLUE;
                break;
            case 4:
                color = PawnType.YELLOW;
                break;
            case 5:
                color = PawnType.PINK;
                break;
        }

        System.out.println("DEBUG: "+color.name() );


        boolean notPlay=true;

        //Check if there is player with the student of the chosen color
        for (SerializablePlayerBoard pb : cli.getClientSocket().getView().getEnemyPlayerBoards().values()) {
            if(pb.getDiningRoom().get(color)>0){
                notPlay = false;
                break;
            }
        }


        if(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(color)>0){
            notPlay = false;
        }

        if(notPlay){
            System.out.println(Constants.ANSI_RED+"Not possible to play this card. No one has students of this color"+Constants.ANSI_RESET);
            cli.setGamePhase(new SelectCharacterCardPhase());
            new Thread(cli).start();
        }else {

            card.setParameterToEffect(color);


            cli.getClientSocket().send(new Remove3StudentsFromDiningRoomCardMessage("get students for character card ",card));


            try {
                synchronized (this){
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            cli.setCharacterCardPlayed(true);

            // return to right action phase
            if(!cli.isMoveStudentsDone()){
                cli.setGamePhase(new MoveStudentsPhase());
                new Thread(cli).start();
            }else if(!cli.isMoveMotherNature()){
                cli.setGamePhase(new MoveMotherNaturePhase());
                new Thread(cli).start();
            } else if (!cli.isGetStudFromCloudDone()) {
                cli.setGamePhase(new GetStudFromCloudPhase());
                new Thread(cli).start();
            }

        }


    }
}
