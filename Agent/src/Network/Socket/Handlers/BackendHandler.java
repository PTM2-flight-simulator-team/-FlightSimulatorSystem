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
import Model.MyLogger;
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
    Thread t;
    public BackendHandler(String backendIP, int port){
        this.backendIP = backendIP;
        this.port = port;
        // Reading the plane data from the file and setting the ID and name of the plane.
        getIDAndName();
        // Creating a new thread and starting it.
        t = new Thread("New Thread") {
            public void run(){
                ConnectToServer();
            }
        };
        t.start();
    }

    /**
     * This function reads the first two lines of the file PlaneData.txt and sets the AgentID and AgentName variables to
     * the values read from the file
     */
    public void getIDAndName(){
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader(System.getenv("APPDATA") + "\\Agent\\resources\\PlaneData.txt"));
            String[] firstrow =  scanner.nextLine().split("=");
            String id = firstrow[1];
            this.AgentID = id;
            String[] secondRow = scanner.nextLine().split("=");
            String name = secondRow[1];
            this.AgentName = name;
            MyLogger.LogMessage("Setted id and name");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * It sends the data to the Backend
     *
     * @param data The data to be sent.
     */
    public void SendPlainData(PlaneData data){
        data.setID(AgentID);
        data.setPlaneName(AgentName);
        try {
            // Checking if the objectOutputStream is not null, if it is not null, it writes the data to the
            // objectOutputStream.
            if(objectOutputStream != null)
                objectOutputStream.writeObject(data);
        } catch (IOException e) {
        }
    }

    /**
     * This function reads the data from the PlaneData file and sends it to the Backend
     */
    public void SendAirplaneData(){
        try {
            Scanner scanner = new Scanner(new FileReader(System.getenv("APPDATA") + "\\Agent\\resources\\PlaneData.txt"));
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

    /**
     * It tries to connect to the server, if it fails it waits 5 seconds and tries again
     */
    private void ConnectToServer(){
        try {
            // It creates a new socket with the IP and port specified.
            socket = new Socket(backendIP, port);
            // It creates a new ServerReaderConnection object and passes the socket to it.
            serverReader = new ServerReaderConnection(socket);

            serverReader.addObserver(this);
            // Creating a new thread and starting it.
            new Thread(serverReader).start();
            OutputStream outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            InputStream inputStream = socket.getInputStream();
            // It reads the data from the PlaneData file and sends it to the Backend.
            SendAirplaneData();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ConnectException e) {
            MyLogger.LogMessage("Connection to backend failed!");
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

    /**
     * It stops the server reader, closes the object output stream, and closes the socket
     */
    public void Stop(){
        try {
            this.serverReader.Stop();
            this.objectOutputStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * > When the model is updated, the view is notified
     *
     * @param o The object that is being observed.
     * @param arg The object that is being passed to the observer.
     */
    @Override
    public void update(Observable o, Object arg) {
        NetworkCommand c= (NetworkCommand) arg;
        // Checking if the action is a get action, if it is, it sets the outObj to the current object.
        if (c.action == CommandAction.Get) {
            c.outObj = this;
        }
        setChanged();
        notifyObservers(arg);
    }

    /**
     * This function takes in a ArrayList of strings, and sends it to the backend
     *
     * @param list The list of flight data to be sent to the backend.
     */
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

    /**
     * It sends the final analytics data to the Backend.
     *
     * @param analytics The analytics object that you want to send to the server.
     */
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
