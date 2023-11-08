package it.polimi.ingsw.network.client.cli.TurnPhases.CharacterCardsViews;

import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.model.characterCard.Replace3StudentsInEntranceCharacterCard;
import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.client.cli.TurnPhases.*;
import it.polimi.ingsw.network.client.cli.componentPrinter.PlayerBoardPrinter;
import it.polimi.ingsw.network.messages.sentByClient.CharacterCard.Replace3StudentsInEntranceCardMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.HashMap;
import java.util.Scanner;

public class Replace3StudentsInEntranceView extends Phase {

    @Override
    public void makeAction(CLI cli) {

        Scanner scanner = new Scanner(System.in);
        int studToMove;
        HashMap<PawnType,Integer> studFromCard = new HashMap<>();
        HashMap<PawnType,Integer> studFromEntrance = new HashMap<>();

        Replace3StudentsInEntranceCharacterCard card = (Replace3StudentsInEntranceCharacterCard) cli.getCardPlayed();



        System.out.println("You may take up to 3 Students from this card and replace them with the same number of Students from your Entrance.");

        PlayerBoardPrinter printer = new PlayerBoardPrinter();
        printer.printEntrance(cli.getClientSocket().getView().getMyPlayerBoard());


        do{

            System.out.println("\n How many student you want to replace? [max 3]");
            System.out.print(">");
            studToMove = scanner.nextInt();

            if(studToMove <0 || studToMove > 3){
                System.out.println(Constants.ANSI_RED+"Invalid number"+Constants.ANSI_RESET);
            }

        }while (studToMove<0 || studToMove > 3);


        System.out.println("DEBUG : sum of stud in entrance "+cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().values().stream().mapToInt(d->d).sum());

        if(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().values().stream().mapToInt(d->d).sum() < studToMove){

            System.out.println(Constants.ANSI_RED+"You don't have enough student in your entrance"+Constants.ANSI_RESET);
            cli.setGamePhase(new SelectCharacterCardPhase());
            new Thread(cli).start();

        } else {

            System.out.println("SELECT THE STUDENTS FROM THE CARD");

            int counter_card = studToMove;
            for (PawnType color : card.getStudentsCard().keySet()){
                int n=0;

                if(card.getStudentsCard().get(color)>0){
                    do{
                        System.out.println("how many "+color.name()+" stud ? [max: "+card.getStudentsCard().get(color)+"]");
                        n = scanner.nextInt();

                        if(n <0 || n > Math.min(card.getStudentsCard().get(color),studToMove) ){
                            System.out.println(Constants.ANSI_RED+"Invalid number "+Constants.ANSI_RESET);
                        }

                    }while (n<0 || n > Math.min(card.getStudentsCard().get(color),studToMove) );

                    studFromCard.put(color,n);
                    counter_card -= n;
                }

                if(counter_card == 0 ){
                    break;
                }
            }



            System.out.println("SELECT THE STUDENTS FROM ENTRANCE");

            int counter_entrance = studToMove;
            for (PawnType color : cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().keySet()){
                int n;

                if(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(color)>0){
                    do{
                        System.out.println("how many "+color.name()+" stud ? [max: "+cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(color)+"]");
                        n = scanner.nextInt();

                        if(n <0 || n > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(color),studToMove) ){
                            System.out.println(Constants.ANSI_RED+"Invalid number "+Constants.ANSI_RESET);
                        }

                    }while (n<0 || n > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(color),studToMove));

                    studFromEntrance.put(color,n);
                    counter_entrance -= n;
                }

                if(counter_entrance == 0 ){
                    break;
                }
            }

            System.out.println("STUD FROM ENTRANCE");
            for(PawnType color : studFromEntrance.keySet()){
                System.out.println("DEBUG: "+ color.name()+" "+studFromEntrance.get(color) );
            }

            System.out.println("STUD FROM CARD");
            for(PawnType color : studFromCard.keySet()){
                System.out.println("DEBUG: "+ color.name()+" "+studFromCard.get(color) );
            }

            card.setParameterToEffect(studFromEntrance,studFromCard,cli.getNickname());

            cli.getClientSocket().send(new Replace3StudentsInEntranceCardMessage(" Replace 3 stud in entrance card", card));


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
