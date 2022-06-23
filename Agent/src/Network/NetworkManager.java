package Network;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Network.Socket.MySocketHandler;

/**
 * The NetworkManager class is an observer of the MySocketHandler class, and it is also an observable class
 */
public class NetworkManager extends Observable implements Observer{
    MySocketHandler socketHandler;

    public NetworkManager(List<String> l){
        socketHandler = new MySocketHandler(l);
        socketHandler.addObserver(this);
    }

    public MySocketHandler getSocketHandler() {
        return socketHandler;
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        if(arg instanceof String)
        {
            setChanged();
            notifyObservers(arg);
            return;
        }
         setChanged();
         notifyObservers(arg);
    }

    /**
     * This function sets the command that the socket handler will send to the FG.
     *
     * @param command The command to be executed on the remote machine.
     */
    public void setCommand(String command){
        this.socketHandler.setCommand(command);
    }

    public void HandleGetDataFromFG(Object arg){
        // MyResponse res = (MyResponse)arg;
        // res.RespondWith(socketHandler.GetResponse());
    }

    /**
     * This function is called by the Analytic class to send a shutdown message to the socketHandler
     *
     * @param analytic The name of the analytic you want to shut down.
     * @param flightData This is the data that you want to send to the Backend. It is an ArrayList of ArrayLists of Strings.
     * The outer ArrayList represents the rows of the table, and the inner ArrayList represents the columns of the table.
     */
    public void ShutDown(String analytic, ArrayList<ArrayList<String>> flightData){
        this.socketHandler.ShutDown(analytic,flightData);
    }

    /**
     * It prints the stream of the Agent.
     */
    public void PrintStream(){
        this.socketHandler.PrintStream();
    }

    /**
     * This function sends the flight data to the backend.
     *
     * @param list The list of flight data to be sent to the backend.
     */
    public void sendFlightDataToBackend(ArrayList<ArrayList<String>> list) {
        this.socketHandler.sendFlightDataToBackend(list);
    }
    /**
     * Send analytics data to the backend
     *
     * @param data The data to be sent to the backend.
     */
    public void sendAnalyticsToBack(String data){
        this.socketHandler.sendAnalyticsToBack(data);
    }

}
