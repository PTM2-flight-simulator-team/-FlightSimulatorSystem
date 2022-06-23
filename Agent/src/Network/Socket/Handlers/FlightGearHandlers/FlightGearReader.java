package Network.Socket.Handlers.FlightGearHandlers;

import CommonClasses.PlaneData;
import Model.MyLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

/**
 * It's a class that reads data from a flight simulator and notifies observers when new data is available
 */
public class FlightGearReader extends Observable {
    // The port that the server will listen to.
    int serverPort = 5400;
    // A hashmap that will hold the data that is read from the server.
    HashMap<String,String> newData;
    // It's creating a new server socket and a new socket that will be used to accept connections from clients.
    ServerSocket server;
    Socket serverAccept;
    // Creating a new thread that will run the StartServer function.
    Thread serverThread;
    // A variable that holds the last data that was read from the server.
    PlaneData myData;
    // A flag that is used to stop the server.
    volatile boolean stop;

    // Creating a new thread that will run the StartServer function.
    public  FlightGearReader(List<String> l){
        newData = new HashMap<>();
        // A flag that is used to stop the server.
        stop = true;

        // Creating a new thread that will run the StartServer function.
        serverThread = new Thread("New Thread") {
            public void run(){
                StartServer(l);
            }
        };
        serverThread.start();
    }

    private void StartServer(List<String> l){
        try {
            // It creates a new server socket on the given port.
            server = new ServerSocket(serverPort);
            // It waits for a client to connect to the server.
            serverAccept = server.accept();
            MyLogger.LogMessage("Reading from fg ready...");
            // Notifying the writer to start writing.
            setChanged();
            notifyObservers("startWriter");
            // It creates a new BufferedReader that will read from the serverAccept socket.
            BufferedReader in = new BufferedReader(new InputStreamReader(serverAccept.getInputStream()));
            String line;
            // Reading the data from the FG and splitting it into a hashmap.
            while(stop == true && (line = in.readLine())!=null)
            {
                String[] vals =line.split(",");
                // Putting the values from the string array into the hashmap.
                for(int i = 0; i < vals.length; i++){
                    newData.put(l.get(i), vals[i]);
                }
                // Creating a new PlaneData object and passing the newData hashmap to the constructor.
                PlaneData data = new PlaneData(newData);
                // Saving the last data that was read from the server.
                myData = data;
                // Notifying the observers that the data has changed.
                setChanged();
                notifyObservers(data);
            }
            // It closes the connection to the server.
            in.close();
            serverAccept.close();

        }catch (SocketException se){
            return;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public PlaneData getMyData() {
        return myData;
    }


    /**
     * The function stops the server by closing the server socket and the server accept socket
     */
    public void stop() {
        try {
            stop = false;
            serverAccept.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
