package Controller.ServerConnection.FrontConnection;

import Controller.JsonsFuncs;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;

public class ShutDownHandler extends Observable implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
        Map<String, Object> param = new HashMap<>();
        String query = he.getRequestURI().getQuery();
        System.out.println(query);
        MyNetworkStatic.parseQuery(query, param);//parse query parameters into map
        List<String> args = new ArrayList<>();
        args.add("shutdown");//args[0] = shutdown
        args.add((String) param.get("plane_id"));//args[1] is the plane id
        setChanged();
        notifyObservers(args);//sending up list of args
    }
}

