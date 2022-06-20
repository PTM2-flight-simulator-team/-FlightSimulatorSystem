package Controller;

import CommonClasses.PlaneData;
import Model.Commands.instructionCommand;
import Model.MyLogger;
import Model.MyModel;
import Network.CommandAction;
import Network.NetworkCommand;
import Network.NetworkManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyController implements Observer {
    private NetworkManager networkManager;
    private MyModel model;

    // This is the constructor of the controller.
    public MyController(MyModel model) {
        this.networkManager = new NetworkManager(model.GetNamesList());
        this.model = model;
        // Adding the controller as an observer to the model and the network manager.
        this.model.addObserver(this);
        this.networkManager.addObserver(this);
        // This is a function that gets the current time and date and formats it to a string.
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String StartTime = currentTime.format(timeFormatter);
        this.model.getAnalyticsHandler().setStartTime(StartTime);
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    public MyModel getModel() {
        return model;
    }

    public void setModel(MyModel model) {
        this.model = model;
    }

    public ArrayList<ArrayList<String>> getFlightData(){
        return this.model.getFlight();
    }

    /**
     * The function receives a network command, checks if it's a set command, if it is, it sets the command, if it's a do
     * command, it executes the command, if it's a get command, it executes the command
     *
     * @param networkCommand the command that was sent from the client.
     */
    public void CLI(NetworkCommand networkCommand){
        // This is a function that checks if the command is a set command, if it is, it sets the command, if it's a do
        //  * command, it executes the command, if it's a get command, it executes the command
        if(networkCommand.action == CommandAction.Set)
        {
            // check if the property is legit -------------------
            instructionCommand c = (instructionCommand) this.getModel().getMyCommands().get("instructions");
            c.setCommand(networkCommand.path + " " + networkCommand.value);
            c.execute();
            return;
        }
        // This is a function that checks if the command is a set command, if it is, it sets the command, if it's a do
        //         * command, it executes the command, if it's a get command, it executes the command
        if(networkCommand.action == CommandAction.Do)
        {
            if(networkCommand.path.contains("printstream"))
            {
                MyLogger.LogMessage("printstream");
                this.getModel().getMyCommands().get("printstream").execute();
                return;
            }
            if(networkCommand.path.contains("reset")){
                MyLogger.LogMessage("reset");
                this.getModel().getMyCommands().get("reset").execute();
                return;
            }
            if(networkCommand.path.contains("shutdown")){
                MyLogger.LogMessage("shutdown");
                this.getModel().getMyCommands().get("analytics").execute();
                this.getModel().getMyCommands().get("shutdown").execute();
                return;
            }
        }
        // This is a function that checks if the command is a set command, if it is, it sets the command, if it's a do
        //          * command, it executes the command, if it's a get command, it executes the command
        if(networkCommand.action == CommandAction.Get){
            if(networkCommand.path.contains("FlightData")){
                MyLogger.LogMessage("FlightDataCommand");
                this.getModel().getMyCommands().get("FlightDataCommand").execute();
                return;
            }
            if(networkCommand.path.contains("analytic"))
            {
                MyLogger.LogMessage("analytics");
                this.getModel().getMyCommands().get("analytics").execute();
                return;
            }

        }
    }

    public void sendFlightDataToBackend(ArrayList<ArrayList<String>> list){
        this.networkManager.sendFlightDataToBackend(list);
    }

    private void sendAnalyticsToBack(String data){
        this.networkManager.sendAnalyticsToBack(data);
    }

    /**
     * The update function is the main function that handles all the commands that are sent from the model and the network
     * manager
     *
     * @param o The Observable object.
     * @param arg the data that was sent from the observable
     */
    @Override
    public void update(Observable o, Object arg) {
        // This is a function that checks if the object that was sent is from the model.
        if (o.getClass().equals(model.getClass())) {
            String[] input = ((String) arg).split(":");
            String command = input[0];
            if(command.equals("instructionCommand"))
            {
                String data = input[1];
                this.networkManager.setCommand(data);
                return;
            }
            if(command.equals("AnalyticSenderCommand"))
            {
                LocalDateTime currentTime = LocalDateTime.now();
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String EndtTime = currentTime.format(timeFormatter);
                this.model.getAnalyticsHandler().setEndTime(EndtTime);
                return;
            }
            if(command.equals("ShutDownCommand"))
            {
                LocalDateTime currentTime = LocalDateTime.now();
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String EndTime = currentTime.format(timeFormatter);
                this.model.setEndTime(EndTime);
                ArrayList<ArrayList<String>> flightData = getFlightData();
                this.networkManager.ShutDown(this.model.getAnalyticsHandler().getFinalAnalytics(),flightData);
                return;
            }
            if(command.equals("PrintStreamCommand"))
            {
                this.networkManager.PrintStream();
                return;
            }
            if(command.equals("LiveStreamCommand"))
            {
                // do something
                return;
            }
            if(command.equals("FlightDataCommand"))
            {
                ArrayList<ArrayList<String>> list = getFlightData();
                sendFlightDataToBackend(list);
            }
        }
        // This is a function that checks if the object that was sent is from the network manager.
        else if (o.getClass().equals(networkManager.getClass())){
            if(arg instanceof String){
                String[] data = ((String) arg).split(":");
                // This is a function that sets the start time of the program.
                if(data[0].equals("StartTime"))
                {
                    this.model.setStartTime(data[1]);
                }
                // This is a function that sets the end time of the program.
                if(data[0].equals("EndTime"))
                {
                    this.model.setEndTime(data[1]);
                }
                else {
                    MyLogger.LogMessage(arg.toString());
                }

            }
            // This is a function that checks if the object that was sent is from the network manager.
            if(arg instanceof NetworkCommand){
                NetworkCommand c = (NetworkCommand) arg;
                CLI(c);
            }
            if (arg instanceof PlaneData){
                PlaneData tempPlane = (PlaneData) arg;
                model.setPlainData(tempPlane);
                model.sendAnalytic(tempPlane);
            }
        }
    }

}
