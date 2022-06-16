package Network.Socket.Handlers;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import CommonClasses.AnalyticsData;
import CommonClasses.PlaneData;
import Network.CommandAction;
import Network.NetworkCommand;

public class BackendHandler extends  Observable implements Observer {

    Socket socket;
    String backendIP;
    int port;
    ObjectOutputStream objectOutputStream;
    ServerReaderConnection serverReader;
    String AgentID;
    String AgentName;
    public BackendHandler(String backendIP, int port){
        this.backendIP = backendIP;
        this.port = port;
        getIDAndName();
        new Thread(){
            public void run(){
                ConnectToServer();
            }
        }.start();
    }

    public void getIDAndName(){
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader("Agent/src/PlaneData.txt"));
            String[] firstrow =  scanner.nextLine().split("=");
            String id = firstrow[1];
            this.AgentID = id;
            String[] secondRow = scanner.nextLine().split("=");
            String name = secondRow[1];
            this.AgentName = name;
            System.out.println("Setted id and name");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void SendPlainData(PlaneData data){
        data.setID(AgentID);
        data.setPlainName(AgentName);
        try {
            if(objectOutputStream != null)
                objectOutputStream.writeObject(data);
            // objectOutputStream.reset();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
    }

    public void SendAirplaneData(){
        try {
            Scanner scanner = new Scanner(new FileReader("Agent/src/PlaneData.txt"));
            String[] firstrow =  scanner.nextLine().split("=");
            String id = firstrow[1];
            this.AgentID = id;
            String[] secondRow = scanner.nextLine().split("=");
            String name = secondRow[1];
            this.AgentName = name;
            if (objectOutputStream != null){
                objectOutputStream.writeObject(id + "," + name);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ConnectToServer(){
        try {
            socket = new Socket(backendIP, port);
            serverReader = new ServerReaderConnection(socket);

            serverReader.addObserver(this);
            new Thread(serverReader).start();
            OutputStream outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            InputStream inputStream = socket.getInputStream();
            SendAirplaneData();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ConnectException e) {
            System.out.println("Connection to backend failed!");
            try {
                Thread.sleep(5000);
                ConnectToServer();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Stop(){
        try {
            this.serverReader.Stop();
            this.objectOutputStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        NetworkCommand c= (NetworkCommand) arg;
        if (c.action == CommandAction.Get) {
            c.outObj = this;
        }
        setChanged();
        notifyObservers(arg);
    }

    public void sendFlightDataToBackend(ArrayList<ArrayList<String>> list) {
        try {
            if (objectOutputStream != null){
                objectOutputStream.writeObject(list);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFinalAnalytics(AnalyticsData analytics) {
        try {
            if (objectOutputStream != null){
                objectOutputStream.writeObject(analytics);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
