package Network.Socket.Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import CommonClasses.PlainData;
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

    public PlainData getMyData() {
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
            } else if(arg instanceof PlainData){
                setChanged();
                notifyObservers(arg);
            }
        }
    }
}


