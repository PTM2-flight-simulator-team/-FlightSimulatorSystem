package Controller.ServerConnection.FrontConnection;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyHttpServer extends Observable implements Observer , Runnable {
    private HttpServer httpServer;
    int port;
    public MyHttpServer() {
        this.port = 9000;
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GetHandler gh = new GetHandler(this);
        PostHandler ph = new PostHandler();
        gh.addObserver(this);
        ph.addObserver(this);
        httpServer.createContext("/GET", gh);
        httpServer.createContext("/POST",ph);
        httpServer.setExecutor(null);
    }

    @Override
    public void run() {
        httpServer.start();
        System.out.println("http server is running...");
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
        List<String> list = (List<String>) arg;
        for(String s: list){
            System.out.println(s);
        }
    }
}
