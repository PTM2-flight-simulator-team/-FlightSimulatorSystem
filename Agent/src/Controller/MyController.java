package Controller;

import CommonClasses.PlaneData;
import Model.Commands.instructionCommand;
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

    public MyController(MyModel model) {
        this.networkManager = new NetworkManager(model.GetNamesList());
        this.model = model;
        this.model.addObserver(this);
        networkManager.addObserver(this);
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

    public void CLI(NetworkCommand networkCommand){
//        String[] result = line.split(":");
        if(networkCommand.action == CommandAction.Set)
        {
            // check if the property is legit -------------------
            instructionCommand c = (instructionCommand) this.getModel().getMyCommands().get("instructions");
            c.setCommand(networkCommand.path + " " + networkCommand.value);
            c.execute();
            System.out.println("setter");
            return;
        }
        if(networkCommand.action == CommandAction.Do)
        {
            if(networkCommand.path.contains("printstream"))
            {
                System.out.println("printstream:");
                this.getModel().getMyCommands().get("printstream").execute();
                return;
            }
            if(networkCommand.path.contains("reset")){
                System.out.println("reset:");
                this.getModel().getMyCommands().get("reset").execute();
                return;
            }
            if(networkCommand.path.contains("shutdown")){
                System.out.println("shutdown:");
                this.getModel().getMyCommands().get("analytics").execute();
                this.getModel().getMyCommands().get("shutdown").execute();
                return;
            }


        }
        if(networkCommand.action == CommandAction.Get){
            if(networkCommand.path.contains("FlightData")){
                System.out.println("FlightDataCommand");
                this.getModel().getMyCommands().get("FlightDataCommand").execute();
                return;
            }
            if(networkCommand.path.contains("analytic"))
            {
                System.out.println("analytics:");
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

    @Override
    public void update(Observable o, Object arg) {
        if (o.getClass().equals(model.getClass())) {
            //instructionCommand:set aileron 0.2
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
//                sendAnalyticsToBack(this.model.getFinalAnalytics());
                // send analytics to the backend
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
                // need to check if something is broken because we close everything
                return;
            }
//            if(command.equals("ResetCommand"))
//            {
//                // do something
//                return;
//            }
            if(command.equals("PrintStreamCommand"))
            {
                // maybe send the result as string or print it?
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
//                System.out.println(list);
            }
        }
        else if (o.getClass().equals(networkManager.getClass())){
            if(arg instanceof String){
                String[] data = ((String) arg).split(":");
                if(data[0].equals("StartTime"))
                {
                    this.model.setStartTime(data[1]);
                }
                if(data[0].equals("EndTime"))
                {
                    this.model.setEndTime(data[1]);
                }
//                else if (data.length > 1 && data[1].startsWith("altitude"))// Analytics
//                {
//                    this.model.sendAnalytic(data[1]);
//                    return;
//                }
                else {
                    System.out.println(arg);
//                    CLI((String) arg);
                }
                //aileron,3,throttle,700
                //set aileron
            }
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
