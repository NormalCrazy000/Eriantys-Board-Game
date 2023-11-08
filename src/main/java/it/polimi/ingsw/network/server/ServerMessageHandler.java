package it.polimi.ingsw.network.server;


import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characterCard.ColorNoInfluenceCharacterCard;
import it.polimi.ingsw.network.client.cli.componentPrinter.GameBoardPrinter;
import it.polimi.ingsw.network.client.cli.componentPrinter.PlayerBoardPrinter;
import it.polimi.ingsw.network.messages.sentByClient.*;
import it.polimi.ingsw.network.messages.sentByClient.CharacterCard.*;
import it.polimi.ingsw.network.messages.sentByServer.*;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdateActivePlayerMessage;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdateEnemyPlayerBoard;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdateGameBoardMessage;
import it.polimi.ingsw.network.messages.sentByServer.updateMessages.UpdatePlayerBoardMessage;
import it.polimi.ingsw.serializableModel.SerializableGameBoard;
import it.polimi.ingsw.serializableModel.SerializablePlayerBoard;
import it.polimi.ingsw.utils.Constants;

/**
 * The class defines the method to handle all the possible messages sent by the client
 */
public class ServerMessageHandler {

    private final Server server;
    private final ServerClientConnection scc;


    /**
     * class constructor
     * @param server is the instance of the server
     * @param serverClientConnection is the server client connection from which will receive the messages.
     */
    public ServerMessageHandler(Server server, ServerClientConnection serverClientConnection){
        this.server = server;
        this.scc=serverClientConnection;
    }

    /** This method handle the NicknameMessage sent by client
     *
     * @param message the {@link NicknameMessage} sent
     */
    public void handleMessage(NicknameMessage message){
        if (scc.getGamePhase() == GamePhases.CONFIGURATION){

            //check if the nickname has been already taken by another player
            if(!server.checkNickname(message.getMessage())){
                scc.send(new NACKMessage("Error! This nickname has already been taken"));
            }else{

                scc.setNickname(message.getMessage());
                server.addTakenNickname(message.getMessage());
                scc.send(new ACKMessage("OK"));
            }

        }
    }

    /** This method handle the NewGameMessage sent by client
     *
     * @param message the {@link NewGameMessage} sent
     */
    public void handleMessage(NewGameMessage message) {

        if (scc.getGamePhase() == GamePhases.CONFIGURATION) {

            if (scc.getNickname() == null) {

                scc.send(new NACKMessage("Error! You have not chose your nickname yet"));

            } else {

                // get game infos
                int numOfPlayer = message.getNumOfPlayer();
                GameMode gameMode = message.getGameMode();

                //check game infos are right
                System.out.println("###: " + numOfPlayer);
                System.out.println("###: " + gameMode);
                scc.send(new ACKMessage("OK"));
                //add to lobby
                if (numOfPlayer == 2) server.addToLobby2Players(scc, gameMode);
                else if (numOfPlayer == 3) server.addToLobby3Players(scc, gameMode);


//                System.out.println(Constants.ANSI_RED+scc.getGameHandler().getGame().getGameBoard().getCharacterDeck().getDeckList()[0].getID()+"\t "+
//                        scc.getGameHandler().getGame().getGameBoard().getCharacterDeck().getDeckList()[0].getEffectDescription()+Constants.ANSI_RESET);
//                System.out.println(Constants.ANSI_RED+scc.getGameHandler().getGame().getCharacterDeck().getDeckList()[0].getID()+"\t "+
//                        scc.getGameHandler().getGame().getCharacterDeck().getDeckList()[0].getEffectDescription()+Constants.ANSI_RESET);


            }
        }
    }

    /** This method handle the JoinOrHostMessage sent by client
     *
     * @param message the {@link JoinOrHostMessage} sent
     */
    public void handleMessage(JoinOrHostMessage message){
        if (scc.getGamePhase() == GamePhases.CONFIGURATION) {
            scc.send(new ACKMessage("OK"));
        }
    }


