package Network.Socket.Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import CommonClasses.PlainData;

public class FlightgearHandler extends Observable{
    int clientPort = 5402;
    int serverPort = 5400;
    HashMap<String,String> newData;
    ServerSocket server;
    Socket serverAccept;
    Socket client;
    Thread serverThread;
    Thread clientThread;
    PrintWriter outToFG;

    public FlightgearHandler(List l){
        newData = new HashMap<>();

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
            clientThread = new Thread("Newest Thread"){
                public void run(){
                   StartClient();
                }
            };
            clientThread.start();   
            BufferedReader in = new BufferedReader(new InputStreamReader(serverAccept.getInputStream()));
            String line;
            while((line = in.readLine())!=null)
            {
                // System.out.println(line);
                String[] vals =line.split(",");
                for(int i = 0; i < vals.length; i++){
                    newData.put(l.get(i), vals[i]);
                }
                PlainData data = new PlainData("ShimiHagever",newData);
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
}
