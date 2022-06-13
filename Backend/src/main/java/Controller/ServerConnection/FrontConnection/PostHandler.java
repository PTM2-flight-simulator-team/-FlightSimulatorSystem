package Controller.ServerConnection.FrontConnection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class PostHandler extends Observable implements HttpHandler {


    @Override
    public void handle(HttpExchange he) throws IOException
    {
        // parse request
        Map<String, Object> parameters = new HashMap<>();
        String query = he.getRequestURI().getQuery();
        System.out.println(query);
        MyNetworkStatic.parseQuery(query, parameters);
        for(String s: parameters.keySet()){
            System.out.println("key: " + s + " value: " + (String) parameters.get(s));
        }
        String action = (String) parameters.get("action");
        System.out.println(action);
        if(action.equals("Joystick")){
            List<String> args = null;
            try {
                args = JoystickHandler.handle(parameters,he);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            setChanged();
            notifyObservers(args);
        }else if(action.equals("Code")){
            List<String> args = CodeHandler.handle(parameters,he);
            setChanged();
            notifyObservers(args);
        }
    }
}
