package Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import CommonClasses.PlaneData;
import Model.Commands.*;

public class MyModel extends Observable {
    private HashMap<String,String> properties;
    private ArrayList<String> propertiesNamesList;

    private HashMap<String,Command> myCommands;
    private AnalyticsHandler analyticsHandler;


    public MyModel() {
        this.myCommands = new HashMap<>();
        this.properties = new HashMap<>();
        this.propertiesNamesList = new ArrayList<>();
        this.analyticsHandler = new AnalyticsHandler();
        this.setCommands();

        //read properties.txt
        try {
            BufferedReader in = new BufferedReader(new FileReader("Agent/src/properties.txt"));
            String line;
            while((line=in.readLine())!=null)
            {
                String sp[] = line.split(",");
                properties.put(sp[0],sp[1]);
                propertiesNamesList.add(sp[0]);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public List<String> GetNamesList(){
        return this.propertiesNamesList;
    }

    public HashMap<String, Command> getCommandList() {
        return this.myCommands;
    }

    public void setCommandList(HashMap<String,Command> commands) {
        this.myCommands = commands;
    }

    public AnalyticsHandler getAnalyticsHandler() {
        return analyticsHandler;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setAnalyticsHandler(AnalyticsHandler analyticsHandler) {
        this.analyticsHandler = analyticsHandler;
    }

    public void setStartTime(String time){
        this.analyticsHandler.setStartTime(time);
    }

    public void setEndTime(String time){
        this.analyticsHandler.setEndTime(time);
    }

//    public void setFrom(String from){
//        this.analyticsHandler.setFrom(from);
//    }

//    public void setTo(String to){
//        this.analyticsHandler.setTo(to);
//    }

    public void sendAnalytic(PlaneData analytic){
        this.analyticsHandler.InsertAnalytics(analytic);
    }

    public String getFinalAnalytics(){
        return this.analyticsHandler.getFinalAnalytics();
    }

    public HashMap<String, Command> getMyCommands() {
        return myCommands;
    }

    public void setCommands(){
        myCommands.put("instructions",new instructionCommand(this));
//        myCommands.put("livestream",new LiveStreamCommand(this));
        myCommands.put("printstream",new PrintStreamCommand(this));
        myCommands.put("reset",new ResetCommand(this));
        myCommands.put("shutdown",new ShutDownCommand(this));
        myCommands.put("analytics",new AnalyticSenderCommand(this));
        myCommands.put("FlightDataCommand",new FlightDataCommand(this));
    }

    public void modelNotify(Object arg){
        setChanged();
        notifyObservers(arg);
    }

    public void setPlainData(PlaneData tempPlane) {
        this.analyticsHandler.AddPlainDataToArrayList(tempPlane);
    }

    public ArrayList<ArrayList<String>> getFlight() {
        return this.analyticsHandler.GetFlight();
    }
}