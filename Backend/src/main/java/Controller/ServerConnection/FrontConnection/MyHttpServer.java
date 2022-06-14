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
        GetPlaneDataHandler gpdh = new GetPlaneDataHandler();//  /GET/PlaneData
        JoystickHandler jh = new JoystickHandler();//    /POST/Joystick
        CodeHandler ch = new CodeHandler();//  /POST/Code
        jh.addObserver(this);
        ch.addObserver(this);
        httpServer.createContext("/GET/PlaneData", gpdh);
        httpServer.createContext("/POST/Code",ch);
        httpServer.createContext("/POST/Joystick",jh);
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
