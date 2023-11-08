package it.polimi.ingsw.network.client.cli.TurnPhases;


import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.PawnType;
import it.polimi.ingsw.model.characterCard.*;
import it.polimi.ingsw.network.client.cli.CLI;
import it.polimi.ingsw.network.client.cli.TurnPhases.CharacterCardsViews.Exchange2StudetsView;
import it.polimi.ingsw.network.client.cli.TurnPhases.CharacterCardsViews.Remove3StudentsFromDiningRoomView;
import it.polimi.ingsw.network.client.cli.TurnPhases.CharacterCardsViews.Replace3StudentsInEntranceView;
import it.polimi.ingsw.network.client.cli.componentPrinter.GameBoardPrinter;
import it.polimi.ingsw.network.client.cli.componentPrinter.PlayerBoardPrinter;
import it.polimi.ingsw.network.messages.sentByClient.CharacterCard.*;
import it.polimi.ingsw.utils.Constants;

import java.util.HashMap;
import java.util.Scanner;

public class SelectCharacterCardPhase extends Phase{
    @Override
    public void makeAction(CLI cli) {

        Scanner scanner = new Scanner(System.in);
        CharacterCard card = null;
        int chosed=-1;
        GameBoardPrinter gameBoardPrinter = new GameBoardPrinter();
        PlayerBoardPrinter playerBoardPrinter = new PlayerBoardPrinter();



        gameBoardPrinter.printCard(cli.getClientSocket().getView().getGameBoard());

        do {
            System.out.println("=================================== CHARACTER CARD ===========================================\n\n" +
                    "Select a Character  Card [0 to quit this view]");
            System.out.print("> ");
            chosed = scanner.nextInt();

            if (chosed <1|| chosed>3){
                System.out.println(Constants.ANSI_RED+"Invalid number"+Constants.ANSI_RESET);
            }

        }while (chosed < 0 || chosed > 3 );

        if(chosed==0){
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
        } else {


            card = cli.getClientSocket().getView().getGameBoard().getCharacterDeck().getDeckList()[chosed-1];


            if(cli.getClientSocket().getView().getMyPlayerBoard().getCoins() < card.getCost()){
                System.out.println(Constants.ANSI_RED+"You don't have enough coin for this card"+Constants.ANSI_RESET);
                cli.setGamePhase(new SelectCharacterCardPhase());
                new Thread(cli).start();
            }else {



                /**************** Add2ToMotherNatureMovementCharacterCard ************************/
                if (card instanceof Add2ToMotherNatureMovementCharacterCard) {

                    cli.setAdditionalMovement(2);
                    cli.getClientSocket().send(new UseCharacterCardMessage("Character Card", card));

                    cli.setCharacterCardPlayed(true);

                    try {
                        synchronized (this){
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // return to right action phase
                    if (!cli.isMoveStudentsDone()) {
                        cli.setGamePhase(new MoveStudentsPhase());
                        new Thread(cli).start();
                    } else if (!cli.isMoveMotherNature()) {
                        cli.setGamePhase(new MoveMotherNaturePhase());
                        new Thread(cli).start();
                    } else if (!cli.isGetStudFromCloudDone()) {
                        cli.setGamePhase(new GetStudFromCloudPhase());
                        new Thread(cli).start();
                    }


                }
                /**************** ColorNoInfluenceCharacterCardController ************************/
                if (card instanceof ColorNoInfluenceCharacterCard) {

                    chosed = 0;

                    System.out.println("Select a color \n\t" +
                            "[1] RED\n\t" +
                            "[2] BLUE\n\t" +
                            "[3] GREEN\n\t" +
                            "[4] PINK\n\t" +
                            "[5] YELLOW\n\t");
                    System.out.print(">");
                    chosed = scanner.nextInt();

                    switch (chosed) {
                        case 1:
                            System.out.println("selected RED");
                            cli.getClientSocket().send(new ColorNoInfluenceCharacterCardMessage("Color no influence", PawnType.RED, card));
                            break;
                        case 2:
                            System.out.println("selected BLUE");
                            cli.getClientSocket().send(new ColorNoInfluenceCharacterCardMessage("Color no influence", PawnType.BLUE, card));
                            break;
                        case 3:
                            System.out.println("selected GREEN");
                            cli.getClientSocket().send(new ColorNoInfluenceCharacterCardMessage("Color no influence", PawnType.GREEN, card));
                            break;
                        case 4:
                            System.out.println("selected PINK");
                            cli.getClientSocket().send(new ColorNoInfluenceCharacterCardMessage("Color no influence", PawnType.PINK, card));
                            break;
                        case 5:
                            System.out.println("selected YELLOW");
                            cli.getClientSocket().send(new ColorNoInfluenceCharacterCardMessage("Color no influence", PawnType.YELLOW, card));

                            break;
                    }

                    try {
                        synchronized (this){
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    cli.setCharacterCardPlayed(true);

                    // return to right action phase
                    if (!cli.isMoveStudentsDone()) {
                        cli.setGamePhase(new MoveStudentsPhase());
                        new Thread(cli).start();
                    } else if (!cli.isMoveMotherNature()) {
                        cli.setGamePhase(new MoveMotherNaturePhase());
                        new Thread(cli).start();
                    } else if (!cli.isGetStudFromCloudDone()) {
                        cli.setGamePhase(new GetStudFromCloudPhase());
                        new Thread(cli).start();
                    }

                }


                /**************** Exchange2StudentsCharacterCard ************************/
                if (card instanceof Exchange2StudentsCharacterCard) {


                    cli.setCardPlayed(card);
                    cli.setGamePhase(new Exchange2StudetsView());
                    new Thread(cli).start();

                }

                /**************** NoEntryCardCharacterCard ************************/
                if (card instanceof NoEntryCardCharacterCard) {

                    System.out.println(">>>>>"+((NoEntryCardCharacterCard) card).getNoEntry());
                    // handle error if no entry tile on card
                    if (((NoEntryCardCharacterCard) card).getNoEntry() == 0) {
                        System.out.println(Constants.ANSI_RED + "No entry tile on this card" + Constants.ANSI_RESET);
                        cli.setGamePhase(new SelectCharacterCardPhase());
                        new Thread(cli).start();
                    }else {

                        chosed = 0;

                        gameBoardPrinter.printIslands(cli.getClientSocket().getView().getGameBoard());
                        do {
                            System.out.println("Select an island where you want ot put the NO ENTRY tile ");
                            System.out.print("> ");
                            chosed = scanner.nextInt();

                            if (chosed < 0 || chosed > cli.getClientSocket().getView().getGameBoard().getRegions().size()) {
                                System.out.println(Constants.ANSI_RED + "Invalid number" + Constants.ANSI_RESET);
                            }
                        } while (chosed < 0 || chosed > cli.getClientSocket().getView().getGameBoard().getRegions().size());

                        ((NoEntryCardCharacterCard) card).setParameterToEffect(chosed - 1);
                        cli.getClientSocket().send(new NoEntryCardMessage("No entry character card", (NoEntryCardCharacterCard) card));

                        try {
                            synchronized (this){
                                wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        cli.setCharacterCardPlayed(true);

                        // return to right action phase
                        if (!cli.isMoveStudentsDone()) {
                            cli.setGamePhase(new MoveStudentsPhase());
                            new Thread(cli).start();
                        } else if (!cli.isMoveMotherNature()) {
                            cli.setGamePhase(new MoveMotherNaturePhase());
                            new Thread(cli).start();
                        } else if (!cli.isGetStudFromCloudDone()) {
                            cli.setGamePhase(new GetStudFromCloudPhase());
                            new Thread(cli).start();
                        }
                    }

                }

                /**************** NoTowerInInfluenceCharacterCard ************************/
                if (card instanceof NoTowerInInfluenceCharacterCard) {

                    cli.getClientSocket().send(new UseCharacterCardMessage("No tower influence", card));

                    try {
                        synchronized (this){
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    cli.setCharacterCardPlayed(true);

                    // return to right action phase
                    if (!cli.isMoveStudentsDone()) {
                        cli.setGamePhase(new MoveStudentsPhase());
                        new Thread(cli).start();
                    } else if (!cli.isMoveMotherNature()) {
                        cli.setGamePhase(new MoveMotherNaturePhase());
                        new Thread(cli).start();
                    } else if (!cli.isGetStudFromCloudDone()) {
                        cli.setGamePhase(new GetStudFromCloudPhase());
                        new Thread(cli).start();
                    }

                }

                /**************** OneStudentToAnIslandCharacterCard ************************/
                if (card instanceof OneStudentToAnIslandCharacterCard) {

                    //you can't plat the card if there aren't students into the bag
                    if (cli.getClientSocket().getView().getGameBoard().isBagEmpty()) {
                        System.out.println(Constants.ANSI_RED + "You cannot play this card. The bag of student's is empty" + Constants.ANSI_RESET);
                        cli.setGamePhase(new SelectCharacterCardPhase());
                        new Thread(cli).start();
                    }


                    String confirmation = null;
                    chosed = 0;
                    PawnType studColor = null;
                    int regionIndex;

                    System.out.println("Take 1 Student from this card and place it on an island of your choice");


                    // print students on card
                    System.out.println("======= Student on card =======");
                    for(PawnType color : PawnType.values()){
                        if(((OneStudentToAnIslandCharacterCard) card).getStudentsCard().get(color) > 0 ){
                            System.out.println( color.name()+" : "+ ((OneStudentToAnIslandCharacterCard) card).getStudentsCard().get(color));
                        }
                    }
                    System.out.println("=====================");

                    for (PawnType color : ((OneStudentToAnIslandCharacterCard) card).getStudentsCard().keySet()) {

                        if (((OneStudentToAnIslandCharacterCard) card).getStudentsCard().get(color) > 0) {
                            System.out.println("you want to take a " + color.name() + " student? [y/n]");
                            System.out.print("> ");
                            confirmation = scanner.next();

                            if (confirmation.equalsIgnoreCase("y")) {
                                studColor = color;
                                break;
                            }
                        }
                    }


                    assert studColor != null;
                    System.out.println("DEBUG: " + studColor.name());

                    gameBoardPrinter.printIslands(cli.getClientSocket().getView().getGameBoard());

                    do {
                        System.out.println("Select an island");
                        System.out.print(">");
                        chosed = scanner.nextInt();

                        if (chosed < 0 || chosed > cli.getClientSocket().getView().getGameBoard().getRegions().size()) {
                            System.out.println(Constants.ANSI_RED + "Invalid number" + Constants.ANSI_RESET);
                        }

                    } while (chosed < 0 || chosed > cli.getClientSocket().getView().getGameBoard().getRegions().size());

                    ((OneStudentToAnIslandCharacterCard) card).setParameterToEffect(
//                            cli.getClientSocket().getView().getGameBoard().getRegions().get(chosed - 1,
                            (chosed-1),
                            studColor
                    );

                    cli.getClientSocket().send(new OneStudentToAnIslandCardMessage("One stud to island card", (OneStudentToAnIslandCharacterCard) card));


                    try {
                        synchronized (this){
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    cli.setCharacterCardPlayed(true);


                    gameBoardPrinter.printRegion(cli.getClientSocket().getView().getGameBoard().getRegions().get(chosed-1), chosed);


                    // return to right action phase
                    if (!cli.isMoveStudentsDone()) {
                        cli.setGamePhase(new MoveStudentsPhase());
                        new Thread(cli).start();
                    } else if (!cli.isMoveMotherNature()) {
                        cli.setGamePhase(new MoveMotherNaturePhase());
                        new Thread(cli).start();
                    } else if (!cli.isGetStudFromCloudDone()) {
                        cli.setGamePhase(new GetStudFromCloudPhase());
                        new Thread(cli).start();
                    }


                }

                /**************** Remove3StudentsFromDiningRoomCharacterCard ************************/
                if (card instanceof Remove3StudentsFromDiningRoomCharacterCard) {

                    cli.setCardPlayed(card);
                    cli.setGamePhase(new Remove3StudentsFromDiningRoomView());
                    new Thread(cli).start();

                }

                /**************** Replace3StudentsInEntranceCharacterCard ************************/
                if (card instanceof Replace3StudentsInEntranceCharacterCard) {
                    cli.setCardPlayed(card);
                    cli.setGamePhase(new Replace3StudentsInEntranceView());
                    new Thread(cli).start();

                }

                /**************** ResolveIslandCharacterCard ************************/
                if (card instanceof ResolveIslandCharacterCard) {

                    chosed = 0;
                    do {
                        System.out.println("Choose an Island and resolve the Island as if Mother Nature had ended her movement there");
                        System.out.print(">");
                        chosed = scanner.nextInt();

                        if (chosed < 0 || chosed > cli.getClientSocket().getView().getGameBoard().getRegions().size()) {
                            System.out.println(Constants.ANSI_RED + "Invalid number" + Constants.ANSI_RESET);
                        }

                    } while (chosed < 0 || chosed > cli.getClientSocket().getView().getGameBoard().getRegions().size());

                    System.out.println("DEBUG: island index " + (chosed - 1));


                    ((ResolveIslandCharacterCard) card).setParameterToEffect(
                            chosed - 1
                    );

                    cli.getClientSocket().send(new ResolveIslandCardMessage("Resolve island card message", (ResolveIslandCharacterCard) card));


                    try {
                        synchronized (this){
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    cli.setCharacterCardPlayed(true);

                    // return to right action phase
                    if (!cli.isMoveStudentsDone()) {
                        cli.setGamePhase(new MoveStudentsPhase());
                        new Thread(cli).start();
                    } else if (!cli.isMoveMotherNature()) {
                        cli.setGamePhase(new MoveMotherNaturePhase());
                        new Thread(cli).start();
                    } else if (!cli.isGetStudFromCloudDone()) {
                        cli.setGamePhase(new GetStudFromCloudPhase());
                        new Thread(cli).start();
                    }


                }

                /**************** Take1StudentToDiningRoomCharacterCard ************************/
                if (card instanceof Take1StudentToDiningRoomCharacterCard) {


                    String confirmation = null;
                    chosed = 0;
                    PawnType studColor = null;


                    //handling move errors
                    if (cli.getClientSocket().getView().getGameBoard().isBagEmpty()) {
                        System.out.println(Constants.ANSI_RED + "Not possible to play this card. The students bag is empty" + Constants.ANSI_RESET);
                        cli.setGamePhase(new SelectCharacterCardPhase());
                        new Thread(cli).start();
                    }else {

                        System.out.println("Take 1 Student from this card and place it in your Dining Room.");


                        // print students on card
                        System.out.println("======= Student on card =======");
                        for(PawnType color : PawnType.values()){
                            if(((Take1StudentToDiningRoomCharacterCard) card).getStudentsCard().get(color) > 0 ){
                                System.out.println( color.name()+" : "+ ((Take1StudentToDiningRoomCharacterCard) card).getStudentsCard().get(color));
                            }
                        }
                        System.out.println("=====================");

                        //print dining room
                        playerBoardPrinter.printDiningRoom(cli.getClientSocket().getView().getMyPlayerBoard());

                        for (PawnType color : ((Take1StudentToDiningRoomCharacterCard) card).getStudentsCard().keySet()) {

                            if(((Take1StudentToDiningRoomCharacterCard) card).getStudentsCard().get(color)>0){
                                System.out.println("you want to take  a " + color.name() + " student? [y/n]");
                                System.out.print("> ");
                                confirmation = scanner.next();

                                if (confirmation.equalsIgnoreCase("y")) {
                                    studColor = color;
                                    break;
                                }
                            }
                        }

                        assert studColor != null;
                        System.out.println("DEBUG: color " + studColor.name());

                        // handling move errors
                        if (cli.getClientSocket().getView().getMyPlayerBoard().getDiningRoom().get(studColor) + 1 > 10) {
                            System.out.println(Constants.ANSI_RED + "Not possible to get that student. Your dining room is full" + Constants.ANSI_RESET);
                            cli.setGamePhase(new SelectCharacterCardPhase());
                            new Thread(cli).start();
                        }


                        ((Take1StudentToDiningRoomCharacterCard) card).setParameterToEffect(studColor);


                        cli.getClientSocket().send(new Take1StudentToDiningRoomCardMessage("One stud to island card", (Take1StudentToDiningRoomCharacterCard) card));


                        try {
                            synchronized (this){
                                wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        cli.setCharacterCardPlayed(true);

                        // return to right action phase
                        if (!cli.isMoveStudentsDone()) {
                            cli.setGamePhase(new MoveStudentsPhase());
                            new Thread(cli).start();
                        } else if (!cli.isMoveMotherNature()) {
                            cli.setGamePhase(new MoveMotherNaturePhase());
                            new Thread(cli).start();
                        } else if (!cli.isGetStudFromCloudDone()) {
                            cli.setGamePhase(new GetStudFromCloudPhase());
                            new Thread(cli).start();
                        }
                    }




                }

                /**************** TakeControlOfProfessorCharacterCard ************************/
                if (card instanceof TakeControlOfProfessorCharacterCard) {

                    String confirmation = null;
                    chosed = 0;

                    System.out.println("You take control of any number of Professor even if you have the same number of Students as the player who currently controls them.");


                    cli.getClientSocket().send(new TakeControlOfProfessorCardMessage("Take control of prof card", (TakeControlOfProfessorCharacterCard) card));



                    try {
                        synchronized (this){
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    cli.setCharacterCardPlayed(true);


                    // return to right action phase
                    if (!cli.isMoveStudentsDone()) {
                        cli.setGamePhase(new MoveStudentsPhase());
                        new Thread(cli).start();
                    } else if (!cli.isMoveMotherNature()) {
                        cli.setGamePhase(new MoveMotherNaturePhase());
                        new Thread(cli).start();
                    } else if (!cli.isGetStudFromCloudDone()) {
                        cli.setGamePhase(new GetStudFromCloudPhase());
                        new Thread(cli).start();
                    }


                }

                /**************** TwoMoreInfluencePointCharacterCard ************************/
                if (card instanceof TwoMoreInfluencePointCharacterCard) {


                    cli.getClientSocket().send(new UseCharacterCardMessage("Two more influence point message", card));


                    try {
                        synchronized (this){
                            wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    cli.setCharacterCardPlayed(true);

                    // return to right action phase
                    if (!cli.isMoveStudentsDone()) {
                        cli.setGamePhase(new MoveStudentsPhase());
                        new Thread(cli).start();
                    } else if (!cli.isMoveMotherNature()) {
                        cli.setGamePhase(new MoveMotherNaturePhase());
                        new Thread(cli).start();
                    } else if (!cli.isGetStudFromCloudDone()) {
                        cli.setGamePhase(new GetStudFromCloudPhase());
                        new Thread(cli).start();
                    }

                }
            }




        }

    }

}
