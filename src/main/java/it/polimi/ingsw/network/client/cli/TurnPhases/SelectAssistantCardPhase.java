package it.polimi.ingsw.network.client.cli.TurnPhases;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.messages.sentByClient.AssistantCardMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.Scanner;


/**
 * This class handles the phase where the player has to choose the assistant card in planning phase
 */
public class SelectAssistantCardPhase extends Phase{


    @Override
    public void makeAction(CLI cli) {

        Scanner scanner = new Scanner(System.in);
        AssistantCard assistantCardChoosed = null;
        int chosed;

        cli.setIsAckArrived(false);

        if(cli.getClientSocket().getView().getActivePlayer().equals(cli.getNickname())) {
            cli.setActionDone(false);

            do{

                System.out.println("STARTING PLANNING PHASE\n"+
                        "In this phase each player in order select an assistant card they want to play");
                System.out.println("Select the assistant card you want to play: ");

                for(int i =0; i < cli.getClientSocket().getView().getAssistantCards().size();i++){
                    System.out.println("["+i+"] card value: "+cli.getClientSocket().getView().getAssistantCards().get(i).getValue()+
                            "/ mother nature movement"+cli.getClientSocket().getView().getAssistantCards().get(i).getMovementMotherNature());
                }
                System.out.print("> ");
                chosed = scanner.nextInt();

//                System.out.println(">>>>>"+ cli.getClientSocket().getView().getAssistantCards().contains(cli.getClientSocket().getView().getAssistantCards().get(chosed-1)));

                if( chosed < 0  || chosed > cli.getClientSocket().getView().getAssistantCards().size()-1 ){
                    System.out.println(Constants.ANSI_RED+"Invalid number"+Constants.ANSI_RESET);
                }


            }while ( chosed< 0 || chosed > cli.getClientSocket().getView().getAssistantCards().size()-1);


            assistantCardChoosed = cli.getClientSocket().getView().getAssistantCards().get(chosed);

            cli.getClientSocket().send(new AssistantCardMessage("assistantCard to play",assistantCardChoosed));

            try {
                synchronized (this){
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(cli.isAckArrived()) {
                cli.getClientSocket().getView().getAssistantCards().remove(chosed);

            }

            cli.setMotherMovement(assistantCardChoosed.getMovementMotherNature());

            System.out.println(Constants.ANSI_YELLOW + "Waiting for other player to finish their turn!" +Constants.ANSI_RESET);

//            cli.setGamePhase(new OtherPlayersTurnPhase());
//            new Thread(cli).start();

//            cli.setGamePhase(new ActionPhase());
//            new Thread(cli).start();

        } else {

            cli.setGamePhase(new OtherPlayersTurnPhase());
            new Thread(cli).start();

        }
    }
}
