package Controller.ServerConnection.AgentConnections;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class AgentListener implements Runnable {
    private Socket client;
    private ObjectInputStream in;
    private boolean running;

    public AgentListener(Socket client) {
        this.client = client;
        try {
            InputStream inputStream = client.getInputStream();
            InputStream bufferedIn = new BufferedInputStream(inputStream);
            in = new ObjectInputStream(bufferedIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        this.running = true;
        while (this.running) {
            try {
                System.out.println("running");

                Object fromAgent = in.readObject();// plaindata

                if (fromAgent != null) {
                    String strFromAgent = (String) fromAgent;
                    System.out.println(strFromAgent);
                }
            }catch (SocketException se){
                this.stopListening();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopListening() {
        this.running = false;
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