    /** This method handle the AmountOfMotherNatureMovementMessage sent by client
     *
     * @param message the {@link AmountOfMotherNatureMovementMessage} sent
     */
    public void handleMessage(AmountOfMotherNatureMovementMessage message) {

        scc.getGameHandler().getGameController().moveMotherNature(message.getMovement());


        SerializableGameBoard newGameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        SerializablePlayerBoard newPlayerBoard = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
        newPlayerBoard.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());
        //Update player boards
        scc.send(new UpdateBoards("updateBoards", newPlayerBoard,newGameBoard));
        // Update game broad for all players
        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("Update gameBoard",newGameBoard));
        scc.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("Update enemy playerboard",newPlayerBoard,scc.getNickname()),scc.getNickname());
        scc.send(new ACKMessage("OK"));

        // if number of region is 3 . End game immediately, notifying the winner
        System.out.println("SIZE: "+scc.getGameHandler().getGame().getGameBoard().getRegions().size());
        if(scc.getGameHandler().getGame().getGameBoard().getRegions().size() <= 3){

            System.out.println("IN");

            scc.getGameHandler().getGame().winner();
            scc.getGameHandler().getGame().setEndGame(true);
            System.out.println("IN2");
            if(scc.getGameHandler().getGame().getWinner() == null){
                scc.getGameHandler().sendBroadcast(new WinMessage("TIE",null));
                System.out.println("SEND TIE");

            }else {
                scc.getGameHandler().sendBroadcast(new WinMessage("WIN",scc.getGameHandler().getGame().getWinner().getNickname()));
                System.out.println("SEND WINNER");
            }
        }



        //check if someone has 0 tower. If so end the game
        for(Player player : scc.getGameHandler().getGame().getPlayers()){
            ServerClientConnection connReference = scc.getGameHandler().getSccRelatedToPlayer().get(player);
            if(connReference.getGameHandler().getGame().getPlayerByNickname(connReference.getNickname()).getPlayerboard().getNumberOfTower() == 0){

                scc.getGameHandler().getGame().winner();
                scc.getGameHandler().getGame().setEndGame(true);

                if(scc.getGameHandler().getGame().getWinner() == null){
                    scc.getGameHandler().sendBroadcast(new WinMessage("TIE",null));
                }else {
                    scc.getGameHandler().sendBroadcast(new WinMessage("WIN",scc.getGameHandler().getGame().getWinner().getNickname()));
                }
                return;
            }
        }

    }


    /** This method handle the AssistantCardMessage sent by client
     *
     * @param message the {@link AssistantCardMessage} sent
     */
    public void handleMessage(AssistantCardMessage message){


        if(scc.getGamePhase()== GamePhases.GAME) {

            //update actionDone variable. The player has started a new action phase so this variable must be false
            scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).setActionDone(false);

            AssistantCard c = message.getAssistantCard();
            System.out.println("> Server MessageHandler.AssistantCard");

            System.out.println("> Card chosen ["+c.getValue()+"]["+c.getMovementMotherNature()+"]");


            if (scc.getGameHandler().getGameController().getCardSelectedByPlayers().contains(c.getValue()) && scc.getGameHandler().getGame().getCurrentPlayer().getAssistantDeck().getAvailableDeckList().size() > 1){
                System.out.println("NACK");
                scc.send(new MoveErrorMessage("card already chosen by another player"));
                System.out.println("message sent");
            }else{

                //register card played by player
                scc.getGameHandler().getGameController().selectAssistantCard(scc.getNickname(),c);

                /* UPDATING PLAYER BOARD */

                //add card to player board
                SerializablePlayerBoard playerBoard = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
                playerBoard.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());
                // update his player board
                scc.send(new UpdatePlayerBoardMessage("new player bard state",scc.getNickname(),playerBoard));
                // update enemy player board for the others players
                scc.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("update enemy playerboard",playerBoard,scc.getNickname()),scc.getNickname());

                /* END UPDATING PLAYER BOARD*/

                System.out.println(scc.getNickname()+" played card "+scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed().getValue());

                System.out.println("sending ack");
                scc.send(new ACKMessage("OK"));

                // if everyone has chosen a card
                if(scc.getGameHandler().getGameController().getCardSelectedByPlayers().size()==scc.getGameHandler().getNumberOfPlayers()){

                    //update order player
                    scc.getGameHandler().getGameController().orderTurnPlayer();

                    //update current player and active player for this scc
                    scc.getGameHandler().getGameController().setCurrentPlayer(
                            scc.getGameHandler().getGame().getPlayers().get(0).getNickname()
                    );
                    scc.getGameHandler().setActivePlayer(
                            scc.getGameHandler().getGameController().getCurrentPlayer()
                    );

                    System.out.println(Constants.ANSI_PURPLE+scc.getGameHandler().getActivePlayer()+Constants.ANSI_RESET);


                    //update activePlayer for every scc except this
                    scc.getGameHandler().sendBroadcastExcept(
                            new UpdateActivePlayerMessage("new active player",scc.getGameHandler().getGameController().getCurrentPlayer()),
                            scc.getGameHandler().getGameController().getCurrentPlayer()
                    );


                    //update game currentIndex player variable
                    scc.getGameHandler().getGame().nextPlayer();

                    // update view for the next player
                    scc.getGameHandler().getSccRelatedToPlayer().get(
                            scc.getGameHandler().getGame().getPlayerByNickname(
                                    scc.getGameHandler().getGameController().getCurrentPlayer()
                            )
                    ).send(new StartTurnMessage("new turn Order Message",scc.getGameHandler().getGameController().getCurrentPlayer()));


                    //all the other players have to wait for their turn
                    scc.getGameHandler().sendBroadcastExcept(new WaitTurnMessage("Wait for your turn"),scc.getGameHandler().getGameController().getCurrentPlayer());

                }else {
                    //set active player to next player in the game array
                    scc.getGameHandler().getGame().nextPlayer();
                    scc.getGameHandler().setActivePlayer(
                            scc.getGameHandler().getGame().getPlayerByIndex(
                                            scc.getGameHandler().getGame().getIndexCurrentPlayer())
                                    .getNickname()
                    );

                    System.out.println("sending ack");
                    scc.send(new ACKMessage("OK"));


                    scc.getGameHandler().getSccRelatedToPlayer().get(
                            scc.getGameHandler().getGame().getPlayerByNickname(
                                    scc.getGameHandler().getActivePlayer()
                            )
                    ).send(new StartPlanningMessage("Start turn", scc.getGameHandler().getActivePlayer()));


                }
            }
        }
    }


    /** This method handle the GetStudentsFromCloudMessage sent by client
     *
     * @param message the {@link GetStudentsFromCloudMessage} sent
     */
    public void handleMessage(GetStudentsFromCloudMessage message){


        //clear for next planning phase
        scc.getGameHandler().getGameController().getCardSelectedByPlayers().clear();

        scc.getGameHandler().getGameController().getStudFromCloud(message.getCloudIndex());

        scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).setActionDone(true);

        SerializablePlayerBoard newPlayerBoard = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
        newPlayerBoard.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());
        SerializableGameBoard newGameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        //update player boards
        scc.send(new UpdateBoards("updateBoards", newPlayerBoard,newGameBoard));
        // update game board for all players
        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("update gameBoard",newGameBoard));
        scc.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("update enemy playerboard",newPlayerBoard,scc.getNickname()),scc.getNickname());
        scc.send(new ACKMessage("OK"));

    }


    /** This method handle the JoinGameMessage sent by client
     *
     * @param message the {@link JoinGameMessage} sent
     */
    public void handleMessage(JoinGameMessage message){

        if(scc.getGamePhase()== GamePhases.CONFIGURATION){
            int numPlayer = message.getNumOfPlayer();
            GameMode gameMode = message.getGameMode();

            //check game infos are right
            System.out.println("###: "+numPlayer);
            System.out.println("###: "+gameMode);

            if (numPlayer == 2) {

                // check if game already created
                if(gameMode == GameMode.EASY){
                    if(server.getLobby2playersEASY().size() == 1){
                        // ok , player join at right time
                        scc.send(new ClientJoinedMesage("OK"));
                        server.addToLobby2Players(scc, gameMode);
                    }else{
                        scc.send(new NotJoinedMessage ("Error! You cant join a game that has not been created."));
                    }
                }
                else if(gameMode == GameMode.EXPERT){
                    if(server.getLobby2playersEXPERT().size() == 1){
                        // ok , player join at right time
                        scc.send(new ClientJoinedMesage("OK"));
                        server.addToLobby2Players(scc, gameMode);
                    }else{
                        scc.send(new NotJoinedMessage("Error! You cant join a game that has not been created."));
                    }
                }

            }
            else if (numPlayer == 3){

                // check if game already created
                if(gameMode == GameMode.EASY){
                    if(server.getLobby3playersEASY().size() == 1 || server.getLobby3playersEASY().size() == 2 ){
                        // ok , player join at right time
                        scc.send(new ClientJoinedMesage("OK"));
                        server.addToLobby3Players(scc, gameMode);
                    }else{
                        scc.send(new NotJoinedMessage ("Error! You cant join a game that has not been created."));
                    }
                }
                else if(gameMode == GameMode.EXPERT){
                    if(server.getLobby3playersEXPERT().size() == 1 || server.getLobby3playersEXPERT().size() == 2){
                        // ok , player join at right time
                        scc.send(new ClientJoinedMesage("OK"));
                        server.addToLobby3Players(scc, gameMode);
                    }else{
                        scc.send(new NotJoinedMessage ("Error! You cant join a game that has not been created."));
                    }
                }

            }
        }
    }




    public void handleMessage(PingACKMessage message){

    }


    /** This method handle the StudentToIslandMessage sent by client
     *
     * @param message the {@link StudentToIslandMessage} sent
     */
    public void handleMessage(StudentToIslandMessage message){

        scc.getGameHandler().getGameController().moveStudentToIsland(message.getIslandIndex(), message.getStudToIsland());

        SerializablePlayerBoard newPlayerBoard = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
        newPlayerBoard.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());
        SerializableGameBoard newGameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        //update player boards
        scc.send(new UpdateBoards("updateBoards", newPlayerBoard,newGameBoard));
        // update game broad for all players
        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("update gameBoard",newGameBoard));
        scc.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("update enemy playerboard",newPlayerBoard,scc.getNickname()),scc.getNickname());
        scc.send(new ACKMessage("OK"));
    }

    /** This method handle the StudentToDiningRoomMessage sent by client
     *
     * @param message the {@link StudentToDiningRoomMessage} sent
     */
    public void handleMessage(StudentToDiningRoomMessage message){


        scc.getGameHandler().getGameController().moveStudentToDiningRoom(message.getStudToDiningRoom());


        SerializablePlayerBoard newPlayerBoard = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
        newPlayerBoard.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());

        PlayerBoardPrinter printer = new PlayerBoardPrinter();
        printer.printEntrance(newPlayerBoard);
        //update each player board + enemy reference
        for(Player player : scc.getGameHandler().getGame().getPlayers()){
            ServerClientConnection connReference = scc.getGameHandler().getSccRelatedToPlayer().get(player);
            SerializablePlayerBoard playerBoard = new SerializablePlayerBoard(connReference.getGameHandler().getGame().getPlayerByNickname(connReference.getNickname()).getPlayerboard());
            playerBoard.setAssistantCard(connReference.getGameHandler().getGame().getPlayerByNickname(connReference.getNickname()).getCardPlayed());
            connReference.send(new UpdatePlayerBoardMessage("update player board state",connReference.getNickname(),playerBoard));
            connReference.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("update enemy player board",playerBoard,connReference.getNickname()),connReference.getNickname());
        }
        scc.send(new ACKMessage("OK"));
    }



    /** This method handle the EndTurnMessage sent by client
     *
     * @param message the {@link EndTurnMessage} sent
     */
    public void handleMessage(EndTurnMessage message){




        //reset card effect
        if(scc.getGameHandler().getGame().getGameMode() == GameMode.EXPERT && scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCharacterCardPlayed() != null){
            scc.getGameHandler().getGameController().resetCharacterCardEffect(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCharacterCardPlayed(),scc.getNickname());
        }

        SerializablePlayerBoard pb = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
        pb.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());
        SerializableGameBoard gb = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        //Update player boards
        scc.send(new UpdateBoards("updateBoards", pb,gb));
        // Update game broad for all players
        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("Update gameBoard",gb));
        scc.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("Update enemy playerboard",pb,scc.getNickname()),scc.getNickname());





        System.out.println(Constants.ANSI_GREEN+"->"+scc.getGameHandler().getGame().getIndexCurrentPlayer()+Constants.ANSI_RESET);
        scc.getGameHandler().getGame().nextPlayer();
        System.out.println(Constants.ANSI_GREEN+"-->"+scc.getGameHandler().getGame().getIndexCurrentPlayer()+Constants.ANSI_RESET);

        scc.getGameHandler().getGameController().setCurrentPlayer(
                scc.getGameHandler().getGame().getPlayers().get(
                        scc.getGameHandler().getGame().getIndexCurrentPlayer()
                ).getNickname()
        );


        scc.getGameHandler().setActivePlayer(
                scc.getGameHandler().getGameController().getCurrentPlayer()
        );


        /*
        --------------------------------------------------------------------------------
        * if every player has done his action phase. The game have to repopulate the cloudTile and reset card effect
        * */
        int i = 0;
        for(Player player: scc.getGameHandler().getGame().getPlayers()){
            if (player.isActionDone()){
                i++;
            }
        }
        if(i == scc.getGameHandler().getNumberOfPlayers()){

            scc.getGameHandler().getGameController().populateCloudTile();

            SerializablePlayerBoard newPlayerBoard = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
            newPlayerBoard.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());
            SerializableGameBoard newGameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
            //Update player boards
            scc.send(new UpdateBoards("updateBoards", newPlayerBoard,newGameBoard));
            // Update game broad for all players
            scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("Update gameBoard",newGameBoard));
            scc.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("Update enemy playerboard",newPlayerBoard,scc.getNickname()),scc.getNickname());
            scc.send(new ACKMessage("OK"));

            //check if it was the last turn
            if(scc.getGameHandler().getGame().isLastTurn()){
                scc.getGameHandler().getGame().winner();
                scc.getGameHandler().getGame().setEndGame(true);

                if(scc.getGameHandler().getGame().getWinner() == null){
                    scc.getGameHandler().sendBroadcast(new WinMessage("TIE",null));
                }else {
                    scc.getGameHandler().sendBroadcast(new WinMessage("WIN",scc.getGameHandler().getGame().getWinner().getNickname()));
                }
            }
