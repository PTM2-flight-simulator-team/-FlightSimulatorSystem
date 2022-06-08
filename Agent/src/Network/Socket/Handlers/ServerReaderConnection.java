package Network.Socket.Handlers;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class ServerReaderConnection extends Observable implements Runnable {
    private Socket server;
    private BufferedReader in;
    volatile boolean stop = false;
    public ServerReaderConnection(Socket s){
        server = s;
        try {
//            InputStream inputStream = server.getInputStream();
//            InputStream bufferedIn = new BufferedInputStream(inputStream);
            in =  new BufferedReader(new InputStreamReader(server.getInputStream()));

            System.out.println("Connected to server");

        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
    @Override
    public void run() {
        while (!stop){
            try {
                String line = in.readLine();
                System.out.println(line);
                setChanged();
                notifyObservers(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void Stop(){
        try {
            stop = true;
            this.in.close();
            this.server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
