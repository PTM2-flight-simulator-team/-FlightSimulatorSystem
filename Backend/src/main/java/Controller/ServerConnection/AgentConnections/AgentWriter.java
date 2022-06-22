package Controller.ServerConnection.AgentConnections;

import java.io.*;
import java.net.Socket;

public class AgentWriter{
    private Socket client;
    private PrintWriter out;
    private String outToAgent;

    public AgentWriter(Socket client) {

        this.client = client;
        try {
            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void outToAgent(String outToAgent) {
//        System.out.println("inside agentWriter outToAgent, id" + Thread.currentThread().getId());
        this.outToAgent = outToAgent;
        out.println(outToAgent);
    }
    public void closeWriter(){
        out.close();
        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutDown(){
        this.outToAgent("shutdown");
    }
}