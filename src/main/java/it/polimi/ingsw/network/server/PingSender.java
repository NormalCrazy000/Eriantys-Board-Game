package it.polimi.ingsw.network.server;


import it.polimi.ingsw.network.messages.PingMessage;

public class PingSender implements Runnable{

    private ServerClientConnection scc;
    private boolean isActive;

    public PingSender(ServerClientConnection scc){
        this.scc = scc;
        isActive = true;
    }


    /**
     * method that sends a ping every 20 second and wait for a response. If the answer does not come, it marks the
     * server client connection as disconnected.
     */
    @Override
    public void run() {

        while(isActive){

            //send ping message
            scc.send(new PingMessage("Ping"));
            isActive = false;

            try {
                Thread.sleep(1000*20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(scc.getNickname() + ": ping disconnection");


    }


    public void pingReceived(){

        System.out.println("Ping");
        isActive = true;
    }

    public void setActive(boolean value){
        isActive=value;
    }
}
