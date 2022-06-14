package Controller.ServerConnection.AgentConnections;

import CommonClasses.PlaneData;
import Controller.Controller;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class AgentListener implements Runnable {
    private Socket client;
    private ObjectInputStream in;
    private boolean running;
    private PlaneData plainData;

    public AgentListener(Socket client) {
        this.client = client;
        try {
            InputStream inputStream = client.getInputStream();
            InputStream bufferedIn = new BufferedInputStream(inputStream);
            in = new ObjectInputStream(bufferedIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        System.out.println("inside agentListener, id" + Thread.currentThread().getId());
        this.running = true;
        while (this.running) {
            try {
                System.out.println("running");

                Object fromAgent = in.readObject();// plaindata

                if (fromAgent instanceof PlaneData) {
                    plainData = (PlaneData)fromAgent;
                    Controller.planeDataMap.put(plainData.getId(),plainData);
                    plainData.Print();
                }
                else{
                    //t.s
                }
            }catch (SocketException se){
                this.stopListening();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopListening() {
        Controller.planeDataMap.remove(this.plainData.getId());
        this.running = false;
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

