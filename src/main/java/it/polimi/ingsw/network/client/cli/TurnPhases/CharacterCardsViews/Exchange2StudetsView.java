package it.polimi.ingsw.network.client.cli.TurnPhases.CharacterCardsViews;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.model.characterCard.Exchange2StudentsCharacterCard;
import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.client.cli.TurnPhases.*;
import it.polimi.ingsw.network.client.cli.componentPrinter.PlayerBoardPrinter;
import it.polimi.ingsw.network.messages.sentByClient.CharacterCard.Exchange2StudentsMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.HashMap;
import java.util.Scanner;

public class Exchange2StudetsView extends Phase {
    @Override
    public void makeAction(CLI cli) {

        Scanner scanner = new Scanner(System.in);
        PlayerBoardPrinter playerBoardPrinter = new PlayerBoardPrinter();
        HashMap<PawnType,Integer> studFromDiningRoom = new HashMap<>();
        HashMap<PawnType,Integer> studFromEntrance = new HashMap<>();
        int chosed;
        int numStudToMove;
        int red;
        int blue;
        int pink;
        int yellow;
        int green;

        playerBoardPrinter.printEntrance(cli.getClientSocket().getView().getMyPlayerBoard());
        playerBoardPrinter.printDiningRoom(cli.getClientSocket().getView().getMyPlayerBoard());

        for (PawnType color : PawnType.values()){
            studFromDiningRoom.put(color,0);
            studFromEntrance.put(color,0);
        }


        do {
            System.out.println("How many students you want to exchange between your entrance and your Dining room? [max 2]");
            System.out.print(">");
            chosed = scanner.nextInt();

            if(chosed<1 ||  chosed> 2 ){
                System.out.println(Constants.ANSI_RED+"Invalid number"+Constants.ANSI_RESET);
            }
        }while (chosed< 1 || chosed>2);

//        System.out.println("DEBUG: Student to move "+chosed);
//        System.out.println("DEBUG: num of stud in dining room:" + cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().values().stream().mapToInt(d->d).sum());

        if(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().values().stream().mapToInt(d->d).sum() < chosed){

            System.out.println(Constants.ANSI_RED+"You don't have enough student in your dining room"+Constants.ANSI_RESET);
            cli.setGamePhase(new SelectCharacterCardPhase());
            new Thread(cli).start();

        }else{


            /*************************************************************
             *                  DINING ROOM
             **************************************************************/
            numStudToMove = chosed;


            System.out.println("====== FROM DINING ROOM =========");

            do{
                //ask for red stud
                if(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.RED)>0){
                    do {
                        if(numStudToMove == 0) break;
                        System.out.println("Red students to move?");
                        System.out.print("> ");
                        red = scanner.nextInt();


                        if(red <0 || red > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.RED), numStudToMove)){
                            System.out.println(Constants.ANSI_RED+"Invalid number"+Constants.ANSI_RESET);
                        }else {
                            studFromDiningRoom.put(PawnType.RED,red);
                            numStudToMove-=red;
                        }

                    }while(red < 0 || red > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.RED), numStudToMove));

                }
                //ask for blue stud
                if(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.BLUE)>0){
                    do {
                        if(numStudToMove==0) break;
                        System.out.println("Blue students to move?");
                        System.out.print("> ");
                        blue = scanner.nextInt();
//                            studToDiningRoom.put(PawnType.BLUE,blue);

                        if(blue < 0 || blue > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.BLUE), numStudToMove)){
                            System.out.println(Constants.ANSI_RED+"Invalid number"+Constants.ANSI_RESET);
                        }else {
                            studFromDiningRoom.put(PawnType.BLUE,blue);
                            numStudToMove-=blue;
                        }

                    }while(blue < 0 || blue > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.BLUE), numStudToMove));
                }
                //ask for pink stud
                if(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.PINK)>0){
                    do {
                        if(numStudToMove==0) break;
                        System.out.println("Pink students to move?");
                        System.out.print("> ");
                        pink = scanner.nextInt();

                        if(pink < 0 || pink > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.PINK),numStudToMove)){
                            System.out.println(Constants.ANSI_RED+"Invalid number"+Constants.ANSI_RESET);
                        } else {
                            studFromDiningRoom.put(PawnType.PINK,pink);
                            numStudToMove-=pink;
                        }

                    }while(pink < 0 || pink > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.PINK),numStudToMove));
                }
                //ask for yellow stud
                if(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.YELLOW)>0){
                    do {
                        if(numStudToMove==0) break;
                        System.out.println("Yellow students to move?");
                        System.out.print("> ");
                        yellow = scanner.nextInt();

                        if(yellow < 0 || yellow >Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.YELLOW) , numStudToMove)) {
                            System.out.println(Constants.ANSI_RED + "Invalid number" + Constants.ANSI_RESET);
                        }else{
                            studFromDiningRoom.put(PawnType.YELLOW,yellow);
                            numStudToMove-=yellow;
                        }

                    }while(yellow < 0 || yellow >Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.YELLOW) , numStudToMove) );
                }
                //ask for green stud
                if(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.GREEN)>0){
                    do {
                        if(numStudToMove==0) break;
                        System.out.println("Green students to move?");
                        System.out.print("> ");
                        green = scanner.nextInt();


                        if(green < 0 || green >Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.GREEN), numStudToMove)) {
                            System.out.println(Constants.ANSI_RED + "Invalid number" + Constants.ANSI_RESET);
                        }else{
                            studFromDiningRoom.put(PawnType.GREEN,green);
                            numStudToMove-=green;
                        }

                    }while(green < 0 || green >Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(PawnType.GREEN), numStudToMove));
                }

            }while (numStudToMove!=0);


            /*************************************************************
             *                  ENTRANCE
             **************************************************************/

            numStudToMove = chosed;

            System.out.println("====== FROM ENTRANCE =========");

            do{
                //ask for red stud
                if(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.RED)>0){
                    do {
                        if(numStudToMove == 0) break;
                        System.out.println("Red students to move?");
                        System.out.print("> ");
                        red = scanner.nextInt();


                        if(red <0 || red > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.RED), numStudToMove)){
                            System.out.println(Constants.ANSI_RED+"Invalid number"+Constants.ANSI_RESET);
                        }else {
                            studFromEntrance.put(PawnType.RED,red);
                            numStudToMove-=red;
                        }

                    }while(red < 0 || red > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.RED), numStudToMove));

                }
                //ask for blue stud
                if(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.BLUE)>0){
                    do {
                        if(numStudToMove==0) break;
                        System.out.println("Blue students to move?");
                        System.out.print("> ");
                        blue = scanner.nextInt();
//                            studToDiningRoom.put(PawnType.BLUE,blue);

                        if(blue < 0 || blue > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.BLUE), numStudToMove)){
                            System.out.println(Constants.ANSI_RED+"Invalid number"+Constants.ANSI_RESET);
                        }else {
                            studFromEntrance.put(PawnType.BLUE,blue);
                            numStudToMove-=blue;
                        }

                    }while(blue < 0 || blue > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.BLUE), numStudToMove));
                }
                //ask for pink stud
                if(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.PINK)>0){
                    do {
                        if(numStudToMove==0) break;
                        System.out.println("Pink students to move?");
                        System.out.print("> ");
                        pink = scanner.nextInt();

                        if(pink < 0 || pink > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.PINK),numStudToMove)){
                            System.out.println(Constants.ANSI_RED+"Invalid number"+Constants.ANSI_RESET);
                        } else {
                            studFromEntrance.put(PawnType.PINK,pink);
                            numStudToMove-=pink;
                        }

                    }while(pink < 0 || pink > Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.PINK),numStudToMove));
                }
                //ask for yellow stud
                if(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.YELLOW)>0){
                    do {
                        if(numStudToMove==0) break;
                        System.out.println("Yellow students to move?");
                        System.out.print("> ");
                        yellow = scanner.nextInt();

                        if(yellow < 0 || yellow >Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.YELLOW) , numStudToMove)) {
                            System.out.println(Constants.ANSI_RED + "Invalid number" + Constants.ANSI_RESET);
                        }else{
                            studFromEntrance.put(PawnType.YELLOW,yellow);
                            numStudToMove-=yellow;
                        }

                    }while(yellow < 0 || yellow >Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.YELLOW) , numStudToMove) );
                }
                //ask for green stud
                if(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.GREEN)>0){
                    do {
                        if(numStudToMove==0) break;
                        System.out.println("Green students to move?");
                        System.out.print("> ");
                        green = scanner.nextInt();


                        if(green < 0 || green >Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.GREEN), numStudToMove)) {
                            System.out.println(Constants.ANSI_RED + "Invalid number" + Constants.ANSI_RESET);
                        }else{
                            studFromEntrance.put(PawnType.GREEN,green);
                            numStudToMove-=green;
                        }

                    }while(green < 0 || green >Math.min(cli.getClientSocket().getView().getMyPlayerBoard().getEntrance().get(PawnType.GREEN), numStudToMove));
                }

            }while (numStudToMove!=0);

            System.out.println("STUD FROM DINING");
            for (PawnType color : studFromDiningRoom.keySet()){
                System.out.println(color.name()+" : "+ studFromDiningRoom.get(color));
            }

            System.out.println("STUD FROM ENTRANCE");
            for (PawnType color : studFromEntrance.keySet()){
                System.out.println(color.name()+" : "+ studFromEntrance.get(color));
            }


            boolean ok = true;

            HashMap<PawnType,Integer> futureDiningRoom = new HashMap<>();

            for(PawnType color : PawnType.values()){
                futureDiningRoom.put(color,
                        cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(color) + studFromEntrance.get(color) - studFromDiningRoom.get(color));
            }

            for(Integer value : futureDiningRoom.values()){
                if(value>10){
                    ok = false;
                }
            }


            if(ok){
                ((Exchange2StudentsCharacterCard) cli.getCardPlayed()).setParameterToEffect(studFromDiningRoom,studFromEntrance );
                cli.getClientSocket().send(new Exchange2StudentsMessage("Exchange 2 Stud card", (Exchange2StudentsCharacterCard) cli.getCardPlayed()));


                try {
                    synchronized (this){
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



                cli.setCharacterCardPlayed(true);


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


            }else {
                System.out.println(Constants.ANSI_RED+"Not possible to play this card. Overflow Dining room"+Constants.ANSI_RESET);
                cli.setGamePhase(new SelectCharacterCardPhase());
                new Thread(cli).start();
            }

        }
    }
}
