package Controller.ServerConnection.AgentConnections;

import CommonClasses.PlaneData;
import Controller.Controller;
import Controller.ServerConnection.AgentConnections.AgentListener;
import Controller.ServerConnection.AgentConnections.AgentWriter;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ClientHandler extends Observable implements Runnable, Observer {
    Socket socket;
    AgentListener agentListener;
    AgentWriter agentWriter;
    Thread myThread;
    String ID = null;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.agentListener = new AgentListener(socket);
        agentListener.addObserver(this);
        this.agentWriter = new AgentWriter(socket);
//        this.ID = "2";
//        Controller.clientMap.put(ID,this);

    }

    @Override
    public void run() {
        System.out.println("client handler , id: " + Thread.currentThread().getId());
        myThread = new Thread(this.agentListener);
        myThread.start();
        this.agentWriter.outToAgent("response from server");
    }

    public void writeToAgent(String str){
        this.agentWriter.outToAgent(str);
    }

    public void closeClient(){
        this.agentWriter.shutDown();
        try {
            Thread.sleep(3000);
            //go to sleep for 3 seconds...enough time to get Analytics or timeSeries from Agent
            this.agentListener.stopListening();
            this.socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof AgentListener){
            PlaneData planeData = (PlaneData) arg;
            if(ID == null){
                ID = planeData.getID();
                Controller.clientMap.put(ID,this);
            }
            setChanged();
            notifyObservers(arg);

        }
    }

    public String getID() {
        return ID;
    }
}