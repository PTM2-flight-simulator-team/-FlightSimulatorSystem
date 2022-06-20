package Network.Socket.Handlers;

import Network.CommandAction;
import Network.NetworkCommand;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
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
                String[] words = line.split(" ");

                if (words.length < 2)
                    continue;

                NetworkCommand c = new NetworkCommand();
                c.fromObj = this;
                c.fullArg = line;
                c.path = words[1];
                if (words[0].toLowerCase().equals("set"))
                {
                    c.action = CommandAction.Set;
                    c.value = words[2];
                }
                else if(words[0].toLowerCase().equals("get")){
                    c.action = CommandAction.Get;
                }
                else{
                    c.action = CommandAction.Do;
                }

                setChanged();
                notifyObservers(c);
            }catch (SocketException e) {
                Stop();
                System.out.println("Server Error: Disconnected");
            }catch (IOException e) {
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
