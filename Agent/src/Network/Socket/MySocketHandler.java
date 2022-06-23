package Network.Socket;

import java.util.*;

import CommonClasses.AnalyticsData;
import CommonClasses.PlaneData;
import Model.MyLogger;
import Network.CommandAction;
import Network.NetworkCommand;
import Network.SSHhandler;
import Network.Socket.Handlers.BackendHandler;
import Network.Socket.Handlers.FlightGearHandlers.FlightGearReader;
import Network.Socket.Handlers.FlightgearHandler;


/**
 * It's a class that handles all the communication between the Agent and the backend
 */
public class MySocketHandler extends Observable implements Observer {
    
    // It's a class that handles all the communication between the Agent and the backend
    private FlightgearHandler fgHandler;
    private BackendHandler backHandler;
    public SSHhandler SSH;
    public MySocketHandler(List l){
        fgHandler = new FlightgearHandler(l);
        backHandler = new BackendHandler("127.0.0.1",5899);
        SSH = new SSHhandler();

        // It's adding the socket handler as an observer to the other handlers.
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



    // It's a method that is called when the observable object is changed.
    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        // It's checking if the object that called the update method is the same as the fgHandler object.
        if(o.getClass().equals(fgHandler.getClass())){
            if(arg instanceof String){
                setChanged();
                notifyObservers(arg);
            }
            // It's casting the object to PlaneData.
            PlaneData data =  (PlaneData)arg;
            // It's sending the data to the backend.
            if(data != null)
            {
                backHandler.SendPlainData(data);
                setChanged();
                notifyObservers(data);
            }
        }
        if(o.getClass().equals(backHandler.getClass())){
            setChanged();
            notifyObservers(arg);
        }
        // It's checking if the object that called the update method is the same as the SSHhandler object.
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

    /**
     * It stops the FlightGearHandler, sends the final analytics to the backend, sends the flight data to the backend,
     * stops the backend handler and stops the SSH connection
     *
     * @param analytic a string that contains the final analytics of the flight.
     * @param flightData is the data that the agent sent to the TS.
     */
    public void ShutDown(String analytic, ArrayList<ArrayList<String>> flightData){
        this.fgHandler.Stop();
        AnalyticsData analyticsData = new AnalyticsData(analytic);
        this.backHandler.sendFinalAnalytics(analyticsData);
        this.sendFlightDataToBackend(flightData);
        MyLogger.LogMessage("sent Final Analytics");
        MyLogger.LogMessage("The analytics are:");
        MyLogger.LogMessage(analytic);
        this.backHandler.Stop();
        this.SSH.Stop();
        MyLogger.LogMessage("Stopped everything");
    }

    /**
     * PrintStream() prints the data in the data structure.
     */
    public void PrintStream(){
        this.fgHandler.getMyData().Print();
    }

    /**
     * This function sends the flight data to the backend.
     *
     * @param list The list of flights to be sent to the backend.
     */
    public void sendFlightDataToBackend(ArrayList<ArrayList<String>> list) {
        this.backHandler.sendFlightDataToBackend(list);
    }
    /**
     * Send the data to the back handler.
     *
     * @param data The data to be sent to the back end.
     */
    public void sendAnalyticsToBack(String data){
        AnalyticsData analyticsData = new AnalyticsData(data);
        this.backHandler.sendFinalAnalytics(analyticsData);
    }

}
