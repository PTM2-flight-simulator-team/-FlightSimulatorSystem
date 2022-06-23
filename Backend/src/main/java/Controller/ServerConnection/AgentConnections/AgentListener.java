package Controller.ServerConnection.AgentConnections;

import CommonClasses.AnalyticsData;
import CommonClasses.PlaneData;
import CommonClasses.PlaneVar;
import Controller.Controller;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class AgentListener extends Observable implements Runnable {
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
        List<List<String>> tsList = null;
        this.running = true;
        while (this.running) {
            try {
                Object fromAgent = in.readObject();// plaindata

                if(fromAgent instanceof String)
                {
                    String str = (String) fromAgent;
                    System.out.println(str);
                    continue;
                }

                if (fromAgent instanceof PlaneData) {
                    planeData = (PlaneData)fromAgent;
//                    Controller.planeDataMap.put(planeData.getId(),planeData);
//                    String id = planeData.getID();
                    setChanged();
                    notifyObservers(planeData);
//                    planeData.Print();
                }
                else if(fromAgent instanceof AnalyticsData){
                    System.out.println("got analytics");
                    AnalyticsData tempAnalytics = (AnalyticsData)fromAgent;
                    int month;
                    Month strMonth;
                    String[] date = tempAnalytics.getEndTime().split("-");//example--> 14-06-2022
                    System.out.println(date);
                    if(date[1].startsWith("0")){
                        month = Integer.parseInt(String.valueOf(date[1].charAt(1)));
                        strMonth = Month.of(month);
                    }
                    else{
                        month = Integer.parseInt(date[1]);
                        strMonth = Month.of(month);
                    }
                    String tempPlaneId = this.planeData.getId();
                    if(Controller.model.DB.doesPlaneExists(tempPlaneId)){//check if plane exists
                        Controller.model.DB.updateMilesById(tempPlaneId , Double.valueOf(tempAnalytics.getMiles()), strMonth);
                        Controller.model.DB.changePlaneState(tempPlaneId , false);
                       // Controller.model.DB.addTs(tempPlaneId,tsList);
                    }
                    else {
                            System.out.println("blabla");
                        HashMap<String,String> data = new HashMap<>();
                        data.put("ID", planeData.getID());
                        data.put("PlaneName", planeData.getPlaneName());
                        for(PlaneVar var: planeData.getAllVars()){
                            data.put(var.getNickName(), var.getValue());
                        }
                            Controller.model.DB.saveNewPlaneAnalytics(this.planeData.getId()
                                    ,this.planeData.getPlaneName(), strMonth ,  Double.valueOf(tempAnalytics.getMiles()) ,tempAnalytics.getState() , data);
//                            this.stopListening();

                    }
                    tempAnalytics.print();
                }
                else {
                    tsList = (List<List<String>>) fromAgent;
                }

                    if(tsList != null){
                        //System.out.println(tsList.toString());
                        Controller.model.DB.savePlaneTimeSeries(planeData.getId() ,planeData.getPlaneName() ,tsList);
                        this.stopListening();
                    }
            }catch (StreamCorruptedException sce){
                this.stopListening();
                break;
            }
            catch (SocketException se){
                this.stopListening();
            } catch (ClassNotFoundException | IOException e) {
//                e.printStackTrace();
            }
        }
    }

    public void stopListening() {
        Controller.planeDataMap.remove(this.planeData.getId());
        this.running = false;
        try {
            in.close();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

}
