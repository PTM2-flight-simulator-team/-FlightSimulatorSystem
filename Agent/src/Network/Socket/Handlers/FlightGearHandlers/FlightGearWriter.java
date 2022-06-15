package Network.Socket.Handlers.FlightGearHandlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;

public class FlightGearWriter extends Observable {
    private int clientPort = 5402;
    Socket client;
    Thread clientThread;
    PrintWriter outToFG;
    volatile boolean stop;


    public FlightGearWriter(){
        System.out.println("writer created...");
        stop = true;
        clientThread = new Thread("Newest Thread"){
            public void run(){
                StartClient();
            }
        };
        clientThread.start();
    }

    private void StartClient(){
        try {
            client = new Socket("127.0.0.1",clientPort);
            outToFG = new PrintWriter(client.getOutputStream());
            System.out.println("Writing to fg ready...");
            // WriteToFG("set /controls/flight/aileron[0] 1");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void WriteToFG(String command)
    {
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
