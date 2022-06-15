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

    public void WriteToFG(String command)
    {
        flightGearWriter.WriteToFG(command);
    }

    public PlaneData getMyData() {
        return flightGearReader.getMyData();
    }

    public void Stop()
    {
        flightGearWriter.stop();
        flightGearReader.stop();
    }


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


