package Network.Socket.Handlers.FlightGearHandlers;

import CommonClasses.PlainData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class FlightGearReader extends Observable {
    int serverPort = 5400;
    HashMap<String,String> newData; //server => reader
    ServerSocket server;
    Socket serverAccept;
    Thread serverThread;
    PlainData myData;
    volatile boolean stop;

    public  FlightGearReader(List<String> l){
        newData = new HashMap<>();
        stop = true;

        serverThread = new Thread("New Thread") {
            public void run(){
                StartServer(l);
            }
        };
        serverThread.start();
    }
    private void StartServer(List<String> l){
        try {
            server = new ServerSocket(serverPort);
            serverAccept = server.accept();
            System.out.println("Reading from fg ready...");
            setChanged();
            notifyObservers("startWriter");
            // check
            BufferedReader in = new BufferedReader(new InputStreamReader(serverAccept.getInputStream()));
            String line;
            while((line = in.readLine())!=null && stop == true)
            {
                // System.out.println(line);
                String[] vals =line.split(",");
                for(int i = 0; i < vals.length; i++){
                    newData.put(l.get(i), vals[i]);
                }
                PlainData data = new PlainData("ShimiHagever",newData); // ailreron 1 ,,,,,,
                myData = data;
//                data.Print();
                setChanged();
                notifyObservers(data);
                // System.out.println(newData.toString());
            }
            in.close();
            serverAccept.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public PlainData getMyData() {
        return myData;
    }


    public void stop() {
        try {
            serverAccept.close();
            server.close();
            stop = false; //serverThread.stop()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
