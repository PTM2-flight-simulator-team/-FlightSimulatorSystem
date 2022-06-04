package Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import Model.Commands.Command;

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

    public void setAnalyticsHandler(AnalyticsHandler analyticsHandler) {
        this.analyticsHandler = analyticsHandler;
    }
}