package it.polimi.ingsw.network.messages;


/**
 * Message sent by client for notify its connection
 *
 * @author Christian
 */
public class PingMessage extends Message{

    public PingMessage(String message){
        super(message);
    }


}
