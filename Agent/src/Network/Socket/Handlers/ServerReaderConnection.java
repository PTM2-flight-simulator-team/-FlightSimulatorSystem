package Network.Socket.Handlers;

import Model.MyLogger;
import Network.CommandAction;
import Network.NetworkCommand;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Observable;

/**
 * > This class is an observable that runs a thread that reads from a socket and notifies observers when it receives a
 * message
 */
public class ServerReaderConnection extends Observable implements Runnable {
    private Socket server;
    private BufferedReader in;
    volatile boolean stop = false;

    public ServerReaderConnection(Socket s){
        server = s;
        try {
            in =  new BufferedReader(new InputStreamReader(server.getInputStream()));
            MyLogger.LogMessage("Connected to server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * It reads a line from the socket, parses it, and then notifies the observers of the command
     */
    @Override
    public void run() {
            while (!stop) {
                try {
                    String line = in.readLine();
                    if(line == null)
                        break;
                    String[] words = line.split(" ");

                    if (words.length < 2)
                        continue;

                    NetworkCommand c = new NetworkCommand();
                    c.fromObj = this;
                    c.fullArg = line;
                    c.path = words[1];
                    if (words[0].toLowerCase().equals("set")) {
                        c.action = CommandAction.Set;
                        c.value = words[2];
                    } else if (words[0].toLowerCase().equals("get")) {
                        c.action = CommandAction.Get;
                    } else {
                        c.action = CommandAction.Do;
                    }

                    setChanged();
                    notifyObservers(c);
                } catch (SocketException e) {
                    Stop();
                    MyLogger.LogMessage("Server Error: Disconnected");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

    }

    /**
     * It closes the input stream and the server socket
     */
    public void Stop(){
        try {
            this.stop = true;
            this.in.close();
            this.server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
