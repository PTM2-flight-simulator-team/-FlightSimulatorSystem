package Network.Socket.Handlers.FlightGearHandlers;

import Model.MyLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;

/**
 * It's a class that write data to the flight simulator.
 */
public class FlightGearWriter extends Observable {
    private int clientPort = 5402;
    Socket client;
    Thread clientThread;
    PrintWriter outToFG;
    volatile boolean stop;


    public FlightGearWriter(){
        // It's a function that prints the message to the console.
        MyLogger.LogMessage("writer created...");
        stop = true;
        // It's a thread that runs the StartClient function.
        clientThread = new Thread("Newest Thread"){
            public void run(){
                StartClient();
            }
        };
        clientThread.start();
    }

    private void StartClient(){
        try {
            // It's creating a new socket with the ip address 127.0.0.1 and the port 5402.
            client = new Socket("127.0.0.1",clientPort);
            // It's creating a new PrintWriter object that writes to the client's output stream.
            outToFG = new PrintWriter(client.getOutputStream());
            MyLogger.LogMessage("Writing to fg ready...");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This function takes a string as an argument and sends it to FlightGear
     *
     * @param command The command to send to FlightGear.
     */
    public void WriteToFG(String command)
    {
        // It's checking if the outToFG object is not null, if it's not null it's writing the command to the output stream.
        if(outToFG != null)
        {
            outToFG.println(command);
            outToFG.flush();
        }
    }
    public void setThrottle(double value){
        outToFG.println("set /controls/engines/current-engine/throttle " + value);
        outToFG.flush();
    }

    /**
     * It closes the output stream, closes the socket, and stops the thread
     */
    public void stop(){
        try {
            outToFG.close();
            client.close();
            clientThread.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
