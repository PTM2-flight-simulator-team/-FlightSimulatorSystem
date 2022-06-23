package Controller.ServerConnection.FrontConnection;

import Controller.JsonsFuncs;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class GetFleetSizeHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        // parse request
        Map<String, Object> param = new HashMap<>();
        String query = he.getRequestURI().getQuery();
        MyNetworkStatic.parseQuery(query, param);//parse query parameters into map
        String response = JsonsFuncs.fleetSize();
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
