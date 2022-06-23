package Network.Socket.Handlers;

import java.util.*;

import CommonClasses.PlaneData;
import Network.Socket.Handlers.FlightGearHandlers.FlightGearReader;
import Network.Socket.Handlers.FlightGearHandlers.FlightGearWriter;

public class FlightgearHandler extends Observable implements Observer  {
    private FlightGearReader flightGearReader;
    private FlightGearWriter flightGearWriter;

    public FlightgearHandler(List<String> l){
         flightGearReader = new FlightGearReader(l);
         flightGearReader.addObserver(this);
    }

    /**
     * It sends a command to FlightGear
     *
     * @param command The command to send to FlightGear.
     */
    public void WriteToFG(String command)
    {
        flightGearWriter.WriteToFG(command);
    }

    public PlaneData getMyData() {
        return flightGearReader.getMyData();
    }

    /**
     * The function stops the connection between the simulator and the program
     */
    public void Stop()
    {
        flightGearWriter.stop();
        flightGearReader.stop();
    }


    /**
     * If the observable is a FlightGearReader, and the argument is a PlaneData object, then notify the observers
     *
     * @param o the object that is being observed
     * @param arg the argument that was passed to the notifyObservers method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof FlightGearReader){
            if (arg.toString().equals("startWriter")){
                flightGearWriter = new FlightGearWriter();
                flightGearWriter.addObserver(this);
            } else if(arg instanceof PlaneData){
                setChanged();
                notifyObservers(arg);
            }
        }
    }
}


