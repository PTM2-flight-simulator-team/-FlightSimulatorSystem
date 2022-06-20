package Controller.ServerConnection.FrontConnection;

import Controller.JsonsFuncs;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class GetTsHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange he) throws IOException {
        // parse request
        Map<String, Object> param = new HashMap<>();
        String query = he.getRequestURI().getQuery();
        System.out.println(query);
        MyNetworkStatic.parseQuery(query, param);//parse query parameters into map
        int index = Integer.parseInt((String) param.get("flightId"));
        String response = JsonsFuncs.getTimeSeries((String) param.get("plane_id"), index);
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}

