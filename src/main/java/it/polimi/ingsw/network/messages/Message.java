package it.polimi.ingsw.network.messages;

import java.io.Serializable;
import java.util.UUID;

/**
 * Generic structure of messages
 */
public abstract class Message implements Serializable {



    private final String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage(){ return message;}
}