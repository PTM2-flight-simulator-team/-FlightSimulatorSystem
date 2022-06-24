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

/**
 * The model is the class that holds all the data and logic of the program
 */
public class MyModel extends Observable {
    // A hashmap that holds the properties of the simulator.
    private HashMap<String,String> properties;
    // A list of the properties names.
    private ArrayList<String> propertiesNamesList;
    // A hashmap that holds all the commands that the model can execute.
    private HashMap<String,Command> myCommands;
    // A class that handles the analytics of the program.
    private AnalyticsHandler analyticsHandler;


    public MyModel() {
        this.myCommands = new HashMap<>();
        this.properties = new HashMap<>();
        this.propertiesNamesList = new ArrayList<>();
        this.analyticsHandler = new AnalyticsHandler();
        this.setCommands();

        //read properties.txt
        // Reading the properties.txt file and adding the properties to the properties' hashmap.
        try {
            BufferedReader in = new BufferedReader(new FileReader(System.getenv("APPDATA") + "\\Agent\\resources\\properties.txt"));
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

    /**
     * > This function takes a PlaneData object and sends it to the analytics handler
     *
     * @param analytic The analytic object that you want to send.
     */
    public void sendAnalytic(PlaneData analytic){
        this.analyticsHandler.InsertAnalytics(analytic);
    }

    public String getFinalAnalytics(){
        return this.analyticsHandler.getFinalAnalytics();
    }

    public HashMap<String, Command> getMyCommands() {
        return myCommands;
    }

    /**
     * It creates a new instance of each command and puts it in a hashmap with the key being the command name
     */
    public void setCommands(){
        myCommands.put("instructions",new instructionCommand(this));
        myCommands.put("printstream",new PrintStreamCommand(this));
        myCommands.put("reset",new ResetCommand(this));
        myCommands.put("shutdown",new ShutDownCommand(this));
        myCommands.put("analytics",new AnalyticSenderCommand(this));
        myCommands.put("FlightDataCommand",new FlightDataCommand(this));
    }

    /**
     * This function sets the changed flag to true and then calls notifyObservers().
     *
     * @param arg The object that is being passed to the observer.
     */
    public void modelNotify(Object arg){
        setChanged();
        notifyObservers(arg);
    }

    /**
     * This function is called by the `PlaneData` class to add a new `PlaneData` object to the `ArrayList` of `PlaneData`
     * objects
     *
     * @param tempPlane This is the PlaneData object that contains all the information about the plane.
     */
    public void setPlainData(PlaneData tempPlane) {
        this.analyticsHandler.AddPlainDataToArrayList(tempPlane);
    }

    /**
     * It returns an ArrayList of ArrayLists of Strings
     *
     * @return An ArrayList of ArrayLists of Strings.
     */
    public ArrayList<ArrayList<String>> getFlight() {
        return this.analyticsHandler.GetFlight();
    }
}