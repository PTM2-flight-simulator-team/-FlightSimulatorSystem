package Controller.ServerConnection.AgentConnections;

import Controller.ServerConnection.AgentConnections.AgentListener;
import Controller.ServerConnection.AgentConnections.AgentWriter;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    Socket socket;
    AgentListener agentListener;
    AgentWriter agentWriter;
    Thread myThread;

    public ClientHandler(Socket socket) {
        this.socket = socket;

        this.agentListener = new AgentListener(socket);
        this.agentWriter = new AgentWriter(socket);
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
}