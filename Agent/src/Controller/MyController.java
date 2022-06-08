package Controller;

import CommonClasses.PlainData;
import Model.Commands.instructionCommand;
import Model.MyModel;
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
//        this.model.setFrom("Tel-Aviv");
//        this.model.setTo("New-York");
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

    public ArrayList<String> getFlightData(){
        return this.model.getFlight();
    }

    public void CLI(String line){
        String[] result = line.split(":");
        if(result[0].equals("set"))
        {
            // check if the property is legit -------------------
            instructionCommand c = (instructionCommand) this.getModel().getMyCommands().get("instructions");
            c.setCommand(result[1]);
            c.execute();
            System.out.println("setter");
            return;
        }
        if(result[0].equals("printstream"))
        {
            System.out.println("printstream:");
            this.getModel().getMyCommands().get("printstream").execute();
            return;

        }
        if(result[0].equals("analytic"))
        {
            System.out.println("analytics:");
            this.getModel().getMyCommands().get("analytics").execute();
            return;
        }
        if(result[0].equals("reset")){
            System.out.println("reset:");
            this.getModel().getMyCommands().get("reset").execute();
            return;
        }
        if(result[0].equals("shutdown")){
            System.out.println("shutdown:");
            this.getModel().getMyCommands().get("shutdown").execute();
            return;
        }
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
                String analytics = this.model.getFinalAnalytics();
                System.out.println(analytics);
                // send analytics to the backend
                return;
            }
            if(command.equals("ShutDownCommand"))
            {
                this.networkManager.ShutDown();
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
                else if (data[1].startsWith("altitude"))// Analytics
                {
                    this.model.sendAnalytic(data[1]);
                    return;
                }
                else {
                    CLI(data[1]);
                }
                //aileron,3,throttle,700
                //set aileron
            }
            if (arg instanceof PlainData){
                PlainData tempPlane = (PlainData) arg;
                model.setPlainData(tempPlane);
            }
        }
    }
}
