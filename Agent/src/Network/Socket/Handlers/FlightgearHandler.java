package Network.Socket.Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    PlainData myData;

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
            // check
            BufferedReader in = new BufferedReader(new InputStreamReader(serverAccept.getInputStream()));
            String line;
            while((line = in.readLine())!=null)
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

    public PlainData getMyData() {
        return myData;
    }

    public void Stop()
    {
        try {
            this.server.close();
            this.serverAccept.close();
            this.client.close();
            // need to check
            this.serverThread.stop();
            this.clientThread.stop();
            this.outToFG.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AutoPilot(){
//        try {
//            Thread.sleep(120000);
//        } catch (InterruptedException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
        System.out.println("Starting autoPilot");
        WriteToFG("set /controls/flight/speedbrake 0");

        WriteToFG("set /controls/engines/current-engine/throttle 1");
//        WriteToFG("set /controls/engines/current-engine/throttle 500");

        double h = Double.parseDouble(myData.getHeading());
        System.out.println(h);
        double minus = -1.0;
        double a = Double.parseDouble(myData.getAltitude());
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("start");
        System.out.println(a);
        while((a - Double.parseDouble(myData.getAltitude()) > -50))
        {
            WriteToFG("set /controls/flight/rudder "+ ((h - Double.parseDouble(myData.getHeading())) / 20));
            WriteToFG("set /controls/flight/aileron " + ((-1 * ((Double.parseDouble(myData.getRollDeg())))) / 70));
            WriteToFG("set /controls/flight/elevator " + ((Double.parseDouble(myData.getPitchDeg())) / 50));
            System.out.println(myData.getAltitude());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("change");
        while((Double.parseDouble(myData.getAltitude())) < 1000){
            WriteToFG("set /controls/flight/rudder "+ ((h - Double.parseDouble(myData.getHeading())) / 200));
            WriteToFG("set /controls/flight/aileron " + ((-1 * ((Double.parseDouble(myData.getRollDeg())))) / 200));
            WriteToFG("set /controls/flight/elevator " + ((Double.parseDouble(myData.getPitchDeg())) / 50));
            System.out.println(myData.getAltitude());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done");
    }

    public void takeoff(){
        WriteToFG("set /controls/flight/speedbrake 0");
        WriteToFG("set /controls/engines/current-engine/throttle 1");
        double h0 = Double.parseDouble(myData.getHeading());
        while((Double.parseDouble(myData.getAltitude())) < 1000){
            WriteToFG("set /controls/flight/rudder "+ ((h0 - Double.parseDouble(myData.getHeading())) / 20));
            WriteToFG("set /controls/flight/aileron " + ((-1 * ((Double.parseDouble(myData.getRollDeg())))) / 70));
            WriteToFG("set /controls/flight/elevator " + ((Double.parseDouble(myData.getPitchDeg())) / 50));
            System.out.println("Alt: " + myData.getAltitude());
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


