package Controller.ServerConnection.AgentConnections;

import CommonClasses.AnalyticsData;
import CommonClasses.PlaneData;
import Controller.Controller;
import Model.Model;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class AgentListener implements Runnable {
    private Socket client;
    private ObjectInputStream in;
    private boolean running;
    private PlaneData planeData;
    private List<List<String>> tsList;


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
                    planeData = (PlaneData)fromAgent;
                    Controller.planeDataMap.put(planeData.getId(),planeData);
                    planeData.Print();
                }
                else if(fromAgent instanceof AnalyticsData){//check if plane exists

                }
                else{
                    tsList = (List<List<String>>)fromAgent;
                    Controller.model.DB.savePlaneTimeSeries(planeData.getId() ,planeData.getPlaneName() ,tsList);
                }
            }catch (SocketException se){
                this.stopListening();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopListening() {
        Controller.planeDataMap.remove(this.planeData.getId());
        this.running = false;
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}