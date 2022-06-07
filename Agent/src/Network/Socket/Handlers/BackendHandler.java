package Network.Socket.Handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import CommonClasses.PlainData;

public class BackendHandler extends  Observable implements Observer {

    Socket socket;
    String backendIP;
    int port;
    ObjectOutputStream objectOutputStream;
    ServerReaderConnection serverReader;
    public BackendHandler(String backendIP, int port){
        this.backendIP = backendIP;
        this.port = port;
        new Thread(){
            public void run(){
                ConnectToServer();
            }
        }.start();
    }

    public void SendPlainData(PlainData data){
        try {
            if(objectOutputStream != null)
                objectOutputStream.writeObject(data);
            // objectOutputStream.reset();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
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
            this.objectOutputStream.close();
            this.serverReader.Stop();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
