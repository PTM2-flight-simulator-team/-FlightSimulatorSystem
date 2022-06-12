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
        myThread = new Thread(this.agentListener);
        myThread.start();
        this.agentWriter.outToAgent("response from server");
    }

    public void writeToAgent(String str){
        this.agentWriter.outToAgent(str);
    }

    public void closeClient(){
        try {
            this.agentListener.stopListening();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}