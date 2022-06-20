package Network.Socket;

import java.util.*;

import CommonClasses.AnalyticsData;
import CommonClasses.PlaneData;
import Model.MyLogger;
import Network.CommandAction;
import Network.NetworkCommand;
import Network.SSHhandler;
import Network.Socket.Handlers.BackendHandler;
import Network.Socket.Handlers.FlightgearHandler;

public class MySocketHandler extends Observable implements Observer {
    
    private FlightgearHandler fgHandler;
    private BackendHandler backHandler;
    public SSHhandler SSH;
    public MySocketHandler(List l){
        fgHandler = new FlightgearHandler(l);
        backHandler = new BackendHandler("127.0.0.1",5899);
        SSH = new SSHhandler();

        fgHandler.addObserver(this);
        backHandler.addObserver(this);
        SSH.addObserver(this);
        SSH.runCli();
    }

    public String GetResponse(){
        return "<h1>This is get response</h1>";
    }

    public FlightgearHandler getFgHandler() {
        return fgHandler;
    }



    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        if(o.getClass().equals(fgHandler.getClass())){
            if(arg instanceof String){
                setChanged();
                notifyObservers(arg);
            }
            //System.out.println("Airplane Data Sent...");
            //backHandler.SendAirplaneData();
            PlaneData data =  (PlaneData)arg;
            if(data != null)
            {
                backHandler.SendPlainData(data);
//                String Analytic = "Analytic:" +"altitude "+ data.getAltitude() + " speed " + data.getAirSpeed_kt(); // add all the data you want to compare
//                setChanged();
//                notifyObservers(Analytic);
                setChanged();
                notifyObservers(data);
            }
        }
        if(o.getClass().equals(backHandler.getClass())){
            setChanged();
            notifyObservers(arg);
        }
        if(o instanceof SSHhandler){
            NetworkCommand c= (NetworkCommand) arg;
            if (c.action == CommandAction.Get) {
                c.outObj = this;
            }
            setChanged();
            notifyObservers(arg);
        }
        
    }

    public void setCommand(String command){
        this.fgHandler.WriteToFG(command);
    }

    public void ShutDown(String analytic, ArrayList<ArrayList<String>> flightData){

        this.fgHandler.Stop();
        AnalyticsData analyticsData = new AnalyticsData(analytic);
        this.backHandler.sendFinalAnalytics(analyticsData);
        this.sendFlightDataToBackend(flightData);
//        this.backHandler.sendFinalAnalytics(analytic);
        MyLogger.LogMessage("sent Final Analytics");
        MyLogger.LogMessage("The analytics are:");
        MyLogger.LogMessage(analytic);
        this.backHandler.Stop();
        this.SSH.Stop();
        MyLogger.LogMessage("Stopped everything");
//        System.exit(0);
    }

    public void PrintStream(){
        this.fgHandler.getMyData().Print();
    }

    public void sendFlightDataToBackend(ArrayList<ArrayList<String>> list) {
        this.backHandler.sendFlightDataToBackend(list);
    }
    public void sendAnalyticsToBack(String data){
        AnalyticsData analyticsData = new AnalyticsData(data);
        this.backHandler.sendFinalAnalytics(analyticsData);
    }

}