//            else {
//
//            }

        }
        /*
         * ----------------------------------------------------------------------
         * */



        //start new turn
        scc.getGameHandler().sendBroadcast(new StartTurnMessage("start next player turn",scc.getGameHandler().getGameController().getCurrentPlayer()));

    }





    /** This method handle the UseCharacterCardMessage sent by client
     *
     * @param message the {@link UseCharacterCardMessage} sent
     */
    public void handleMessage(UseCharacterCardMessage message) {
        message.getCard().setGame(scc.getGameHandler().getGameController().getGame());

        scc.getGameHandler().getGameController().playCharacterCard(message.getCard(),scc.getNickname());

        //message.getCard().effect();
        //update my player board
        SerializablePlayerBoard newPlayerBoard = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
        newPlayerBoard.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());
        scc.send(new UpdatePlayerBoardMessage("Update PlayerBoard",scc.getNickname(),newPlayerBoard));

        //update enemy player board reference to my player board
        scc.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("Update enemy playerboard",newPlayerBoard,scc.getNickname()),scc.getNickname());



        //update game board
        SerializableGameBoard gameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        System.out.println("debug" + gameBoard.getCharacterDeck().getCardByID(message.getCard().getID()).getCost());


        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("update game board",gameBoard));
        scc.send(new ACKMessage("OK"));

    }


    /** This method handle the ColorNoInfluenceCharacterCardMessage sent by client
     *
     * @param message the {@link ColorNoInfluenceCharacterCardMessage} sent
     */
    public void handleMessage(ColorNoInfluenceCharacterCardMessage message) {
        ColorNoInfluenceCharacterCard card = (ColorNoInfluenceCharacterCard) message.getCard();
        card.setParameterToEffect(message.getColor());
        message.getCard().setGame(scc.getGameHandler().getGameController().getGame());
        scc.getGameHandler().getGameController().playCharacterCard(message.getCard(),scc.getNickname());



        //card.effect();

        //update my player board
        SerializablePlayerBoard newPlayerBoard = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
        newPlayerBoard.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());
        scc.send(new UpdatePlayerBoardMessage("Update PlayerBoard",scc.getNickname(),newPlayerBoard));

        //update enemy player board reference to my player board
        scc.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("Update enemy playerboard",newPlayerBoard,scc.getNickname()),scc.getNickname());

        //update game board
        SerializableGameBoard gameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("update game board",gameBoard));
        scc.send(new ACKMessage("OK"));


    }

    /** This method handle the Exchange2StudentsMessage sent by client
     *
     * @param message the {@link Exchange2StudentsMessage} sent
     */
    public void handleMessage(Exchange2StudentsMessage message){
        message.getCard().setGame(scc.getGameHandler().getGameController().getGame());

        scc.getGameHandler().getGameController().playCharacterCard(message.getCard(),scc.getNickname());

        //message.getCard().effect();

        //update each player board + enemy reference
        for(Player player : scc.getGameHandler().getGame().getPlayers()){
            ServerClientConnection connReference = scc.getGameHandler().getSccRelatedToPlayer().get(player);
            SerializablePlayerBoard playerBoard = new SerializablePlayerBoard(connReference.getGameHandler().getGame().getPlayerByNickname(connReference.getNickname()).getPlayerboard());

            playerBoard.setAssistantCard(connReference.getGameHandler().getGame().getPlayerByNickname(connReference.getNickname()).getCardPlayed());
            connReference.send(new UpdatePlayerBoardMessage("update player board state",connReference.getNickname(),playerBoard));
            connReference.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("update enemy player board",playerBoard,connReference.getNickname()),connReference.getNickname());

        }

        //update game board
        SerializableGameBoard gameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("update gameboard",gameBoard));


        scc.send(new ACKMessage("OK"));

    }

    /** This method handle the NoEntryCardMessage sent by client
     *
     * @param message the {@link NoEntryCardMessage} sent
     */
    public void handleMessage(NoEntryCardMessage message){
        message.getCard().setGame(scc.getGameHandler().getGameController().getGame());

        scc.getGameHandler().getGameController().playCharacterCard(message.getCard(),scc.getNickname());

        //message.getCard().effect();

        //update my player board
        SerializablePlayerBoard newPlayerBoard = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
        newPlayerBoard.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());
        scc.send(new UpdatePlayerBoardMessage("Update PlayerBoard",scc.getNickname(),newPlayerBoard));

        //update enemy player board reference to my player board
        scc.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("Update enemy playerboard",newPlayerBoard,scc.getNickname()),scc.getNickname());


        SerializableGameBoard gameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("Update game board", gameBoard));
        scc.send(new ACKMessage("OK"));

    }


    /** This method handle the OneStudentToAnIslandCardMessage sent by client
     *
     * @param message the {@link OneStudentToAnIslandCardMessage} sent
     */
    public void handleMessage (OneStudentToAnIslandCardMessage message){
        message.getCard().setGame(scc.getGameHandler().getGameController().getGame());

        scc.getGameHandler().getGameController().playCharacterCard(message.getCard(),scc.getNickname());


        System.out.println("DEBUG: " + message.getCard().getStudColor() .name() +" "+message.getCard().getStudToAdd().get(message.getCard().getStudColor()));


        GameBoardPrinter printer = new GameBoardPrinter();
//        printer.printRegion(scc.getGameHandler().getGame().getGameBoard().getRegion(message.getCard().getRegion()),message.getCard().getRegion()+1 );

        //message.getCard().effect();


        //update my player board
        SerializablePlayerBoard newPlayerBoard = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
        newPlayerBoard.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());
        scc.send(new UpdatePlayerBoardMessage("Update PlayerBoard",scc.getNickname(),newPlayerBoard));

        //update enemy player board reference to my player board
        scc.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("Update enemy playerboard",newPlayerBoard,scc.getNickname()),scc.getNickname());

        SerializableGameBoard gameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("Update gameBoard",gameBoard));

        System.out.println("===AFTER===");
        printer.printRegion(gameBoard.getRegions().get(message.getCard().getRegion()), message.getCard().getRegion()+1 );
        scc.send(new ACKMessage("OK"));

    }

    /** This method handle the ResolveIslandCardMessage sent by client
     *
     * @param message the {@link ResolveIslandCardMessage} sent
     */
    public void handleMessage (ResolveIslandCardMessage message){
        message.getCard().setGame(scc.getGameHandler().getGameController().getGame());

        scc.getGameHandler().getGameController().playCharacterCard(message.getCard(),scc.getNickname());

        //message.getCard().effect();

        //update my player board
        SerializablePlayerBoard newPlayerBoard = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
        newPlayerBoard.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());
        scc.send(new UpdatePlayerBoardMessage("Update PlayerBoard",scc.getNickname(),newPlayerBoard));

        //update enemy player board reference to my player board
        scc.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("Update enemy playerboard",newPlayerBoard,scc.getNickname()),scc.getNickname());


        SerializableGameBoard gameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("Update game board", gameBoard));
        scc.send(new ACKMessage("OK"));

    }


    /** This method handle the Take1StudentToDiningRoomCardMessage sent by client
     *
     * @param message the {@link Take1StudentToDiningRoomCardMessage} sent
     */
    public void handleMessage (Take1StudentToDiningRoomCardMessage message){

        System.out.println("HELLO WORD");
        message.getCard().setGame(scc.getGameHandler().getGameController().getGame());
        System.out.println("GAME SETTED");

        scc.getGameHandler().getGameController().playCharacterCard(message.getCard(),scc.getNickname());

        System.out.println("EFFECT DONE");
        //apply effect
        //message.getCard().effect();



        //update each player board + enemy reference
        for(Player player : scc.getGameHandler().getGame().getPlayers()){
            ServerClientConnection connReference = scc.getGameHandler().getSccRelatedToPlayer().get(player);
            SerializablePlayerBoard playerBoard = new SerializablePlayerBoard(connReference.getGameHandler().getGame().getPlayerByNickname(connReference.getNickname()).getPlayerboard());
            playerBoard.setAssistantCard(connReference.getGameHandler().getGame().getPlayerByNickname(connReference.getNickname()).getCardPlayed());
            connReference.send(new UpdatePlayerBoardMessage("update player board state",connReference.getNickname(),playerBoard));
            connReference.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("update enemy player board",playerBoard,connReference.getNickname()),connReference.getNickname());

        }

        System.out.println("UPDATE PLAYER");

        //update game board
        SerializableGameBoard gameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("update gameboard",gameBoard));
        System.out.println("UPDATE GAMEBOARD");


        scc.send(new ACKMessage("OK"));
    }


    /** This method handle the TakeControlOfProfessorCardMessage sent by client
     *
     * @param message the {@link TakeControlOfProfessorCardMessage} sent
     */
    public void handleMessage(TakeControlOfProfessorCardMessage message){


        message.getCard().setGame(scc.getGameHandler().getGameController().getGame());

        scc.getGameHandler().getGameController().playCharacterCard(message.getCard(),scc.getNickname());

        //message.getCard().effect();

        //update player board and enemy reference to your player board
        SerializablePlayerBoard playerBoard = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
        playerBoard.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());

        scc.send(new UpdatePlayerBoardMessage("update player board ",scc.getNickname(), playerBoard));
        scc.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("update enemy playerboard",playerBoard,scc.getNickname()),scc.getNickname());

        //update game board
        SerializableGameBoard gameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("update gameboard",gameBoard));
        scc.send(new ACKMessage("OK"));

    }


    /** This method handle the Replace3StudentsInEntranceCardMessage sent by client
     *
     * @param message the {@link Replace3StudentsInEntranceCardMessage} sent
     */
    public void handleMessage (Replace3StudentsInEntranceCardMessage message){
        message.getCard().setGame(scc.getGameHandler().getGameController().getGame());

        scc.getGameHandler().getGameController().playCharacterCard(message.getCard(),scc.getNickname());

        //message.getCard().effect();

        //update player board and enemy reference to your player board
        System.out.println("Check if game is equal:");
        System.out.println(message.getCard().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard().equals(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard()));
        SerializablePlayerBoard playerBoard = new SerializablePlayerBoard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getPlayerboard());
        playerBoard.setAssistantCard(scc.getGameHandler().getGame().getPlayerByNickname(scc.getNickname()).getCardPlayed());
        scc.send(new UpdatePlayerBoardMessage("update player board ",scc.getNickname(), playerBoard));
        scc.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("update enemy playerboard",playerBoard,scc.getNickname()),scc.getNickname());

        //update game board
        SerializableGameBoard gameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("update gameboard",gameBoard));


        PlayerBoardPrinter printer = new PlayerBoardPrinter();
        printer.printEntrance(playerBoard);
        scc.send(new ACKMessage("OK"));


    }


    /** This method handle the Remove3StudentsFromDiningRoomCardMessage sent by client
     *
     * @param message the {@link Remove3StudentsFromDiningRoomCardMessage} sent
     */
    public void handleMessage (Remove3StudentsFromDiningRoomCardMessage message){
//        HashMap <Player, Integer> studToRemove = new HashMap<>();
//
//        for(Player player : scc.getGameHandler().getGame().getPlayers()){
//
//            ServerClientConnection conn = scc.getGameHandler().getSccRelatedToPlayer().get(player);
//            int studOfThisPlayer = scc.getGameHandler().getGame().getPlayerByNickname(player.getNickname()).getPlayerboard().getAllStudentsOrderDiningRoom().get(message.getCard().getStudColor());
//
//            studToRemove.put(player, Math.min(studOfThisPlayer, 3));
//        }
        message.getCard().setParameterToEffect(message.getCard().getStudColor());
        message.getCard().setGame(scc.getGameHandler().getGameController().getGame());

        scc.getGameHandler().getGameController().playCharacterCard(message.getCard(),scc.getNickname());
        //message.getCard().effect();
        //update each player board + enemy reference
        for(Player player : scc.getGameHandler().getGame().getPlayers()){
            ServerClientConnection connReference = scc.getGameHandler().getSccRelatedToPlayer().get(player);
            SerializablePlayerBoard playerBoard = new SerializablePlayerBoard(connReference.getGameHandler().getGame().getPlayerByNickname(connReference.getNickname()).getPlayerboard());
            playerBoard.setAssistantCard(connReference.getGameHandler().getGame().getPlayerByNickname(connReference.getNickname()).getCardPlayed());
            connReference.send(new UpdatePlayerBoardMessage("update player board state",connReference.getNickname(),playerBoard));
            connReference.getGameHandler().sendBroadcastExcept(new UpdateEnemyPlayerBoard("update enemy player board",playerBoard,connReference.getNickname()),connReference.getNickname());

        }

        //update game board
        SerializableGameBoard gameBoard = new SerializableGameBoard(scc.getGameHandler().getGame().getGameBoard());
        scc.getGameHandler().sendBroadcast(new UpdateGameBoardMessage("update gameboard",gameBoard));

        scc.send(new ACKMessage("OK"));

    }











}